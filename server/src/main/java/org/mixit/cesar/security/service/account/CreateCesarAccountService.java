package org.mixit.cesar.security.service.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.common.base.Strings;
import org.mixit.cesar.security.service.exception.EmailExistException;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.repository.AuthorityRepository;
import org.mixit.cesar.site.model.member.SharedLink;
import org.mixit.cesar.site.repository.InterestRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.repository.SharedLinkRepository;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.mixit.cesar.security.service.authentification.CryptoService;
import org.mixit.cesar.security.service.exception.LoginExistException;
import org.mixit.cesar.security.service.exception.UserNotFoundException;
import org.mixit.cesar.security.service.mail.MailBuilder;
import org.mixit.cesar.security.service.mail.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CreateCesarAccountService {

    @Autowired
    private MailerService mailerService;

    @Autowired
    private MailBuilder mailBuilder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private SharedLinkRepository sharedLinkRepository;

    @Autowired
    public AbsoluteUrlFactory urlFactory;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CryptoService cryptoService;

    private Member updateMember(Member member, Account account){
        member.setLogin(account.getLogin())
                .setEmail(account.getEmail())
                .setFirstname(account.getFirstname())
                .setLastname(account.getLastname());
        return member;
    }

    /**
     * Create an account with user password
     */
    public Account createNormalAccount(Account account) {
        //Step1: Account ids are generated
        account.setProvider(OAuthProvider.CESAR);
        account.setOauthId(UUID.randomUUID().toString());
        tokenService.generateNewToken(account);

        //Step2: we check if login exist
        if (accountRepository.findByLogin(account.getLogin()) != null) {
            throw new LoginExistException();
        }

        //Step3: we check if a member exist with the same email
        Member member = tokenService.tryToLinkWithActualMember(account);
        if(member==null){
            member = memberRepository.save(updateMember(new Member(), account));
        }
        else{
            member.getROLES().clear();
            updateMember(member, account);
        }

        //Step4: a member is created but invalid
        //Step5 : account is created
        account
                .setValid(false)
                .setMember(member)
                .setPassword(cryptoService.passwordHash(account.getPassword()));

        accountRepository.save(account).addAuthority(authorityRepository.findByName(Role.MEMBER));

        //Step6: a mail with a token is send to the user. He has to confirm it before 3
        mailerService.send(
                account.getEmail(),
                mailBuilder.getTitle(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, account),
                mailBuilder.buildContent(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, account, Optional.empty()));

        return account;
    }

    /**
     * Update an account
     */
    public Account updateAccount(Account account) {
        //Step1: we read account in database
        Account accountDb = accountRepository.findByOauthProviderAndId(account.getProvider(), account.getOauthId());
        Member member = Optional.ofNullable(account.getMember()).orElse(new Member());

        if (accountDb == null) {
            throw new UserNotFoundException();
        }

        boolean emailChanged = !accountDb.getEmail().equals(account.getEmail());

        if(emailChanged){
            //We check that the email is not used by another account
            tokenService.tryToLinkWithActualMember(accountDb);
            memberRepository.findByEmail(account.getEmail()).stream().forEach(m -> {
                if (m.getId().equals(member.getId())) {
                    throw new EmailExistException();
                }
            });
        }

        //Data are updated
        accountDb
                .setEmail(account.getEmail())
                .setLastname(account.getLastname())
                .setFirstname(account.getFirstname())
                .setDefaultLanguage(account.getDefaultLanguage());

        //Member linked to the account is also updated
        accountDb.getMember()
                .setEmail(account.getEmail())
                .setLastname(account.getLastname())
                .setFirstname(account.getFirstname())
                .setCompany(member.getCompany())
                .setShortDescription(member.getShortDescription())
                .setLongDescription(member.getLongDescription());

        //We need to update interests
        updateMemberInterest(accountDb.getMember(), member);

        //And links
        updateMemberShareLinks(accountDb.getMember(), member);

        //An email is send if mail changes
        if (emailChanged) {
            mailerService.send(
                    account.getEmail(),
                    mailBuilder.getTitle(MailBuilder.TypeMail.EMAIL_CHANGED, account),
                    mailBuilder.buildContent(MailBuilder.TypeMail.EMAIL_CHANGED, account, Optional.empty()));
        }

        return accountRepository.save(accountDb);
    }

    /**
     * Helper to update the interest list
     */
    protected void updateMemberInterest(Member<Member> memberDb, Member<Member> member){
        List<Interest> interestToDelete = new ArrayList<>();
        memberDb.getInterests()
                .stream()
                .forEach(inter -> {
                    Optional<Interest> interest = member.getInterests().stream().filter(i -> i.getName().equals(inter.getName())).findAny();
                    if(!interest.isPresent()){
                        interestToDelete.add(inter);
                    }
                });

        //We can delete old references
        memberDb.getInterests().removeAll(interestToDelete);

        member
                .getInterests()
                .stream()
                .forEach(inter -> {
                    Optional<Interest> interest = memberDb.getInterests().stream().filter(i -> i.getName().equals(inter.getName())).findAny();
                    if(!interest.isPresent()){
                        Interest interes = interestRepository.findByName(inter.getName());
                        if(interes==null){
                            interes = interestRepository.save(new Interest().setName(inter.getName()));
                        }
                        memberDb.addInterest(interes);
                    }
                });
    }

    /**
     * Helper to update the user links (we delete all of them who have an empty URL
     */
    protected void updateMemberShareLinks(Member<Member> memberDb, Member<Member> member){
        List<SharedLink> sharedLinksToDelete = new ArrayList<>();
        memberDb.getSharedLinks()
                .stream()
                .forEach(sl -> {
                    //We search the reference in the links sent by user
                    Optional<SharedLink> sharedLink = member.getSharedLinks().stream().filter(i -> i.getId().equals(sl.getId())).findAny();
                    if ( sharedLink.isPresent() && !Strings.isNullOrEmpty(sharedLink.get().getURL())){
                        sl.setName(sharedLink.get().getName()).setURL(sharedLink.get().getURL());
                    }
                    else{
                        sharedLinksToDelete.add(sl);
                    }
                });

        //We can delete old references
        memberDb.getSharedLinks().removeAll(sharedLinksToDelete);
        sharedLinkRepository.delete(sharedLinksToDelete);

        //We need to find the new occurrences to create
        member
                .getSharedLinks()
                .stream()
                .filter(sl -> sl.getId() == null && !Strings.isNullOrEmpty(sl.getURL()))
                .forEach(sl -> memberDb.addSharedLink(sharedLinkRepository.save(new SharedLink().setName(sl.getName()).setURL(sl.getURL()).setMember(memberDb))));

    }
}

package org.mixit.cesar.security.service.account;

import java.util.Optional;
import java.util.UUID;

import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.repository.AuthorityRepository;
import org.mixit.cesar.site.repository.MemberRepository;
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

        //Step4: a member is created but invalid
        account.setValid(false);
        member = memberRepository.save(updateMember(member==null ? new Member() : member, account));

        //Step5 : account is created
        account
                .setMember(member)
                .addAuthority(authorityRepository.findByName(Role.MEMBER))
                .setPassword(cryptoService.passwordHash(account.getPassword()));

        accountRepository.save(account);

        //Step6: a mail with a token is send to the user. He has to confirm it before 3
        mailerService.send(
                account.getEmail(),
                "Account validation",
                mailBuilder.createHtmlMail(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, account, Optional.empty()));

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


        //An email is send if mail changes
        if (emailChanged) {
            mailerService.send(
                    account.getEmail(),
                    "Email changed",
                    mailBuilder.createHtmlMail(MailBuilder.TypeMail.EMAIL_CHANGED, account, Optional.empty()));
        }

        return accountRepository.save(accountDb);
    }
}

package org.mixit.cesar.service.account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Preconditions;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.model.session.SessionLanguage;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.repository.AuthorityRepository;
import org.mixit.cesar.repository.MemberRepository;
import org.mixit.cesar.service.exception.EmailExistException;
import org.mixit.cesar.service.exception.InvalidTokenException;
import org.mixit.cesar.service.mail.MailBuilder;
import org.mixit.cesar.service.mail.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSocialAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MailerService mailerService;

    @Autowired
    private MailBuilder mailBuilder;

    @Autowired
    private TokenService tokenService;

    /**
     * Create an account linked to a social network {@link OAuthProvider}
     */
    public Account createAccount(OAuthProvider provider, String oauthId) {
        Preconditions.checkNotNull(provider);
        Preconditions.checkNotNull(oauthId);


        Account account = accountRepository.save(
                new Account()
                        .setProvider(provider)
                        .setOauthId(oauthId)
                        .setRegisteredAt(LocalDateTime.now())
                        .setDefaultLanguage(SessionLanguage.fr)
                        .setValid(false)
        );
        account.addAuthority(authorityRepository.findByName(Role.MEMBER));
        accountRepository.save(account);
        return account;

    }

    /**
     * We had'nt be able to keep the old accounts when we migrated to the new website. When a user sign in for the
     * first time on the new website, we try to link his old member infos
     */
    public Member tryToLinkWithActualMember(String email){
        return null;
    }
    /**
     * Update an account to add email
     */
    public void updateAccount(Account account, String token, String oauthId) {

        Account partial = accountRepository.findByToken(token);

        if (partial == null) {
            throw new InvalidTokenException();
        }

        partial.setValid(false)
                .setEmail(account.getEmail())
                .setFirstname(account.getFirstname())
                .setLastname(account.getLastname());

        List<Member> member = memberRepository.findByEmail(account.getEmail());

        if (!member.isEmpty()) {
            //Accout can be linked to a member but an email can't be used twice
            if (partial.getMember() == null ||
                    (partial.getMember() != null && !partial.getMember().getId().equals(member.stream().findFirst().get().getId()))) {
                throw new EmailExistException();
            }
        }

        if (partial.getMember() != null) {
            partial.getMember().setEmail(account.getEmail())
                    .setFirstname(account.getFirstname())
                    .setLastname(account.getLastname());
        }
        else {
            partial.setMember(memberRepository.save(new Member()
                    .setLogin(oauthId)
                    .setEmail(account.getEmail())
                    .setFirstname(account.getFirstname())
                    .setLastname(account.getLastname())
                    .setPublicProfile(Boolean.TRUE)));
        }

        partial.addAuthority(authorityRepository.findByName(Role.MEMBER));
        tokenService.reinitTokenValidity(partial);
        accountRepository.save(partial);

        //Step6: a mail with a token is send to the user. He has to confirm it before 3 3020151625
        mailerService.send(
                partial.getEmail(),
                "Account validation",
                mailBuilder.createHtmlMail(MailBuilder.TypeMail.SOCIAL_ACCOUNT_VALIDATION, partial, Optional.empty()));

    }

}

package org.mixit.cesar.service.user;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.google.common.base.Preconditions;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.model.session.SessionLanguage;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.repository.AuthorityRepository;
import org.mixit.cesar.repository.MemberRepository;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.mixit.cesar.service.authentification.Credentials;
import org.mixit.cesar.service.mail.MailBuilder;
import org.mixit.cesar.service.mail.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

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

    /**
     * Generate a new token
     */
    public void generateNewToken(Account account) {
        Preconditions.checkNotNull(account);
        Preconditions.checkNotNull(account.getOauthId());

        account.setToken(UUID.randomUUID().toString());
        account.setTokenExpiration(LocalDateTime.now().plus(Duration.ofHours(3)));
    }


    /**
     * Create an account linked to a social network {@link OAuthProvider}
     */
    public Account createSocialAccount(OAuthProvider provider, String oauthId) {
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
     * Create an account with user password
     */
    public Credentials createNormalAccount(Account account) {
        //Step2: Account ids are generated
        account.setProvider(OAuthProvider.CESAR);
        account.setOauthId(UUID.randomUUID().toString());
        generateNewToken(account);

        //Step3: we check if login exist
        if (accountRepository.findByLogin(account.getLogin()) != null) {
            throw new LoginExistException();
        }

        //Step4: we check if a member exist with the same email
        if (!memberRepository.findByEmail(account.getEmail()).isEmpty()) {
            throw new EmailExistException();
        }

        //Step5: a member is created but invalid
        account.setValid(false);

        Member member = memberRepository.save(new Member()
                .setLogin(account.getLogin())
                .setEmail(account.getEmail())
                .setFirstname(account.getFirstname())
                .setLastname(account.getLastname())
                .setPublicProfile(Boolean.TRUE));

        //Step6 : account is created
        account.setMember(member);
        account.addAuthority(authorityRepository.findByName(Role.MEMBER));
        accountRepository.save(account);

        //Step7: a mail with a token is send to the user. He has to confirm it before 24h
        Credentials credentials = Credentials.build(account);
        mailerService.send(
                credentials.getEmail(),
                "Account validation",
                mailBuilder.createHtmlMail(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, credentials));

        //Token is not send to the frontend because account is not validated
        credentials.setToken(null);

        return credentials;
    }


    public Credentials validateAccountAfterMailReception(String token) {
        Account account = accountRepository.findByToken(token);

        //Step1: we check the token and its validity
        if (account == null) {
            throw new InvalidTokenException();
        }
        if (LocalDateTime.now().minus(Duration.ofHours(3)).compareTo(account.getTokenExpiration()) > 0) {
            throw new ExpiredTokenException();
        }
        account.setValid(true);
        return Credentials.build(account);
    }


}

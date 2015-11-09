package org.mixit.cesar.service.account;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
import org.mixit.cesar.service.exception.EmailExistException;
import org.mixit.cesar.service.exception.ExpiredTokenException;
import org.mixit.cesar.service.exception.InvalidTokenException;
import org.mixit.cesar.service.exception.LoginExistException;
import org.mixit.cesar.service.mail.MailBuilder;
import org.mixit.cesar.service.mail.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    /**
     * Create an account with user password
     */
    public Credentials createNormalAccount(Account account) {
        //Step1: Account ids are generated
        account.setProvider(OAuthProvider.CESAR);
        account.setOauthId(UUID.randomUUID().toString());
        tokenService.generateNewToken(account);

        //Step2: we check if login exist
        if (accountRepository.findByLogin(account.getLogin()) != null) {
            throw new LoginExistException();
        }

        //Step3: we check if a member exist with the same email
        if (!memberRepository.findByEmail(account.getEmail()).isEmpty()) {
            throw new EmailExistException();
        }

        //Step4: a member is created but invalid
        account.setValid(false);

        Member member = memberRepository.save(new Member()
                .setLogin(account.getLogin())
                .setEmail(account.getEmail())
                .setFirstname(account.getFirstname())
                .setLastname(account.getLastname())
                .setPublicProfile(Boolean.TRUE));

        //Step5 : account is created
        account.setMember(member);
        account.addAuthority(authorityRepository.findByName(Role.MEMBER));
        accountRepository.save(account);

        //Step6: a mail with a token is send to the user. He has to confirm it before 3
        Credentials credentials = Credentials.build(account);
        mailerService.send(
                credentials.getEmail(),
                "Account validation",
                mailBuilder.createHtmlMail(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, credentials, Optional.empty()));

        //Token is not send to the frontend because account is not validated
        credentials.setToken(null);

        return credentials;
    }

}

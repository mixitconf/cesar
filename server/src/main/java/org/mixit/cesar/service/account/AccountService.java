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
        Objects.requireNonNull(account);
        Objects.requireNonNull(account.getOauthId());
        account.setToken(UUID.randomUUID().toString());
        reinitTokenValidity(account);
    }


    /**
     * Reinit token validity
     */
    public void reinitTokenValidity(Account account) {
        Objects.requireNonNull(account);
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
     * Update an account to add email
     */
    public Account updateSocialAccount(OAuthProvider provider, Account account) {
        //TODO
        return null;
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

        //Step7: a mail with a token is send to the user. He has to confirm it before 3
        Credentials credentials = Credentials.build(account);
        mailerService.send(
                credentials.getEmail(),
                "Account validation",
                mailBuilder.createHtmlMail(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, credentials, Optional.empty()));

        //Token is not send to the frontend because account is not validated
        credentials.setToken(null);

        return credentials;
    }


    /**
     * Send a mail with a token for a person who lost his password
     */
    public void startReinitPassword(String email) {
        //We have only one member for an email
        Optional<Member> member = memberRepository.findByEmail(email).stream().findAny();
        member.orElseThrow(EmailExistException::new);

        //The old users can have several accounts. It's not the same case for newers
        Stream<Account> accounts  = accountRepository.findByMemberId(member.get().getId()).stream();

        Optional<Account> account = accounts.filter(o -> o.getProvider().equals(OAuthProvider.CESAR)).findAny();

        //If user has a classic account we send him the mail to reinitialize his password
        if(account.isPresent()){
            generateNewToken(account.get());
            mailerService.send(
                    email,
                    "Password reinitialization",
                    mailBuilder.createHtmlMail(MailBuilder.TypeMail.REINIT_PASSWORD, Credentials.build(account.get()), Optional.of(account.get().getProvider())));
        }
        else{
            account = accounts.findAny();
            reinitTokenValidity(account.get());
            //If the user use a social network to connect to the application we don't need to send him an email
            mailerService.send(
                    email,
                    "Account validation",
                    mailBuilder.createHtmlMail(MailBuilder.TypeMail.ACCOUND_NEW_VALIDATION, Credentials.build(account.get()), Optional.of(account.get().getProvider())));
        }
    }

    public Credentials validateAccountAfterMailReception(String token) {
        Account account = checkToken(token);
        account.setValid(true);
        return Credentials.build(account);
    }

    /**
     * Chek user with token
     */
    public Account checkToken(String token) {
        Account account = accountRepository.findByToken(token);

        //Step1: we check the token and its validity
        if (account == null) {
            throw new InvalidTokenException();
        }
        //Step2: the token is only valid during 3 hours
        if (LocalDateTime.now().minus(Duration.ofHours(3)).compareTo(account.getTokenExpiration()) > 0) {
            throw new ExpiredTokenException();
        }

        return account;
    }

}

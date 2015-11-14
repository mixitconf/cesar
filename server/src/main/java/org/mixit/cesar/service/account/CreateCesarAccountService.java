package org.mixit.cesar.service.account;

import java.util.Optional;
import java.util.UUID;

import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.repository.AuthorityRepository;
import org.mixit.cesar.repository.MemberRepository;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.mixit.cesar.service.authentification.CryptoService;
import org.mixit.cesar.service.exception.EmailExistException;
import org.mixit.cesar.service.exception.LoginExistException;
import org.mixit.cesar.service.exception.UserNotFoundException;
import org.mixit.cesar.service.mail.MailBuilder;
import org.mixit.cesar.service.mail.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
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

    @Autowired
    private CryptoService cryptoService;

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
        if (!memberRepository.findByEmail(account.getEmail()).isEmpty()) {
            throw new EmailExistException();
        }

        //Step4: a member is created but invalid
        account.setValid(false);

        Member member = memberRepository.save(new Member()
                .setLogin(account.getLogin())
                .setEmail(account.getEmail())
                .setFirstname(account.getFirstname())
                .setLastname(account.getLastname()));

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
                .setFirstname(account.getFirstname());


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

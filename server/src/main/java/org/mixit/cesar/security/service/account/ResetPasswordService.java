package org.mixit.cesar.security.service.account;

import java.util.Optional;
import javax.transaction.Transactional;

import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.authentification.CryptoService;
import org.mixit.cesar.security.service.exception.EmailExistException;
import org.mixit.cesar.security.service.mail.MailBuilder;
import org.mixit.cesar.security.service.mail.MailerService;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResetPasswordService {

    @Autowired
    private MailerService mailerService;

    @Autowired
    private MailBuilder mailBuilder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    public AbsoluteUrlFactory urlFactory;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CryptoService cryptoService;


    /**
     * Send a mail with a token for a person who lost his password
     */
    public void sendMailToResetPassword(String email) {
        //We have only one member for an email
        Optional<Member> member = memberRepository.findByEmail(email).stream().findAny();
        member.orElseThrow(EmailExistException::new);

        //The old users can have several accounts. It's not the same case for newers
        Optional<Account> account = accountRepository
                .findByMemberId(member.get().getId())
                .stream()
                .filter(o -> o.getProvider().equals(OAuthProvider.CESAR))
                .findAny();

        //If user has a classic account we send him the mail to reinitialize his password
        if (account.isPresent()) {
            tokenService.generateNewToken(account.get());
            account.get().setPassword(cryptoService.generateRandomPassword());
            mailerService.send(
                    email,
                    mailBuilder.getTitle(MailBuilder.TypeMail.REINIT_PASSWORD, account.get()),
                    mailBuilder.buildContent(MailBuilder.TypeMail.REINIT_PASSWORD, account.get(), Optional.of(account.get().getProvider())));
            account.get().setPassword(cryptoService.passwordHash(account.get().getPassword()));
            accountRepository.save(account.get());
        }
        else {
            account = accountRepository
                    .findByMemberId(member.get().getId())
                    .stream()
                    .findAny();

            tokenService.reinitTokenValidity(account.orElseThrow(EmailExistException::new));
            //If the user use a social network to connect to the application we don't need to send him an email
            mailerService.send(
                    email,
                    mailBuilder.getTitle(MailBuilder.TypeMail.ACCOUND_NEW_VALIDATION, account.get()),
                    mailBuilder.buildContent(MailBuilder.TypeMail.ACCOUND_NEW_VALIDATION, account.get(), Optional.of(account.get().getProvider())));
        }
    }

}

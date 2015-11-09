package org.mixit.cesar.service.account;

import java.time.LocalDateTime;
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
import org.mixit.cesar.service.exception.LoginExistException;
import org.mixit.cesar.service.mail.MailBuilder;
import org.mixit.cesar.service.mail.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

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
        if(account.isPresent()){
            tokenService.generateNewToken(account.get());
            mailerService.send(
                    email,
                    "Password reinitialization",
                    mailBuilder.createHtmlMail(MailBuilder.TypeMail.REINIT_PASSWORD, Credentials.build(account.get()), Optional.of(account.get().getProvider())));
            accountRepository.save(account.get());
        }
        else{
            account = accountRepository
                    .findByMemberId(member.get().getId())
                    .stream()
                    .findAny();
            tokenService.reinitTokenValidity(account.get());
            //If the user use a social network to connect to the application we don't need to send him an email
            mailerService.send(
                    email,
                    "Account validation",
                    mailBuilder.createHtmlMail(MailBuilder.TypeMail.ACCOUND_NEW_VALIDATION, Credentials.build(account.get()), Optional.of(account.get().getProvider())));
        }
    }

}

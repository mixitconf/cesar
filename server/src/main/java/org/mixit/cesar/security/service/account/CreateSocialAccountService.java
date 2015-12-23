package org.mixit.cesar.security.service.account;

import java.time.LocalDateTime;
import java.util.Optional;

import com.google.common.base.Preconditions;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.repository.AuthorityRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.security.service.exception.EmailExistException;
import org.mixit.cesar.security.service.exception.InvalidTokenException;
import org.mixit.cesar.security.service.mail.MailBuilder;
import org.mixit.cesar.security.service.mail.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
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


        if (partial.getMember() != null) {
            Optional<Member> member = memberRepository.findByEmail(account.getEmail()).stream().findFirst();
            if (member.isPresent()) {
                //Accout can be linked to a member but an email can't be used twice
                if (partial.getMember() == null ||
                        (partial.getMember() != null && !partial.getMember().getId().equals(member.get().getId()))) {
                    throw new EmailExistException();
                }
            }
            partial.getMember().setEmail(account.getEmail())
                    .setFirstname(account.getFirstname())
                    .setLastname(account.getLastname());
        }
        else {
            Member member = tokenService.tryToLinkWithActualMember(partial);

            partial.setMember(member!=null ? member : memberRepository.save(new Member()
                    .setLogin(oauthId)
                    .setEmail(account.getEmail())
                    .setFirstname(account.getFirstname())
                    .setLastname(account.getLastname())
                    .setPublicProfile(Boolean.TRUE)));
        }

        tokenService.reinitTokenValidity(partial);
        accountRepository.save(partial);

        //Step6: a mail with a token is send to the user. He has to confirm it before 3 3020151625
        mailerService.send(
                partial.getEmail(),
                mailBuilder.getTitle(MailBuilder.TypeMail.SOCIAL_ACCOUNT_VALIDATION, partial),
                mailBuilder.buildContent(MailBuilder.TypeMail.SOCIAL_ACCOUNT_VALIDATION, partial, Optional.empty()));

    }

}

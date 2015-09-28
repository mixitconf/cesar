package org.mixit.cesar.security.authentification;

import org.mixit.cesar.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Authenticate a user from the database.
 */
@Component("cesarUserDetailsService")
public class CesarUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CesarUserDetailsService.class);

    @Autowired
    private AccountRepository accountRepository;


//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(final String login) {
//        log.debug("Authenticating {}", login);
//        String lowercaseLogin = login.toLowerCase();
//
//        Account account = accountRepository.findByOauthProviderAndId(OAuthProvider.CESAR, login);
//        if (account == null) {
//            throw new UsernameNotFoundException(String.format("User %s was not found in the database", lowercaseLogin));
//        }
//
//
//        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Authority authority : account.getAuthorities()) {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
//            grantedAuthorities.add(grantedAuthority);
//        }
//
//        return new User(
//                lowercaseLogin,
//                account.getPassword(),
//                grantedAuthorities);
//    }
}

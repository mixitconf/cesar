package org.mixit.cesar.repository;

import org.mixit.cesar.model.member.SharedLink;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.model.security.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link SharedLink}
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query(value = "SELECT u FROM Account u where u.provider=:provider and u.oauthId=:oauthId")
    Account findByOauthProviderAndId(@Param("provider") OAuthProvider provider, @Param("oauthId") String oauthId);

    @Query(value = "SELECT u FROM Account u left join fetch u.authorities aut left join fetch u.member m where u.token=:token")
    Account findByToken(@Param("token") String  token);

    @Query(value = "SELECT u FROM Account u left join fetch u.authorities aut left join fetch u.member m where u.login=:login")
    Account findByLogin(@Param("login") String  login);
}

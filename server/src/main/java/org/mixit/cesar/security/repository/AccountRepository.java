package org.mixit.cesar.security.repository;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_ARTICLE;
import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_SECURITY;

import java.util.List;

import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.OAuthProvider;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Account}
 */
public interface AccountRepository extends CrudRepository<Account, Long> {


    List<Account> findByEmail(String email);

    @Cacheable(CACHE_SECURITY)
    @Query(value = "SELECT u FROM Account u left join fetch u.member m")
    List<Account> findAll();

    @Query(value = "SELECT u FROM Account u where u.member.id=:idMember")
    Account findByMember(@Param("idMember") Long idMember);

    @Query(value = "SELECT u FROM Account u where u.provider=:provider and u.oauthId=:oauthId")
    Account findByOauthProviderAndId(@Param("provider") OAuthProvider provider, @Param("oauthId") String oauthId);

    @Query(value = "SELECT u FROM Account u left join fetch u.authorities aut left join fetch u.member m where u.token=:token")
    Account findByToken(@Param("token") String  token);

    @Query(value = "SELECT u FROM Account u left join fetch u.authorities aut left join fetch u.member m where u.login=:login")
    Account findByLogin(@Param("login") String  login);

    @Query(value = "SELECT u FROM Account u left join fetch u.authorities aut left join fetch u.member m where m.id=:id")
    List<Account> findByMemberId(@Param("id") Long  id);

    @Query(value = "SELECT u FROM Account u inner join u.member m where m.logoUrl IS NULL")
    List<Account> findPotentialSpeakers();
}

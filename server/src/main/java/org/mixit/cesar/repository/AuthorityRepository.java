package org.mixit.cesar.repository;

import static org.mixit.cesar.config.CesarCacheConfig.CACHE_SECURITY;

import org.mixit.cesar.config.CesarCacheConfig;
import org.mixit.cesar.model.member.SharedLink;
import org.mixit.cesar.model.security.Authority;
import org.mixit.cesar.model.security.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link SharedLink}
 */
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    @Cacheable(CACHE_SECURITY)
    Authority findByName(Role name);
}

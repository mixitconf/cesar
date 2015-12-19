package org.mixit.cesar.security.repository;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_SECURITY;

import org.mixit.cesar.security.model.Authority;
import org.mixit.cesar.security.model.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link Authority}
 */
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByName(Role name);
}

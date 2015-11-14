package org.mixit.cesar.repository;

import static org.mixit.cesar.config.CesarCacheConfig.CACHE_SESSION;

import org.mixit.cesar.model.member.Interest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link Interest}
 */
public interface InterestRepository extends CrudRepository<Interest, Long> {
    @Cacheable(CACHE_SESSION)
    Interest findByName(String name);
}

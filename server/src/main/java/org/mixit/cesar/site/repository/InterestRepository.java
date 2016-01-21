package org.mixit.cesar.site.repository;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_SESSION;

import org.mixit.cesar.site.model.member.Interest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link Interest}
 */
public interface InterestRepository extends CrudRepository<Interest, Long> {

    Interest findByName(String name);

    @Cacheable(CACHE_SESSION)
    @Override
    Iterable<Interest> findAll();
}

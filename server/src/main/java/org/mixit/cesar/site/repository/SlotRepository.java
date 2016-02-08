package org.mixit.cesar.site.repository;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_PLANNING;

import java.util.List;

import org.mixit.cesar.site.config.CesarCacheConfig;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.model.session.Session;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Slot}
 */
public interface SlotRepository extends CrudRepository<Slot, Long> {

    @Cacheable(CACHE_PLANNING)
    @Query(value = "SELECT DISTINCT slot FROM Slot slot left join fetch slot.session s where s.event.id = :idEvent and s.valid = true ")
    List<Slot> findAllSlots(@Param("idEvent") Long idEvent);

}

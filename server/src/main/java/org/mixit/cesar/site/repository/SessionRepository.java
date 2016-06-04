package org.mixit.cesar.site.repository;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_LIGHTNINGTALK;
import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_RANKING;
import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_SESSION;

import java.util.List;

import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.session.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Session}
 */
public interface SessionRepository extends CrudRepository<Session, Long> {

    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes left join fetch s.slot where i.name = :interest and s.valid = true ")
    List<Session> findAllSessionsByInterest(@Param("interest") String interest);

    @Cacheable(CACHE_RANKING)
    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes left join fetch s.slot where s.event = :event and s.valid = true ")
    List<Session> findAllSessionsForRanking(@Param("event") Event event);

    @Cacheable(CACHE_SESSION)
    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes left join fetch s.slot where s.event = :event and s.valid = true and s.sessionAccepted = true and s.format <> 'LightningTalk'")
    List<Session> findAllAcceptedSessions(@Param("event") Event event);

    @Cacheable(CACHE_SESSION)
    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes left join fetch s.slot where s.event = :event and s.valid = true and s.sessionAccepted = true and s.format = 'Keynote'")
    List<Session> findAllAcceptedKeynotes(@Param("event") Event event);

    @Cacheable(CACHE_SESSION)
    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes left join fetch s.slot where s.event = :event and s.valid = true and s.sessionAccepted = true and s.format = 'Talk'")
    List<Session> findAllAcceptedTalks(@Param("event") Event event);

    @Cacheable(CACHE_SESSION)
    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes left join fetch s.slot where s.event = :event and s.valid = true and s.sessionAccepted = true and s.format = 'Workshop'")
    List<Session> findAllAcceptedWorkshops(@Param("event") Event event);

    @Cacheable(CACHE_LIGHTNINGTALK)
    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes where s.event = :event and s.valid = true and s.format = 'LightningTalk'")
    List<Session> findAllLightningTalks(@Param("event") Event event);

    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes where s.event = :event and s.valid = true and sp.id = :idSpeaker and s.format = 'LightningTalk'")
    List<Session> findAllMyLightningTalks(@Param("event") Event event, @Param("idSpeaker") Long idSpeaker);
}

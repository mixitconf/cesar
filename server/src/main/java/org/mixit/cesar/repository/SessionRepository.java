package org.mixit.cesar.repository;

import java.util.List;

import org.mixit.cesar.model.session.Keynote;
import org.mixit.cesar.model.session.LightningTalk;
import org.mixit.cesar.model.session.Session;
import org.mixit.cesar.model.session.Talk;
import org.mixit.cesar.model.session.Workshop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Session}
 */
public interface SessionRepository extends CrudRepository<Session, Long> {

    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' ")
    List<Session> findAllSessions(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Keynote s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' ")
    List<Keynote> findAllKeynotes(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Talk s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' ")
    List<Talk> findAllTalks(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Workshop s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' ")
    List<Workshop> findAllWorkshops(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM LightningTalk s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' ")
    List<LightningTalk> findAllLightningTalks(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Session s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' and s.sessionAccepted = 'true'")
    List<Session> findAllAcceptedSessions(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Keynote s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' and s.sessionAccepted = 'true'")
    List<Keynote> findAllAcceptedKeynotes(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Talk s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' and s.sessionAccepted = 'true'")
    List<Talk> findAllAcceptedTalks(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Workshop s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' and s.sessionAccepted = 'true'")
    List<Workshop> findAllAcceptedWorkshops(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM LightningTalk s left join fetch s.interests i left join fetch s.speakers sp left join fetch s.votes  where s.event.id = :idEvent and s.valid = 'true' and s.sessionAccepted = 'true'")
    List<LightningTalk> findAllAcceptedLightningTalks(@Param("idEvent") Long idEvent);
}

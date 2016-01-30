package org.mixit.cesar.site.repository;

import java.util.List;

import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.model.session.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Slot}
 */
public interface SlotRepository extends CrudRepository<Slot, Long> {

    @Query(value = "SELECT DISTINCT slot FROM Slot slot left join fetch slot.session s where s.event.id = :idEvent and s.valid = true ")
    List<Session> findAllSessions(@Param("idEvent") Long idEvent);

}

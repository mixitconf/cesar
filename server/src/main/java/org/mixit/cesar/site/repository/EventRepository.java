package org.mixit.cesar.site.repository;

import java.util.List;

import org.mixit.cesar.site.model.event.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link Event}
 */
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findByCurrent(boolean current);

    Event findByYear(int year);

    @Query(value = "SELECT max(e.year) FROM Event e")
    Integer findLatestYearEdition();

    @Query(value = "SELECT min(e.year) FROM Event e")
    Integer findFirstYearEdition();
}

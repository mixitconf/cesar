package org.mixit.cesar.cfp.repository;

import java.util.List;

import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.site.model.session.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Proposal}
 */
public interface ProposalRepository extends CrudRepository<Proposal, Long> {

    /**
     * Return the proposal for an event
     */
    @Query(value = "SELECT DISTINCT s FROM Proposal s left join fetch s.interests i left join fetch s.speakers sp where s.event.id = :idEvent")
    List<Proposal> findAllProposals(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Proposal s left join fetch s.speakers sp left join fetch s.votes where s.event.id = :idEvent")
    List<Proposal> findAllProposalsWithVotes(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT s FROM Proposal s left join fetch s.interests i left join fetch s.speakers sp where s.event.id = :idEvent and sp.id = :idSpeaker")
    List<Proposal> findMyProposals(@Param("idEvent") Long idEvent, @Param("idSpeaker") Long idSpeaker);
}

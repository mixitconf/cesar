package org.mixit.cesar.cfp.repository;

import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.cfp.model.ProposalVote;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.member.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * {@link Proposal}
 */
public interface ProposalVoteRepository extends CrudRepository<ProposalVote, Long> {

    /**
     * Return the proposal vote
     */
    @Query(value = "SELECT DISTINCT pv FROM ProposalVote pv where pv.voter = :voter and pv.proposal = :proposal")
    List<ProposalVote> findProposalVote(@Param("voter") Staff voter, @Param("proposal") Proposal proposal);

    /**
     * Return the proposal for an event
     */
    @Query(value = "SELECT DISTINCT pv FROM ProposalVote pv where pv.proposal.event = :event and pv.voter = :voter")
    List<ProposalVote> findStaffVotesForEvent(@Param("voter") Staff voter, @Param("event") Event event);
}

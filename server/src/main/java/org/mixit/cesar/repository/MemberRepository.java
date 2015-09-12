package org.mixit.cesar.repository;

import java.util.List;

import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.member.Sponsor;
import org.mixit.cesar.model.member.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Member}
 */
public interface MemberRepository extends CrudRepository<Member, Long> {

    @Query(value = "SELECT DISTINCT m FROM Member m left join fetch m.interests i left join fetch m.sharedLinks l")
    List<Member> findAllMembers();

    @Query(value = "SELECT DISTINCT m FROM Staff m left join fetch m.interests i left join fetch m.sharedLinks l")
    List<Staff> findAllStaffs();


    @Query(value = "SELECT DISTINCT m FROM Member m left join fetch m.sessions s  left join fetch m.events e left join fetch m.interests i left join fetch m.sharedLinks l where e.id = :idEvent")
    List<Member> findAllSpeakers(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT m FROM Member m left join fetch m.sessions s left join fetch m.events e left join fetch m.interests i left join fetch m.sharedLinks l where e.id = :idEvent and s.sessionAccepted = true")
    List<Member> findAllAcceptedSpeakers(@Param("idEvent") Long idEvent);

    @Query(value = "SELECT DISTINCT m FROM Sponsor m left join fetch m.events e left join fetch m.interests i left join fetch m.sharedLinks l where e.id = :idEvent")
    List<Sponsor> findAllSponsors(@Param("idEvent") Long idEvent);


}

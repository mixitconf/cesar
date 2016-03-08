package org.mixit.cesar.site.repository;

import java.util.List;

import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.model.session.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * {@link Vote}
 */
public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query(value = "SELECT m FROM Vote m where m.session.id=:idSession and m.member.id=:idMember")
    Vote findBySessionAndMember(@Param("idSession") Long idSession, @Param("idMember") Long idMember);

    List<Vote> findByMember(Member member);
}

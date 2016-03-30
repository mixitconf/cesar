package org.mixit.cesar.site.repository;

import java.util.List;

import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.session.Favorite;
import org.mixit.cesar.site.model.session.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link Favorite}
 */
public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    List<Favorite> findByMember(Member member);

    @Query(value = "SELECT DISTINCT f FROM Favorite f where f.member = :member and f.session = :session")
    Favorite findByMemberAndSession(@Param("member") Member member, @Param("session") Session session);
}

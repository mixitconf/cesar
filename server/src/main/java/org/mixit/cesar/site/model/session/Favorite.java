package org.mixit.cesar.site.model.session;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.member.Member;

/**
 * Users (with account) can mark session like favorite
 */
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    @ManyToOne
    private Session session;

    @ManyToOne
    private Member member;

    public Long getId() {
        return id;
    }

    public Favorite setId(Long id) {
        this.id = id;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public Favorite setSession(Session session) {
        this.session = session;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public Favorite setMember(Member member) {
        this.member = member;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(session, favorite.session) &&
                Objects.equals(member, favorite.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, member);
    }
}

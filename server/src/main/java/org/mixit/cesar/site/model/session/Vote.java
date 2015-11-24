package org.mixit.cesar.site.model.session;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.mixit.cesar.site.model.member.Member;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    public Session session;

    @ManyToOne
    public Member member;

    /**
     * true if the vote is positive
     */
    @NotNull
    public Boolean value;

    public Long getId() {
        return id;
    }

    public Vote setId(Long id) {
        this.id = id;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public Vote setSession(Session session) {
        this.session = session;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public Vote setMember(Member member) {
        this.member = member;
        return this;
    }

    public Boolean getValue() {
        return value;
    }

    public Vote setValue(Boolean value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(session, vote.session) &&
                Objects.equals(member, vote.member) &&
                Objects.equals(value, vote.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, member, value);
    }
}

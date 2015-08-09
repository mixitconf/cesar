package org.mixit.cesar.model.session;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.mixit.cesar.model.member.Member;

/**
 * A comment on a session talk.
 */
@Entity
public class SessionComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    public Member member;

    @ManyToOne(optional = false)
    public Session session;

    /**
     * Markdown enabled
     */
    @Lob
    @NotNull
    public String content;

    @NotNull
    public Instant postedAt = Instant.now();

    /* true if private comment. A private comment is visible only to admin members and owners of commented entity (like speakers of a Session). */
    @Column(name = "private")
    public boolean privatelyVisible;


    public Long getId() {
        return id;
    }

    public SessionComment setId(Long id) {
        this.id = id;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public SessionComment setMember(Member member) {
        this.member = member;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public SessionComment setSession(Session session) {
        this.session = session;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SessionComment setContent(String content) {
        this.content = content;
        return this;
    }

    public Instant getPostedAt() {
        return postedAt;
    }

    public SessionComment setPostedAt(Instant postedAt) {
        this.postedAt = postedAt;
        return this;
    }

    public boolean isPrivatelyVisible() {
        return privatelyVisible;
    }

    public SessionComment setPrivatelyVisible(boolean privatelyVisible) {
        this.privatelyVisible = privatelyVisible;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionComment that = (SessionComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SessionComment{" +
                "id=" + id +
                ", member=" + member +
                ", session=" + session +
                '}';
    }
}

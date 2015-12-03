package org.mixit.cesar.cfp.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.mixit.cesar.site.model.member.Member;

/**
 * A comment on a proposal talk.
 */
@Entity
public class ProposalComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    public Member member;

    @ManyToOne(optional = false)
    public Proposal proposal;

    /**
     * Markdown enabled
     */
    @Lob
    @NotNull
    public String content;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    public LocalDateTime postedAt = LocalDateTime.now();

    /* true if private comment. A private comment is visible only to admin members and owners of commented entity (like speakers of a Session). */
    @Column(name = "private")
    public boolean privatelyVisible;


    public Long getId() {
        return id;
    }

    public ProposalComment setId(Long id) {
        this.id = id;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public ProposalComment setMember(Member member) {
        this.member = member;
        return this;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public ProposalComment setProposal(Proposal proposal) {
        this.proposal = proposal;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ProposalComment setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public ProposalComment setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
        return this;
    }

    public boolean isPrivatelyVisible() {
        return privatelyVisible;
    }

    public ProposalComment setPrivatelyVisible(boolean privatelyVisible) {
        this.privatelyVisible = privatelyVisible;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalComment that = (ProposalComment) o;
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
                ", proposal=" + proposal +
                '}';
    }
}

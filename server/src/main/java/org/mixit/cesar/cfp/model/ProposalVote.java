package org.mixit.cesar.cfp.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.member.Staff;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"proposal_id", "voter_id"})})
public class ProposalVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    @Min(-2)
    @Max(2)
    @JsonView(FlatView.class)
    protected int voteValue;

    @ManyToOne(optional = false)
    @JsonView(FlatView.class)
    protected Member voter;

    @ManyToOne(optional = false)
    @JsonView(FlatView.class)
    protected Proposal proposal;

    @Lob
    @JsonView(FlatView.class)
    private String voteComment;

    public Long getId() {
        return id;
    }

    public ProposalVote setId(Long id) {
        this.id = id;
        return this;
    }

    public int getVoteValue() {
        return voteValue;
    }

    public ProposalVote setVoteValue(int voteValue) {
        this.voteValue = voteValue;
        return this;
    }

    public Member getVoter() {
        return voter;
    }

    public ProposalVote setVoter(Member voter) {
        this.voter = voter;
        return        this;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public ProposalVote setProposal(Proposal proposal) {
        this.proposal = proposal;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalVote vote = (ProposalVote) o;
        return Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getVoteComment() {
        return voteComment;
    }

    public ProposalVote setVoteComment(String voteComment) {
        this.voteComment = voteComment;
        return this;
    }
}

package org.mixit.cesar.cfp.web.dto;

import org.mixit.cesar.cfp.model.ProposalVote;

/**
 * @author mpetitdant
 *         Date: 10/02/16
 */
public class ProposalVoteDTO {
    private  Long proposalId;
    private  int voteValue;
    private String voteComment;
    private String voter;

    public ProposalVoteDTO() {

    }

    public static ProposalVoteDTO convert(ProposalVote proposalVote) {
        return new ProposalVoteDTO()
                .setProposalId(proposalVote.getProposal().getId())
                .setVoteComment(proposalVote.getVoteComment())
                .setVoteValue(proposalVote.getVoteValue())
                .setVoter(proposalVote.getVoter().getFirstname());
    }

    public Long getProposalId() {
        return proposalId;
    }

    public int getVoteValue() {
        return voteValue;
    }

    public ProposalVoteDTO setProposalId(Long proposalId) {
        this.proposalId = proposalId;
        return this;
    }

    public ProposalVoteDTO setVoteValue(int voteValue) {
        this.voteValue = voteValue;
        return this;
    }

    public String getVoteComment() {
        return voteComment;
    }

    public ProposalVoteDTO setVoteComment(String voteComment) {
        this.voteComment = voteComment;
        return this;
    }

    public String getVoter() {
        return voter;
    }

    public ProposalVoteDTO setVoter(String voter) {
        this.voter = voter;
        return this;
    }
}

package org.mixit.cesar.cfp.web.dto;

import org.mixit.cesar.cfp.model.ProposalVote;

/**
 * @author mpetitdant
 *         Date: 10/02/16
 */
public class ProposalVoteDTO {
    private  Long proposalId;
    private  int voteValue;

    public ProposalVoteDTO() {

    }

    public ProposalVoteDTO(ProposalVote proposalVote) {
        this.proposalId = proposalVote.getProposal().getId();
        this.voteValue = proposalVote.getVoteValue();
    }

    public Long getProposalId() {
        return proposalId;
    }

    public int getVoteValue() {
        return voteValue;
    }

    public void setProposalId(Long proposalId) {
        this.proposalId = proposalId;
    }

    public void setVoteValue(int voteValue) {
        this.voteValue = voteValue;
    }
}

package org.mixit.cesar.cfp.model;

public class ProposalVoteParam {


    private Long proposalId;

    private ProposalVoteValue voteValue;

    public Long getProposalId() {
        return proposalId;
    }

    public void setProposalId(Long proposalId) {
        this.proposalId = proposalId;
    }

    public ProposalVoteValue getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(ProposalVoteValue voteValue) {
        this.voteValue = voteValue;
    }
}

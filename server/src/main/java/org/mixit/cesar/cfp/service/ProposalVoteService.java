package org.mixit.cesar.cfp.service;

import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.cfp.model.ProposalVote;
import org.mixit.cesar.cfp.repository.ProposalVoteRepository;
import org.mixit.cesar.site.model.member.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProposalVoteService {

    @Autowired
    private ProposalVoteRepository proposalVoteRepository;

    public void vote(Proposal proposal, Staff voter, int voteValue) {
        Objects.requireNonNull(proposal, "proposal is required");
        Objects.requireNonNull(voter, "voter is required");
        Objects.requireNonNull(voteValue, "vote value is required");

        ProposalVote proposalVote;

        // Expecting empty list or singleton
        List<ProposalVote> proposalPersisted = proposalVoteRepository.findProposalVote(voter, proposal);

        if (proposalPersisted.size() == 0) {
            proposalVote = new ProposalVote()
                    .setVoter(voter)
                    .setProposal(proposal)
                    .setVoteValue(voteValue);
        }else {
            proposalVote = proposalPersisted.get(0);
            proposalVote.setVoteValue(voteValue);
        }

        proposalVoteRepository.save(proposalVote);
    }

    public void voteComment(Proposal proposal, Staff voter, int voteValue, String voteComment) {
        Objects.requireNonNull(proposal, "proposal is required");
        Objects.requireNonNull(voter, "voter is required");
        Objects.requireNonNull(voteComment, "vote comment is required");

        ProposalVote proposalVote;

        // Expecting empty list or singleton
        List<ProposalVote> proposalPersisted = proposalVoteRepository.findProposalVote(voter, proposal);

        if (proposalPersisted.size() == 0) {
            proposalVote = new ProposalVote()
                    .setVoter(voter)
                    .setProposal(proposal)
                    .setVoteValue(voteValue)
                    .setVoteComment(voteComment);
        }else {
            proposalVote = proposalPersisted.get(0);
            proposalVote.setVoteComment(voteComment).setVoteValue(voteValue);
        }

        proposalVoteRepository.save(proposalVote);
    }


}

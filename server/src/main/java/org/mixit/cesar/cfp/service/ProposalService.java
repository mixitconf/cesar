package org.mixit.cesar.cfp.service;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.cfp.model.ProposalError;
import org.mixit.cesar.cfp.model.ProposalStatus;
import org.mixit.cesar.site.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProposalService {

    @Autowired
    private MemberService memberService;

    /**
     * Check the required fields. A user can save a partial proposal and complete it later
     */
    public Set<ProposalError> check(Proposal proposal){
        Set<ProposalError> errors = Sets.newHashSet();

        //To be valid the profile of the speakers have to be valid
        if(proposal.getSpeakers().isEmpty()){
            errors.add(new ProposalError());
        }
        return errors;
    }

    public ProposalStatus computeProposalState(Proposal proposal){
        Set<ProposalError> errors = check(proposal);
        proposal.getSpeakers().forEach(speaker -> errors.addAll(memberService.checkSpeakerData(speaker)));

      //  if(errors.isEmpty())
    }

    public void save(){

    }
}

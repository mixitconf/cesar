package org.mixit.cesar.cfp.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.cfp.model.ProposalStatus;
import org.mixit.cesar.cfp.model.ProposalVote;
import org.mixit.cesar.cfp.repository.ProposalRepository;
import org.mixit.cesar.cfp.repository.ProposalVoteRepository;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.member.Staff;
import org.mixit.cesar.site.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 */
@RestController
@RequestMapping("/app/cfp/vote")
public class ProposalVoteController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ProposalVoteRepository proposalVoteRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Authenticated
    @RequestMapping(value = "/nbsessions")
    public long getNbSessions(@RequestParam(required = false) Integer year) {
        return proposalRepository.findAllProposals(eventService.getEvent(year).getId())
                .stream()
                .filter(p -> p.getStatus().equals(ProposalStatus.SUBMITTED))
                .count();
    }

    @Authenticated
    @RequestMapping
    @JsonView(FlatView.class)
    public Map<String, Long> getAllByEvent(@RequestParam(required = false) Integer year) {
        List<ProposalVote> votes = proposalVoteRepository.findAllByEvent(eventService.getEvent(year));

        return votes
                .stream()
                .collect(Collectors.groupingBy(v -> v.getVoter().getFirstname(), Collectors.counting()));
    }
}

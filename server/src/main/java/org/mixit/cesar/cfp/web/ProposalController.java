package org.mixit.cesar.cfp.web;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.cfp.model.ProposalError;
import org.mixit.cesar.cfp.model.ProposalStatus;
import org.mixit.cesar.cfp.repository.ProposalRepository;
import org.mixit.cesar.cfp.repository.ProposalVoteRepository;
import org.mixit.cesar.cfp.service.ProposalService;
import org.mixit.cesar.cfp.service.ProposalVoteService;
import org.mixit.cesar.cfp.web.dto.ProposalVoteDTO;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.security.service.autorisation.NeedsRole;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.Tuple;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller used to return the parameters used in the CFP
 */
@RestController
@RequestMapping("/app/cfp/proposal")
@Transactional
public class ProposalController {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    ProposalService proposalService;

    @Autowired
    ProposalVoteService proposalVoteService;

    @Autowired
    ProposalVoteRepository proposalVoteRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping()
    @ResponseStatus(HttpStatus.OK)
    @JsonView(FlatView.class)
    @NeedsRole(Role.ADMIN)
    public List<Proposal> allProposals(@RequestParam(required = false) Integer year) {
        return proposalRepository.findAllProposals(eventService.getEvent(year).getId());
    }

    @RequestMapping("/mine")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(FlatView.class)
    @Authenticated
    public List<Proposal> myProposal(@RequestParam(required = false) Integer year) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        return proposalRepository.findMyProposals(eventService.getEvent(year).getId(), currentUser.getCredentials().get().getMember().getId());
    }

    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(FlatView.class)
    public Proposal proposal(@PathVariable(value = "id") Long id) {
        return proposalRepository.findOne(id);
    }

    /**
     * When we create a new user we want to know if a login is already used. This method checks the login
     */
    @RequestMapping(value = "/speakers")
    @ResponseStatus(HttpStatus.OK)
    public List<Tuple> speakers() {

        List<Account> accounts = accountRepository.findPotentialSpeakers();

        //We can have several account for the same person
        return
                accounts
                        .stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getFirstname().concat(" ").concat(a.getLastname()),
                                Collectors.mapping(a -> a.getMember().getId(), Collectors.toList())
                        ))
                        .entrySet()
                        .stream()
                        .map(a -> new Tuple().setKey(a.getKey()).setValue(a.getValue().stream().findFirst().get()))
                        .collect(Collectors.toList());
    }


    @RequestMapping(method = RequestMethod.POST)
    @Authenticated
    @JsonView(FlatView.class)
    public Proposal save(@RequestBody Proposal proposal) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        return proposalService.save(proposal, currentUser.getCredentials().get());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/check")
    @Authenticated
    public Set<ProposalError> check(@RequestBody Proposal proposal) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        if(proposal.getSpeakers().isEmpty()){
            proposal.addSpeaker(accountRepository.findOne(currentUser.getCredentials().get().getId()).getMember());
        }
        proposalService.updateProposalSpeaker(proposal, proposal, currentUser.getCredentials().get());
        return proposalService.check(proposal);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {
        proposalService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/vote")
    @ResponseStatus(HttpStatus.OK)
    @Authenticated
    @NeedsRole(Role.ADMIN)
    public void vote(@RequestBody ProposalVoteDTO voteParam) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        Proposal proposal = proposalRepository.findOne(voteParam.getProposalId());
        proposalVoteService.vote(proposal, currentUser.getCredentials().get().getMember(), voteParam.getVoteValue());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/vote-comment")
    @ResponseStatus(HttpStatus.OK)
    @Authenticated
    @NeedsRole(Role.ADMIN)
    public void voteComment(@RequestBody ProposalVoteDTO voteParam) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        Proposal proposal = proposalRepository.findOne(voteParam.getProposalId());
        proposalVoteService.voteComment(proposal, currentUser.getCredentials().get().getMember(), voteParam.getVoteValue(), voteParam.getVoteComment());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/votes")
    @ResponseStatus(HttpStatus.OK)
    @Authenticated
    @NeedsRole(Role.ADMIN)
    public List<ProposalVoteDTO> getVotes(@RequestParam(required = false) Integer year) {
        Event event = eventService.getEvent(year);
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        Member voter = currentUser.getCredentials().get().getMember();
        return proposalVoteRepository.findStaffVotesForEvent(voter, event)
                .stream()
                .map(ProposalVoteDTO::convert)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{proposalId}/state/{state}")
    @ResponseStatus(HttpStatus.OK)
    @Authenticated
    @NeedsRole(Role.ADMIN)
    public void changeState(@PathVariable(value = "proposalId")  Long proposalId,
                       @PathVariable(value = "state")  ProposalStatus state) {
        Proposal proposal = proposalRepository.findOne(proposalId);
        proposal.setStatus(state);

        if(state == ProposalStatus.REJECTED && proposal.getSession()!=null){
            proposal.getSession().setSessionAccepted(false);
        }
        else if (state == ProposalStatus.ACCEPTED && proposal.getSession()!=null){
            proposal.getSession().setSessionAccepted(true);
        }
    }
}
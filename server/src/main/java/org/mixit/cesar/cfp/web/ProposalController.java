package org.mixit.cesar.cfp.web;

import java.util.List;

import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.cfp.repository.ProposalRepository;
import org.mixit.cesar.cfp.service.ProposalService;
import org.mixit.cesar.site.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to return the parameters used in the CFP
 */
@RestController
@RequestMapping("/app/cfp/proposal")
public class ProposalController {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    ProposalService proposalService;

    @Autowired
    private EventService eventService;

    @RequestMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Proposal> categories(@RequestParam(required = false) Integer year) {
        return proposalRepository.findAllProposals(eventService.getEvent(year).getId());
    }

    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Proposal category(@PathVariable(value = "id") Long id) {
        return proposalRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Proposal> save(@RequestBody Proposal proposal) {
        return ResponseEntity.ok().body(proposalService.save(proposal));
    }
}
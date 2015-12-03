package org.mixit.cesar.cfp.web;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mixit.cesar.cfp.model.ProposalCategory;
import org.mixit.cesar.cfp.model.ProposalStatus;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.site.model.Tuple;
import org.mixit.cesar.site.model.session.Format;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to return the parameters used in the CFP
 */
@RestController
@RequestMapping("/cfp/param")
public class ProposalParamController {

    @RequestMapping(value = "/category")
    @ResponseStatus(HttpStatus.OK)
    public Set<ProposalCategory> categories() {
        return Stream.of(ProposalCategory.values()).collect(Collectors.toSet());
    }

    @RequestMapping(value = "/status")
    @ResponseStatus(HttpStatus.OK)
    public Set<ProposalStatus> status() {
        return Stream.of(ProposalStatus.values()).collect(Collectors.toSet());
    }

    @RequestMapping(value = "/format")
    @ResponseStatus(HttpStatus.OK)
    public Set<Format> formats() {
        return Stream.of(Format.values()).collect(Collectors.toSet());
    }

}

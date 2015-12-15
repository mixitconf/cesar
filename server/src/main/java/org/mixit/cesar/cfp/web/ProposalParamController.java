package org.mixit.cesar.cfp.web;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mixit.cesar.cfp.model.ProposalCategory;
import org.mixit.cesar.cfp.model.ProposalStatus;
import org.mixit.cesar.site.model.session.Format;
import org.springframework.http.HttpStatus;
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
    public List<ProposalCategory> categories() {
        return Stream.of(ProposalCategory.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/status")
    @ResponseStatus(HttpStatus.OK)
    public List<ProposalStatus> status() {
        return Stream.of(ProposalStatus.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/format")
    @ResponseStatus(HttpStatus.OK)
    public List<Format> formats() {
        return Stream.of(Format.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList());
    }

}
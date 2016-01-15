package org.mixit.cesar.cfp.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mixit.cesar.cfp.model.ProposalCategory;
import org.mixit.cesar.cfp.model.ProposalNbAttendees;
import org.mixit.cesar.cfp.model.ProposalStatus;
import org.mixit.cesar.cfp.model.ProposalTypeSession;
import org.mixit.cesar.site.model.Tuple;
import org.mixit.cesar.site.model.session.Format;
import org.mixit.cesar.site.model.session.Level;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to return the parameters used in the CFP
 */
@RestController
@RequestMapping("/app/cfp/param")
public class ProposalParamController {

    @RequestMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Tuple> params() {
        return Arrays.asList(
                new Tuple().setKey("categories").setValue(Stream.of(ProposalCategory.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList())),
                new Tuple().setKey("status").setValue(Stream.of(ProposalStatus.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList())),
                new Tuple().setKey("formats").setValue(Stream.of(Format.values()).filter(f -> !Format.Keynote.equals(f)&&!Format.LightningTalk.equals(f)).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList())),
                new Tuple().setKey("types").setValue(Stream.of(ProposalTypeSession.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList())),
                new Tuple().setKey("levels").setValue(Stream.of(Level.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList())),
                new Tuple().setKey("maxAttendees").setValue(Stream.of(ProposalNbAttendees.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList()))
        );
    }

    @RequestMapping(value = "/category")
    @ResponseStatus(HttpStatus.OK)
    public List<ProposalCategory> categories() {
        return Stream.of(ProposalCategory.values()).sorted((a, b) -> a.toString().compareTo(b.toString())).collect(Collectors.toList());
    }
}

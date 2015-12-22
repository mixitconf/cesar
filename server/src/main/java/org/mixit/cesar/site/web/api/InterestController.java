package org.mixit.cesar.site.web.api;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 19/12/15.
 */
@Api(value = "Interests",
        description = "Helper to find interests")
@RestController
@RequestMapping("/api/interest")
public class InterestController {

    @Autowired
    private InterestRepository interestRepository;

    @RequestMapping("/{prefix}")
    @ApiOperation(value = "Return interests starting with param", httpMethod = "GET")
    public List<String> getInterests(@PathVariable("prefix") String prefix){
        return StreamSupport
                .stream(interestRepository.findAll().spliterator(), true)
                .map(Interest::getName)
                .filter(n -> n.toUpperCase().startsWith(prefix.toUpperCase()))
                .collect(Collectors.toList());

    }

    @RequestMapping
    @ApiOperation(value = "Return all interests", httpMethod = "GET")
    public List<String> getInterests(){
        return StreamSupport
                .stream(interestRepository.findAll().spliterator(), true)
                .map(Interest::getName)
                .collect(Collectors.toList());

    }
}

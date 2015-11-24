package org.mixit.cesar.site.web.api;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.service.EventService;
import org.mixit.cesar.site.web.api.dto.MemberResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Conference members",
        description = "Member Api helps you to find all the persons linked with the conference. Members have an account " +
                "on our website, participants have a ticket for the conference. You can also find informations about the " +
                "speakers, the staff and the sponsors")
@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventService eventService;

    private <T extends Member<T>> ResponseEntity<List<MemberResource>> getAllMembers(List<T> members) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(4, TimeUnit.DAYS))
                .body(members
                        .stream()
                        .map(m -> MemberResource.convert(m))
                        .collect(Collectors.toList()));
    }

    @RequestMapping("/{id}")
    @ApiOperation(value = "Finds one member", httpMethod = "GET")
    public ResponseEntity<MemberResource> getMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findOne(id);
        member.setNbConsults(member.getNbConsults() + 1);
        memberRepository.save(member);
        return ResponseEntity
                .ok()
                .body(MemberResource.convert(member));
    }

    @RequestMapping
    @ApiOperation(value = "Finds all members", httpMethod = "GET")
    public ResponseEntity<List<MemberResource>> getAllMembers() {
        return getAllMembers(memberRepository.findAllMembers());
    }

    @RequestMapping(value = "/staff")
    @ApiOperation(value = "Finds Mix-IT staff", httpMethod = "GET")
    public ResponseEntity<List<MemberResource>> getAllStaffs() {
        return getAllMembers(memberRepository.findAllStaffs());
    }

    @RequestMapping(value = "/speaker")
    @ApiOperation(value = "Finds all speakers", httpMethod = "GET")
    public ResponseEntity<List<MemberResource>> getAllSpeakers(@RequestParam(required = false) Integer year) {
        return getAllMembers(memberRepository.findAllAcceptedSpeakers(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/speaker/lightningtalks")
    @ApiOperation(value = "Finds all lightningtalk speakers", httpMethod = "GET")
    public ResponseEntity<List<MemberResource>> getAllLightningtalksSpeakers(@RequestParam(required = false) Integer year) {
        return getAllMembers(memberRepository.findAllLigthningtalkSpeakers(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/sponsor")
    @ApiOperation(value = "Finds all sponsors", httpMethod = "GET")
    public ResponseEntity<List<MemberResource>> getAllSponsors(@RequestParam(required = false) Integer year) {
        return getAllMembers(memberRepository.findAllSponsors(eventService.getEvent(year).getId()));
    }

}
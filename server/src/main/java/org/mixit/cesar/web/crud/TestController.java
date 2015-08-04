package org.mixit.cesar.web.crud;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.websocket.server.PathParam;

import org.mixit.cesar.dto.MemberResource;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.repository.EventRepository;
import org.mixit.cesar.repository.InterestRepository;
import org.mixit.cesar.repository.MemberRepository;
import org.mixit.cesar.repository.SessionRepository;
import org.mixit.cesar.repository.SharedLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO to delete. It just a test
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    SharedLinkRepository sharedLinkRepository;

    @RequestMapping(value = "members")
    public ResponseEntity<List<String>> members(){
        return new ResponseEntity<>(
                StreamSupport.stream(memberRepository.findAll().spliterator(), false)
                        .map(m -> m.toString())
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "events")
    public ResponseEntity<List<String>> events(){
        return new ResponseEntity<>(
                StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                        .map(m -> m.toString())
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "sessions")
    public ResponseEntity<List<String>> sessions(){
        return new ResponseEntity<>(
                StreamSupport.stream(sessionRepository.findAll().spliterator(), false)
                        .map(m -> m.toString())
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "links")
    public ResponseEntity<List<String>> links(){
        return new ResponseEntity<>(
                StreamSupport.stream(sharedLinkRepository.findAll().spliterator(), false)
                        .map(m -> m.toString())
                        .collect(Collectors.toList()), HttpStatus.OK);
    }
}
package org.mixit.cesar.web.api;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.dto.SessionResource;
import org.mixit.cesar.model.session.Session;
import org.mixit.cesar.repository.SessionRepository;
import org.mixit.cesar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Conference sessions",
        description = "Session Api helps you to find all the sessions of the conference (Talk, Workshop, Keynote and ligthning talks)")
@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    EventService eventService;

    private <T extends Session> ResponseEntity<List<SessionResource>> getAllSessions(List<T> sessions) {
        return new ResponseEntity<>(sessions
                .stream()
                .map(m -> SessionResource.convert(m))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping("/{id}")
    @ApiOperation(value = "Finds one session", httpMethod = "GET")
    public ResponseEntity<SessionResource> getSession(@PathVariable("id") Long id) {
        return new ResponseEntity<>(SessionResource.convert(sessionRepository.findOne(id)), HttpStatus.OK);
    }

    @RequestMapping
    @ApiOperation(value = "Finds all sessions", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllSessions(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllSessions(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/keynote")
    @ApiOperation(value = "Finds all keynotes", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllKeynotes(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllKeynotes(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/talk")
    @ApiOperation(value = "Finds all talks", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllTalks(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllTalks(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/workshop")
    @ApiOperation(value = "Finds all workshop", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllWorkshops(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllWorkshops(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/lightningtalks")
    @ApiOperation(value = "Finds all the lightning talks", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllLightningTalks(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllLightningTalks(eventService.getEvent(year).getId()));
    }
}
package org.mixit.cesar.site.web.api;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.site.web.api.dto.MemberResource;
import org.mixit.cesar.site.web.api.dto.SessionResource;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * In the public API we expose only accepted sessions
 */
@Api(value = "Conference sessions",
        description = "Session Api helps you to find all the sessions of the conference (Talk, Workshop, Keynote and ligthning talks)")
@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    EventService eventService;

    private <T extends Session<T>> ResponseEntity<List<SessionResource>> getAllSessions(List<T> sessions) {
        return ResponseEntity
                .ok()
                .body(sessions
                .stream()
                .map(m -> SessionResource.convert(m))
                .collect(Collectors.toList()));
    }

    @RequestMapping("/{id}")
    @ApiOperation(value = "Finds one session", httpMethod = "GET")
    public ResponseEntity<SessionResource> getSession(@PathVariable("id") Long id) {
        Session session = sessionRepository.findOne(id);
        session.setNbConsults(session.getNbConsults()+1);
        sessionRepository.save(session);
        return ResponseEntity
                .ok()
                .body(SessionResource.convert(session));
    }

    @RequestMapping
    public ResponseEntity<List<SessionResource>> getAllSessions(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllAcceptedSessions(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/keynote")
    @ApiOperation(value = "Finds all keynotes", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllKeynotes(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllAcceptedKeynotes(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/talk")
    @ApiOperation(value = "Finds all talks", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllTalks(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllAcceptedTalks(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/workshop")
    @ApiOperation(value = "Finds all workshop", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllWorkshops(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllAcceptedWorkshops(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/lightningtalks")
    @ApiOperation(value = "Finds all the lightning talks", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllLightningTalks(@RequestParam(required = false) Integer year) {
        return getAllSessions(sessionRepository.findAllAcceptedLightningTalks(eventService.getEvent(year).getId()));
    }

    @RequestMapping(value = "/interest/{name}")
    @ApiOperation(value = "Finds all sessions linked to an interest", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllSessions(@PathVariable String name) {
        return getAllSessions(sessionRepository.findAllSessionsByInterest(name));
    }
}
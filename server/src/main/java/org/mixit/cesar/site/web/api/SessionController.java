package org.mixit.cesar.site.web.api;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.mixit.cesar.CesarApplication.DATE_FORMAT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mixit.cesar.site.model.session.Format;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.mixit.cesar.site.service.EventService;
import org.mixit.cesar.site.service.qrcode.QrCodeGenerator;
import org.mixit.cesar.site.model.ListView;
import org.mixit.cesar.site.web.dto.SessionResource;
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
    private SessionRepository sessionRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    @Autowired
    private QrCodeGenerator qrCodeGenerator;

    private <T extends Session> List<SessionResource> getAllSessions(List<T> sessions) {
        return sessions
                .stream()
                .map(SessionResource::convert)
                .collect(Collectors.toList());
    }

    private <T extends Session> List<SessionResource> getAllSessionsWithQrCode(List<T> sessions) {
        return sessions
                .stream()
                .map(SessionResource::convert)
                .map(this::computeQrCode)
                .collect(Collectors.toList());
    }

    private SessionResource computeQrCode(SessionResource session) {
        String categoryUrl = urlFactory.getBaseUrl() + "/session/" + session.getIdSession();
        session.setQrCode(qrCodeGenerator.generateQRCode(categoryUrl, ErrorCorrectionLevel.H));
        return session;
    }

    @RequestMapping("/{id}")
    @ApiOperation(value = "Finds one session", httpMethod = "GET")
    public ResponseEntity<SessionResource> getSession(@PathVariable("id") Long id, @RequestParam(required = false) boolean anonymous) {
        Session session = sessionRepository.findOne(id);
        if (!anonymous) {
            session.setNbConsults(session.getNbConsults() + 1);
            sessionRepository.save(session);
        }
        return ResponseEntity
                .ok()
                .body(SessionResource.convert(session));
    }

    private boolean filterSession(Session session, Format format, LocalDateTime start, LocalDateTime end) {
        boolean result = format == null || session.getFormat() == format;
        if (result && session.getSlot() != null) {
            result = start == null || session.getSlot().getStart().isAfter(start);
            if (result) {
                result = end == null || session.getSlot().getStart().isBefore(end);
            }
        }
        return result;
    }

    @RequestMapping("/ranking")
    @ApiOperation(value = "Finds the ranking of the better sessions", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getSessionRanking(
            @ApiParam(required = false, name = "year", value = "Year if null return data for current year")
            @RequestParam(required = false) Integer year,
            @ApiParam(required = false, name = "format", value = "Format of the session : Keynote, Talk...")
            @RequestParam(required = false) Format format,
            @ApiParam(required = false, name = "start", value = "Start for the comparaison")
            @RequestParam(required = false) String start,
            @ApiParam(required = false, name = "end", value = "End for the comparaison")
            @RequestParam(required = false) String end,
            @ApiParam(required = true, name = "limit", value = "Nb elements in te ranking")
            @RequestParam(defaultValue = "5") Integer limit
    ) {

        return ResponseEntity
                .ok(getAllSessions(
                        sessionRepository
                                .findAllAcceptedSessions(eventService.getEvent(year))
                                .stream()
                                .filter(session -> filterSession(
                                        session,
                                        format,
                                        start == null ? null : LocalDateTime.parse(start, ofPattern(DATE_FORMAT)),
                                        end == null ? null : LocalDateTime.parse(end, ofPattern(DATE_FORMAT))
                                ))
                                .limit(limit)
                                .collect(Collectors.toList())));
    }

    @RequestMapping
    @ApiOperation(value = "Finds all sessions", httpMethod = "GET")
    @JsonView(ListView.class)
    public ResponseEntity<List<SessionResource>> getAllSessions(@RequestParam(required = false) Integer year) {
        return ResponseEntity
                .ok(getAllSessions(sessionRepository.findAllAcceptedSessions(eventService.getEvent(year))));
    }

    @RequestMapping(value = "/keynote")
    @ApiOperation(value = "Finds all keynotes", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllKeynotes(
            @ApiParam(name = "year", value = "Year if null return data for current year")
            @RequestParam(required = false) Integer year) {
        return ResponseEntity
                .ok(getAllSessions(sessionRepository.findAllAcceptedKeynotes(eventService.getEvent(year))));
    }

    @RequestMapping(value = "/talk")
    @ApiOperation(value = "Finds all talks", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllTalks(
            @ApiParam(name = "year", value = "Year if null return data for current year")
            @RequestParam(required = false) Integer year) {
        return ResponseEntity
                .ok(getAllSessions(sessionRepository.findAllAcceptedTalks(eventService.getEvent(year))));
    }

    @RequestMapping(value = "/workshop")
    @ApiOperation(value = "Finds all workshop", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllWorkshops(
            @ApiParam(name = "year", value = "Year if null return data for current year")
            @RequestParam(required = false) Integer year) {
        return ResponseEntity
                .ok(getAllSessions(sessionRepository.findAllAcceptedWorkshops(eventService.getEvent(year))));
    }

    @RequestMapping(value = "/lightningtalks")
    @ApiOperation(value = "Finds all the lightning talks", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllLightningTalks(
            @ApiParam(name = "year", value = "Year if null return data for current year")
            @RequestParam(required = false) Integer year) {
        return ResponseEntity
                .ok(getAllSessions(sessionRepository.findAllLightningTalks(eventService.getEvent(year))));
    }

    @RequestMapping(value = "/interest/{name}")
    @ApiOperation(value = "Finds all sessions linked to an interest", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllSessions(
            @PathVariable String name) {
        return ResponseEntity
                .ok(getAllSessions(sessionRepository.findAllSessionsByInterest(name)));
    }

    @RequestMapping(value = "/qrcode")
    @ApiOperation(value = "Finds all sessions with Qrcode", httpMethod = "GET")
    public ResponseEntity<List<SessionResource>> getAllSessionsWithCode(@RequestParam(required = false) Integer year) {
        return ResponseEntity
                .ok(getAllSessionsWithQrCode(sessionRepository.findAllAcceptedSessions(eventService.getEvent(year))));
    }


}
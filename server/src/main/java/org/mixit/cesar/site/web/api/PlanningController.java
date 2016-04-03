package org.mixit.cesar.site.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.planning.Room;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.SlotRepository;
import org.mixit.cesar.site.service.EventService;
import org.mixit.cesar.site.web.dto.MemberResource;
import org.mixit.cesar.site.web.dto.RoomResource;
import org.mixit.cesar.site.web.dto.SessionResource;
import org.mixit.cesar.site.web.dto.SlotDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planning")
public class PlanningController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/room")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResource> rooms() {
        return Stream
                .of(Room.values())
                .map(r -> new RoomResource()
                        .setKey(r.toString())
                        .setCapacity(r.getCapacity())
                        .setName(r.getName())
                )
                .collect(Collectors.toList());
    }


    @RequestMapping
    @ResponseStatus(HttpStatus.OK)
    @JsonView(FlatView.class)
    public Map<Room, List<Slot>> getAllSlotsByRoom(@RequestParam(required = false) Integer year) {
        return slotRepository
                .findAllSlots(eventService.getEvent(year).getId())
                .stream()
                .filter(s-> s.getRoom()!=null)
                .sorted((a, b) -> a.getStart().compareTo(b.getStart()))
                .collect(Collectors.groupingBy(Slot::getRoom));
    }

    @RequestMapping(value = "/transversal")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(FlatView.class)
    public List<Slot> getAllTransversalSlots(@RequestParam(required = false) Integer year) {
        return slotRepository
                .findAllSlots(eventService.getEvent(year).getId())
                .stream()
                .filter(s-> s.getRoom()==null)
                .sorted((a, b) -> a.getStart().compareTo(b.getStart()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/others")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(FlatView.class)
    public List<Slot> getAllSlotsDifferentThanSessions(@RequestParam(required = false) Integer year) {
        return slotRepository
                .findAllSlots(eventService.getEvent(year).getId())
                .stream()
                .filter(s-> s.getSession()==null)
                .sorted((a, b) -> a.getStart().compareTo(b.getStart()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/slots")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(FlatView.class)
    public List<Slot> getAllSlots(@RequestParam(required = false) Integer year) {
        return slotRepository
                .findAllSlots(eventService.getEvent(year).getId())
                .stream()
                .filter(s -> s.getSession()!=null)
                .sorted((a, b) -> a.getStart().compareTo(b.getStart()))
                .collect(Collectors.toList());
    }
}
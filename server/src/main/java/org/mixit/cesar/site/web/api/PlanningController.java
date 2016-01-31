package org.mixit.cesar.site.web.api;

import java.time.LocalDateTime;
import java.util.Arrays;
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
import org.mixit.cesar.site.web.api.dto.RoomResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planning")
@Transactional
public class PlanningController {

    @Autowired
    private SessionRepository sessionRepository;

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
    public Map<Room, List<Slot>> sesions(@RequestParam(required = false) Integer year) {
        Map<Room, List<Slot>> slots = new HashMap<>();

        slots.put(Room.Amphi1, Arrays.asList(
                new Slot()
                        .setRoom(Room.Amphi1)
                        .setLabel("planning.accueil")
                        .setStart(LocalDateTime.of(2016, 4, 21, 8, 0))
                        .setEnd(LocalDateTime.of(2016, 4, 21, 9, 10)),
                new Slot()
                        .setSession(sessionRepository.findOne(631L))
                        .setRoom(Room.Amphi1)
                        .setStart(LocalDateTime.of(2016, 4, 21, 10, 0))
                        .setEnd(LocalDateTime.of(2016, 4, 21, 10, 30)),
                new Slot()
                        .setSession(sessionRepository.findOne(711L))
                        .setRoom(Room.Amphi1)
                        .setStart(LocalDateTime.of(2016, 4, 21, 10, 30))
                        .setEnd(LocalDateTime.of(2016, 4, 21, 11, 40)),
                new Slot()
                        .setSession(sessionRepository.findOne(771L))
                        .setRoom(Room.Amphi1)
                        .setStart(LocalDateTime.of(2016, 4, 21, 13, 30))
                        .setEnd(LocalDateTime.of(2016, 4, 21, 14, 20))
        ));

        return slots;
    }
//    @RequestMapping(value = "/room")
//    @ResponseStatus(HttpStatus.OK)
//    public Map<String, String> rooms() {
//        return Stream.of(Room.values()).collect(Collectors.toMap(Room::toString, Room::getName));
//    }
}
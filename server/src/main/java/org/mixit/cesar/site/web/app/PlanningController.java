package org.mixit.cesar.site.web.app;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mixit.cesar.site.model.planning.Room;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/planning")
@Transactional
public class PlanningController {

    @RequestMapping(value = "/room")
    @ResponseStatus(HttpStatus.OK)
    public List<Room> categories() {
        return Stream.of(Room.values()).collect(Collectors.toList());
    }
}
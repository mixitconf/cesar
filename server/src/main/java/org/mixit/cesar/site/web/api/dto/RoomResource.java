package org.mixit.cesar.site.web.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.model.session.Vote;
import org.mixit.cesar.site.web.api.MemberController;
import org.mixit.cesar.site.web.api.SessionController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * Room
 */
public class RoomResource extends ResourceSupport {

    private String key;
    public int capacity;
    public String name;

    public String getKey() {
        return key;
    }

    public RoomResource setKey(String key) {
        this.key = key;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public RoomResource setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public String getName() {
        return name;
    }

    public RoomResource setName(String name) {
        this.name = name;
        return this;
    }
}

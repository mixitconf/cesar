package org.mixit.cesar.site.web.dto;

import org.springframework.hateoas.ResourceSupport;

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

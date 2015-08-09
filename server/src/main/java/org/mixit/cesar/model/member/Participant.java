package org.mixit.cesar.model.member;

import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.mixit.cesar.model.event.Event;
import org.mixit.cesar.model.security.Role;

@Entity
public class Participant<T extends Participant> extends Member<T> {

    public Participant() {
        ROLES.add(Role.Member);
        ROLES.add(Role.Participant);
    }

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Event> events = new TreeSet<>();

    public Set<Event> getEvents() {
        return events;
    }

    public T clearEvents() {
        this.events.clear();
        return (T) this;
    }

    public T addEvent(Event event) {
        this.events.add(event);
        return (T) this;
    }

    public T removeSharedLink(Event event) {
        this.events.remove(event);
        return (T) this;
    }
}

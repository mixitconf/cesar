package org.mixit.cesar.site.model.planning;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Type;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.session.Session;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 30/01/16.
 */
@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    /**
     * The room where the session is
     */
    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private Room room;

    /**
     * Session associated with this slot. For an event like lunch, pause... we have
     * no session
     */
    @JsonView(FlatView.class)
    @OneToOne(mappedBy = "slot")
    private Session session;

    @ManyToOne(optional = false)
    private Event event;

    /**
     * Start time of this slot
     */
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @JsonView(FlatView.class)
    private LocalDateTime start;

    /**
     * End time of this slot
     */
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @JsonView(FlatView.class)
    private LocalDateTime end;

    /**
     * Label
     */
    @JsonView(FlatView.class)
    private String label;


    public Long getId() {
        return id;
    }

    public Slot setId(Long id) {
        this.id = id;
        return this;
    }

    public Room getRoom() {
        return room;
    }

    public Slot setRoom(Room room) {
        this.room = room;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public Slot setSession(Session session) {
        this.session = session;
        return this;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Slot setStart(LocalDateTime start) {
        this.start = start;
        return this;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getLabel() {
        return label;
    }

    public Slot setLabel(String label) {
        this.label = label;
        return this;
    }

    public Slot setEnd(LocalDateTime end) {
        this.end = end;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}

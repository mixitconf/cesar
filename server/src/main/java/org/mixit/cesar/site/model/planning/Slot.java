package org.mixit.cesar.site.model.planning;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.mixit.cesar.site.model.session.Session;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 30/01/16.
 */
@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The room where the session is
     */
    @Enumerated(EnumType.STRING)
    private Room room;

    /**
     * Session associated with this slot. For an event like lunch, pause... we have
     * no session
     */
    @ManyToOne(optional = true)
    private Session session;

    /**
     * Start time of this slot
     */
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime start;

    /**
     * End time of this slot
     */
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime end;

    /**
     * Label
     */
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
}

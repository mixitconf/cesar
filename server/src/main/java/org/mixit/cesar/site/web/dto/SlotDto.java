package org.mixit.cesar.site.web.dto;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.mixit.cesar.CesarApplication.DATE_FORMAT;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mixit.cesar.site.model.planning.Room;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.model.session.Talk;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 23/02/16.
 */
public class SlotDto implements Serializable{

    private Long id;
    private Room room;
    private Long idSession;
    private String start;
    private String end;
    private String label;

    public static SlotDto convert(Slot slot) {
        return new SlotDto()
                .setId(slot.getId())
                .setEnd(slot.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))
                .setStart(slot.getStart().format(DateTimeFormatter.ISO_DATE_TIME))
                .setRoom(slot.getRoom())
                .setIdSession(slot.getSession() != null ? slot.getSession().getId() : null)
                .setLabel(slot.getLabel());
    }

    public Slot convert() {
        Slot slot = new Slot()
                .setId(getId())
                .setEnd(getEndDate())
                .setStart(getStartDate())
                .setRoom(getRoom())
                .setLabel(getLabel());
        if (getIdSession() != null) {
            slot.setSession(new Talk().setId(getIdSession()));
        }
        return slot;
    }

    public Long getId() {
        return id;
    }

    public SlotDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Room getRoom() {
        return room;
    }

    public SlotDto setRoom(Room room) {
        this.room = room;
        return this;
    }

    public Long getIdSession() {
        return idSession;
    }

    public SlotDto setIdSession(Long idSession) {
        this.idSession = idSession;
        return this;
    }

    public String getStart() {
        return start;
    }

    public LocalDateTime getStartDate() {
        return getStart() == null ? null : LocalDateTime.parse(getStart(), ofPattern(DATE_FORMAT));
    }

    public SlotDto setStart(String start) {
        this.start = start;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public LocalDateTime getEndDate() {
        return getEnd() == null ? null : LocalDateTime.parse(getEnd(), ofPattern(DATE_FORMAT));
    }

    public SlotDto setEnd(String end) {
        this.end = end;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public SlotDto setLabel(String label) {
        this.label = label;
        return this;
    }
}

package org.mixit.cesar.site.model.member;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mixit.cesar.site.model.event.Event;

/**
 * A comment on e session talk.
 */
@Entity
public class MemberEvent {

    public enum Level{
        GOLD,
        SILVER,
        BRONZE,
        LANYARD,
        PARTY,
        BREAKFAST,
        LUNCH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    public Member member;

    @ManyToOne(optional = false)
    public Event event;

    @Enumerated(EnumType.STRING)
    public Level level;

    public Long getId() {
        return id;
    }

    public MemberEvent setId(Long id) {
        this.id = id;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public MemberEvent setMember(Member member) {
        this.member = member;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public MemberEvent setEvent(Event event) {
        this.event = event;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    public MemberEvent setLevel(Level level) {
        this.level = level;
        return this;
    }
}

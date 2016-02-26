package org.mixit.cesar.site.model.event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.model.UserView;

@Entity
public class Event implements Comparable<Event>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int year;

    private boolean current;

    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentInstantAsMillisLong")
    @JsonView(UserView.class)
    private Instant day1;

    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentInstantAsMillisLong")
    @JsonView(UserView.class)
    private Instant day2;

    public Long getId() {
        return id;
    }

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public int getYear() {
        return year;
    }

    public Event setYear(int year) {
        this.year = year;
        return this;
    }

    public boolean isCurrent() {
        return current;
    }

    public Event setCurrent(boolean current) {
        this.current = current;
        return this;
    }

    @Override
    public int compareTo(Event o) {
        return id.compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Instant getDay1() {
        return day1;
    }

    public Event setDay1(Instant day1) {
        this.day1 = day1;
        return this;
    }

    public Instant getDay2() {
        return day2;
    }

    public Event setDay2(Instant day2) {
        this.day2 = day2;
        return this;
    }
}

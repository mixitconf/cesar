package org.mixit.cesar.site.model.member;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.UserView;

/**
 * Member Interest
 */
@Entity
public class Interest implements Comparable<Interest> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({FlatView.class, UserView.class})
    private Long id;

    @Size(max = 50)
    @JsonView({FlatView.class, UserView.class})
    public String name;

    public Long getId() {
        return id;
    }

    public Interest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Interest setName(String name) {
        this.name = name.trim();
        return this;
    }

    public int compareTo(Interest otherInterest) {
        return name.compareTo(otherInterest.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interest interest = (Interest) o;
        return Objects.equals(name, interest.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Interest{" +
                "name='" + name + '\'' +
                '}';
    }
}

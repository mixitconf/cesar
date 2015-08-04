package org.mixit.cesar.model.member;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Member Interest
 */
@Entity
public class Interest implements Comparable<Interest> {

    @Id
    @Size(max = 50)
    public String name;

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

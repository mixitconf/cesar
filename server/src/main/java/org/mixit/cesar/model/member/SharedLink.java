package org.mixit.cesar.model.member;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An URL shared by a member on his profile
 */
@Entity
public class SharedLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public int ordernum;

    @NotNull
    @Size(max = 50)
    public String name;

    @Size(max = 250)
    @org.hibernate.validator.constraints.URL
    @NotNull
    public String URL;

    @ManyToOne(optional = false)
    public Member member;

    public Long getId() {
        return id;
    }

    public SharedLink setId(Long id) {
        this.id = id;
        return this;
    }

    public int getOrdernum() {
        return ordernum;
    }

    public SharedLink setOrdernum(int ordernum) {
        this.ordernum = ordernum;
        return this;
    }

    public String getName() {
        return name;
    }

    public SharedLink setName(String name) {
        this.name = name;
        return this;
    }

    public String getURL() {
        return URL;
    }

    public SharedLink setURL(String URL) {
        this.URL = URL;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public SharedLink setMember(Member member) {
        this.member = member;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedLink that = (SharedLink) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(URL, that.URL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, URL);
    }

    @Override
    public String toString() {
        return "SharedLink{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}

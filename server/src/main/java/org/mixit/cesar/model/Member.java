package org.mixit.cesar.model;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.collect.ComparisonChain;
import org.hibernate.validator.constraints.Email;

@Entity
public class Member implements Comparable<Member> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Internal login : functional key
     */
    @NotNull
    @Size(max = 100)
    private String login;

    @Email
    @Size(max = 250)
    private String email;

    @Size(max = 100)
    private String firstname;

    @Size(max = 100)
    private String lastname;

    @Size(max = 100)
    private String company;

    private Boolean ticketingRegistered;

    private Instant registeredAt = Instant.now();

    /**
     * User-defined description, potentially as MarkDown
     */
    private String shortDescription;

    /**
     * User-defined description, potentially as MarkDown
     */
    private String longDescription;

    /**
     * Number of profile consultations
     */
    @Min(0)
    private long nbConsults;

    /**
     * true if profile is public (visible not connected)
     */
    @NotNull
    private Boolean publicProfile = Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public Member setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Member setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Member setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public Member setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public Member setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public Member setCompany(String company) {
        this.company = company;
        return this;
    }

    public Boolean getTicketingRegistered() {
        return ticketingRegistered;
    }

    public Member setTicketingRegistered(Boolean ticketingRegistered) {
        this.ticketingRegistered = ticketingRegistered;
        return this;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public Member setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Member setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public Member setLongDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public long getNbConsults() {
        return nbConsults;
    }

    public Member setNbConsults(long nbConsults) {
        this.nbConsults = nbConsults;
        return this;
    }

    public Boolean getPublicProfile() {
        return publicProfile;
    }

    public Member setPublicProfile(Boolean publicProfile) {
        this.publicProfile = publicProfile;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) &&
                Objects.equals(login, member.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

    @Override
    public int compareTo(Member member) {
        return ComparisonChain.start()
                .compare(this.lastname, member.lastname)
                .compare(this.firstname, member.firstname)
                .result();
    }
}
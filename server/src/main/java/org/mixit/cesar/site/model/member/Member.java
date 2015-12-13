package org.mixit.cesar.site.model.member;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.ComparisonChain;
import org.hibernate.validator.constraints.Email;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.UserView;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.session.Session;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Member<T extends Member> implements Comparable<Member> {

    @Transient
    protected final Set<Role> ROLES = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({FlatView.class, UserView.class})
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
    @JsonView(FlatView.class)
    private String firstname;

    @Size(max = 100)
    @JsonView(FlatView.class)
    private String lastname;

    @Size(max = 100)
    @JsonView({FlatView.class, UserView.class})
    private String company;

    private Boolean ticketingRegistered;

    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @JsonView(UserView.class)
    private LocalDateTime registeredAt = LocalDateTime.now();

    /**
     * User-defined description, potentially as MarkDown
     */
    @Size(max = 300)
    @JsonView({FlatView.class, UserView.class})
    private String shortDescription;

    /**
     * User-defined description, potentially as MarkDown
     */
    @Lob
    @JsonView(UserView.class)
    private String longDescription;

    /**
     * Number of profile consultations
     */
    @Min(0)
    @JsonView(UserView.class)
    private long nbConsults = 0;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JsonView(UserView.class)
    public Set<Interest> interests = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @OrderColumn(name = "ordernum")
    @JsonView(UserView.class)
    public List<SharedLink> sharedLinks = new LinkedList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    public Set<MemberEvent> memberEvents = new HashSet<>();

    @ManyToMany(mappedBy = "speakers")
    public Set<Session> sessions = new HashSet<>();

    /**
     * true if profile is public (visible not connected)
     */
    @NotNull
    private Boolean publicProfile = Boolean.FALSE;

    public Member() {
        ROLES.add(Role.MEMBER);
    }

    public boolean hasRole(Role role) {
        return ROLES.contains(role);
    }


    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public Set<Role> getROLES() {
        return ROLES;
    }

    public String getLogin() {
        return login;
    }

    public T setLogin(String login) {
        this.login = login;
        return (T) this;
    }

    public String getEmail() {
        return email;
    }

    public T setEmail(String email) {
        this.email = email;
        return (T) this;
    }

    public String getFirstname() {
        return firstname;
    }

    public T setFirstname(String firstname) {
        this.firstname = firstname;
        return (T) this;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public T clearSessions() {
        this.sessions.clear();
        return (T) this;
    }

    public T addSession(Session session) {
        this.sessions.add(session);
        return (T) this;
    }

    public T removeSharedLink(Session session) {
        this.sessions.remove(session);
        return (T) this;
    }

    public Set<MemberEvent> getMemberEvents() {
        return memberEvents;
    }

    public T clearMemberEvent() {
        this.memberEvents.clear();
        return (T) this;
    }

    public T addMemberEvent(MemberEvent event) {
        this.memberEvents.add(event);
        return (T) this;
    }

    public T removeMemberEvent(MemberEvent event) {
        this.memberEvents.remove(event);
        return (T) this;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public T addInterest(Interest interest) {
        this.interests.add(interest);
        return (T) this;
    }

    public T removeInterest(Interest interest) {
        this.interests.remove(interest);
        return (T) this;
    }

    public T clearInterests() {
        this.interests.clear();
        return (T) this;
    }

    public List<SharedLink> getSharedLinks() {
        return sharedLinks;
    }

    public T addSharedLink(SharedLink link) {
        this.sharedLinks.add(link);
        return (T) this;
    }

    public T removeSharedLink(SharedLink link) {
        this.sharedLinks.remove(link);
        return (T) this;
    }

    public T clearSharedLinks() {
        this.sharedLinks.clear();
        return (T) this;
    }

    public String getLastname() {
        return lastname;
    }

    public T setLastname(String lastname) {
        this.lastname = lastname;
        return (T) this;
    }

    public String getCompany() {
        return company;
    }

    public T setCompany(String company) {
        this.company = company;
        return (T) this;
    }

    public Boolean getTicketingRegistered() {
        return ticketingRegistered;
    }

    public T setTicketingRegistered(Boolean ticketingRegistered) {
        this.ticketingRegistered = ticketingRegistered;
        return (T) this;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public T setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
        return (T) this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public T setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return (T) this;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public T setLongDescription(String longDescription) {
        this.longDescription = longDescription;
        return (T) this;
    }

    public long getNbConsults() {
        return nbConsults;
    }

    public T setNbConsults(long nbConsults) {
        this.nbConsults = nbConsults;
        return (T) this;
    }

    public Boolean getPublicProfile() {
        return publicProfile;
    }

    public T setPublicProfile(Boolean publicProfile) {
        this.publicProfile = publicProfile;
        return (T) this;
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
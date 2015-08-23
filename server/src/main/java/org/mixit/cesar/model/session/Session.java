package org.mixit.cesar.model.session;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.mixit.cesar.model.event.Event;
import org.mixit.cesar.model.member.Interest;
import org.mixit.cesar.model.member.Speaker;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Session<T extends Session> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    protected Format format;

    @ManyToOne(optional = false)
    private Event event;

    @NotNull
    @Size(max = 100)
    private String title;

    @Size(max = 300)
    private String summary;

    private Instant addedAt = Instant.now();

    private Integer maxAttendees;

    @Enumerated(EnumType.STRING)
    private Level level;

    /** Is this session a guest session **/
    private boolean guest=false;

    @Lob
    private String messageForStaff;

    @Lob
    @NotNull
    private String description;

    @Column
    @Lob
    private String ideaForNow;

    @ManyToMany
    @NotNull
    private Set<Speaker> speakers = new HashSet<Speaker>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Interest> interests = new TreeSet<Interest>();

    /** Eventual comments */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    private List<SessionComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    /** Number of consultation */
    private long nbConsults;

    /** Is session validated (publicly visible) */
    protected boolean valid;

    @Enumerated(EnumType.STRING)
    @NotNull   // But nullable : must give language when editing, but not always given on old sessions.
    private SessionLanguage lang = SessionLanguage.fr;

    /* true if Staff has given feedback */
    private boolean feedback;

    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    public Format getFormat() {
        return format;
    }

    public T setFormat(Format format) {
        this.format = format;
        return (T) this;
    }

    public Event getEvent() {
        return event;
    }

    public T setEvent(Event event) {
        this.event = event;
        return (T) this;
    }

    public String getTitle() {
        return title;
    }

    public T setTitle(String title) {
        this.title = title;
        return (T) this;
    }

    public String getSummary() {
        return summary;
    }

    public T setSummary(String summary) {
        this.summary = summary;
        return (T) this;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public T setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
        return (T) this;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public String getMessageForStaff() {
        return messageForStaff;
    }

    public void setMessageForStaff(String messageForStaff) {
        this.messageForStaff = messageForStaff;
    }

    public String getDescription() {
        return description;
    }

    public T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getIdeaForNow() {
        return ideaForNow;
    }

    public T setIdeaForNow(String ideaForNow) {
        this.ideaForNow = ideaForNow;
        return (T) this;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public T addSpeaker(Speaker speaker) {
        this.speakers.add(speaker);
        return (T) this;
    }

    public T removeSpeaker(Speaker speaker) {
        this.speakers.remove(speaker);
        return (T) this;
    }

    public T clearSpeakers() {
        this.speakers.clear();
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

    public List<SessionComment> getComments() {
        return comments;
    }

    public T setComments(List<SessionComment> comments) {
        this.comments = comments;
        return (T) this;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public T addVote(Vote vote) {
        this.votes.add(vote);
        return (T) this;
    }

    public T removeVote(Vote vote) {
        this.votes.remove(vote);
        return (T) this;
    }

    public T clearVotes() {
        this.votes.clear();
        return (T) this;
    }

    public long getNbConsults() {
        return nbConsults;
    }

    public T setNbConsults(long nbConsults) {
        this.nbConsults = nbConsults;
        return (T) this;
    }

    public boolean isValid() {
        return valid;
    }

    public T setValid(boolean valid) {
        this.valid = valid;
        return (T) this;
    }

    public SessionLanguage getLang() {
        return lang;
    }

    public T setLang(SessionLanguage lang) {
        this.lang = lang;
        return (T) this;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public T setFeedback(boolean feedback) {
        this.feedback = feedback;
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session<?> session = (Session<?>) o;
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package org.mixit.cesar.site.model.session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Type;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.planning.Slot;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    protected Format format;

    @ManyToOne(optional = false)
    private Event event;

    @NotNull
    @Size(max = 100)
    @JsonView(FlatView.class)
    private String title;

    @Size(max = 300)
    private String summary;

    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime addedAt = LocalDateTime.now();

    private Integer maxAttendees;

    @Enumerated(EnumType.STRING)
    private Level level;

    /**
     * Is this session a guest session
     **/
    private boolean guest = false;

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
    private Set<Member> speakers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Interest> interests = new TreeSet<Interest>();

    /**
     * Eventual comments
     */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    private List<SessionComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    @OneToOne(optional = true)
    private Slot slot;

    /**
     * Number of consultation
     */
    private long nbConsults = 0;

    /**
     * Is session validated (publicly visible)
     */
    protected boolean valid;

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private SessionLanguage lang = SessionLanguage.fr;

    private Boolean sessionAccepted;

    /* true if Staff has given feedback */
    private boolean feedback;

    @Size(max = 255)
    @JsonView(FlatView.class)
    private String link;

    public static Session createKeynote(){
        return new Session().setFormat(Format.Keynote);
    }

    public static Session createWorkshop(){
        return new Session().setFormat(Format.Workshop);
    }

    public static Session createRandom(){
        return new Session().setFormat(Format.Random);
    }

    public static Session createLightningTalk(){
        return new Session().setFormat(Format.LightningTalk);
    }

    public static Session createTalk(){
        return new Session().setFormat(Format.Talk);
    }

    public Long getId() {
        return id;
    }

    public Session setId(Long id) {
        this.id = id;
        return this;
    }

    public Format getFormat() {
        return format;
    }

    public Session setFormat(Format format) {
        this.format = format;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Session setEvent(Event event) {
        this.event = event;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Session setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Session setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public Session setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
        return this;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public Session setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    public Session setLevel(Level level) {
        this.level = level;
        return this;
    }

    public boolean isGuest() {
        return guest;
    }

    public Session setGuest(boolean guest) {
        this.guest = guest;
        return this;
    }

    public String getMessageForStaff() {
        return messageForStaff;
    }

    public Session setMessageForStaff(String messageForStaff) {
        this.messageForStaff = messageForStaff;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Session setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIdeaForNow() {
        return ideaForNow;
    }

    public Session setIdeaForNow(String ideaForNow) {
        this.ideaForNow = ideaForNow;
        return this;
    }

    public Set<Member> getSpeakers() {
        return speakers;
    }

    public boolean containsSpeaker(Long idSpeaker) {
        return this.speakers
                .stream()
                .filter(s -> s.getId().equals(idSpeaker))
                .findFirst()
                .isPresent();
    }

    public Session addSpeaker(Member speaker) {
        this.speakers.add(speaker);
        return this;
    }

    public Session removeSpeaker(Member speaker) {
        this.speakers.remove(speaker);
        return this;
    }

    public Session clearSpeakers() {
        this.speakers.clear();
        return this;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public Session addInterest(Interest interest) {
        this.interests.add(interest);
        return this;
    }

    public Session removeInterest(Interest interest) {
        this.interests.remove(interest);
        return this;
    }

    public Session clearInterests() {
        this.interests.clear();
        return this;
    }

    public List<SessionComment> getComments() {
        return comments;
    }

    public Session setComments(List<SessionComment> comments) {
        this.comments = comments;
        return this;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public int getPositiveVotes() {
        return votes.stream().distinct().collect(Collectors.summingInt(v -> Boolean.TRUE.equals(v.getValue()) ? 1 : 0));
    }

    public float getPositiveVotePercents() {
        if (votes.isEmpty()) {
            return 0;
        }
        return getPositiveVotes() / votes.size();
    }

    public Session addVote(Vote vote) {
        this.votes.add(vote);
        return this;
    }

    public Session removeVote(Vote vote) {
        this.votes.remove(vote);
        return this;
    }

    public Session clearVotes() {
        this.votes.clear();
        return this;
    }

    public long getNbConsults() {
        return nbConsults;
    }

    public Session setNbConsults(long nbConsults) {
        this.nbConsults = nbConsults;
        return this;
    }

    public boolean isValid() {
        return valid;
    }

    public Session setValid(boolean valid) {
        this.valid = valid;
        return this;
    }

    public SessionLanguage getLang() {
        return lang;
    }

    public Session setLang(SessionLanguage lang) {
        this.lang = lang;
        return this;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public Session setFeedback(boolean feedback) {
        this.feedback = feedback;
        return this;
    }

    public Slot getSlot() {
        return slot;
    }

    public Session setSlot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Session setLink(String link) {
        this.link = link;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id);
    }

    public Boolean getSessionAccepted() {
        return sessionAccepted;
    }

    public Session setSessionAccepted(Boolean sessionAccepted) {
        this.sessionAccepted = sessionAccepted;
        return this;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

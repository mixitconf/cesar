package org.mixit.cesar.cfp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Type;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.session.Format;
import org.mixit.cesar.site.model.session.Level;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.model.session.SessionLanguage;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private Format format;

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private ProposalCategory category;

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private ProposalTypeSession typeSession;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Session session;

    @NotNull
    @Size(max = 100)
    @JsonView(FlatView.class)
    private String title;

    @Size(max = 300)
    @JsonView(FlatView.class)
    private String summary;

    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @JsonView(FlatView.class)
    private LocalDateTime addedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private ProposalNbAttendees maxAttendees;

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private Level level;

    /**
     * Is this session a guest session
     **/
    private boolean guest = false;

    @Lob
    @JsonView(FlatView.class)
    private String messageForStaff;

    @Lob
    @JsonView(FlatView.class)
    private String description;

    @Column
    @Lob
    @JsonView(FlatView.class)
    private String ideaForNow;

    @ManyToMany
    @JsonView(FlatView.class)
    private Set<Member> speakers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JsonView(FlatView.class)
    private Set<Interest> interests = new HashSet<>();

    /**
     * Eventual comments
     */
    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    private List<ProposalComment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private SessionLanguage lang = SessionLanguage.fr;

    @Enumerated(EnumType.STRING)
    @JsonView(FlatView.class)
    private ProposalStatus status;

    /* true if Staff has given feedback */
    private boolean feedback;

    @JsonView(FlatView.class)
    private boolean valid;

    @Size(max = 255)
    @JsonView(FlatView.class)
    private String link;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    private List<ProposalVote> votes = new ArrayList<>();

    public Session toSession(Session session){
        if(session==null){
            session = new Session();
        }

        session
                .setFormat(format)
                .setDescription(description)
                .setEvent(event)
                .setIdeaForNow(ideaForNow)
                .setLang(lang)
                .setLevel(level)
                .setLink(link)
                .setMessageForStaff(messageForStaff)
                .setMaxAttendees(maxAttendees.getNbMax())
                .setSummary(summary)
                .setTitle(title)
                .clearInterests()
                .clearSpeakers();

        final Session sess = session;
        interests.stream().forEach(i -> sess.addInterest(i));
        speakers.stream().forEach(s -> sess.addSpeaker(s));

        return session;
    }


    public Long getId() {
        return id;
    }

    public Proposal setId(Long id) {
        this.id = id;
        return this;
    }

    public Format getFormat() {
        return format;
    }

    public Proposal setFormat(Format format) {
        this.format = format;
        return this;
    }

    public ProposalCategory getCategory() {
        return category;
    }

    public Proposal setCategory(ProposalCategory category) {
        this.category = category;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Proposal setEvent(Event event) {
        this.event = event;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Proposal setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Proposal setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public ProposalTypeSession getTypeSession() {
        return typeSession;
    }

    public Proposal setTypeSession(ProposalTypeSession typeSession) {
        this.typeSession = typeSession;
        return this;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public Proposal setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
        return this;
    }

    public ProposalNbAttendees getMaxAttendees() {
        return maxAttendees;
    }

    public Proposal setMaxAttendees(ProposalNbAttendees maxAttendees) {
        this.maxAttendees = maxAttendees;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    public Proposal setLevel(Level level) {
        this.level = level;
        return this;
    }

    public boolean isGuest() {
        return guest;
    }

    public Proposal setGuest(boolean guest) {
        this.guest = guest;
        return this;
    }

    public String getMessageForStaff() {
        return messageForStaff;
    }

    public Proposal setMessageForStaff(String messageForStaff) {
        this.messageForStaff = messageForStaff;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Proposal setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIdeaForNow() {
        return ideaForNow;
    }

    public Proposal setIdeaForNow(String ideaForNow) {
        this.ideaForNow = ideaForNow;
        return this;
    }

    public Set<Member> getSpeakers() {
        return speakers;
    }

    public Proposal addSpeaker(Member speaker) {
        this.speakers.add(speaker);
        return this;
    }

    public Proposal removeSpeaker(Member speaker) {
        this.speakers.remove(speaker);
        return this;
    }

    public Proposal clearSpeakers() {
        this.speakers.clear();
        return this;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public Proposal addInterest(Interest interest) {
        this.interests.add(interest);
        return this;
    }

    public Proposal removeInterest(Interest interest) {
        this.interests.remove(interest);
        return this;
    }

    public Proposal clearInterests() {
        this.interests.clear();
        return this;
    }

    public List<ProposalComment> getComments() {
        return comments;
    }

    public Proposal setComments(List<ProposalComment> comments) {
        this.comments = comments;
        return this;
    }

    public SessionLanguage getLang() {
        return lang;
    }

    public Proposal setLang(SessionLanguage lang) {
        this.lang = lang;
        return this;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public Proposal setFeedback(boolean feedback) {
        this.feedback = feedback;
        return this;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public Proposal setStatus(ProposalStatus status) {
        this.status = status;
        return this;
    }

    public boolean isValid() {
        return valid;
    }

    public Proposal setValid(boolean valid) {
        this.valid = valid;
        return this;
    }

    public List<ProposalVote> getVotes() {
        return votes;
    }

    public void setVotes(List<ProposalVote> votes) {
        this.votes = votes;
    }

    public Session getSession() {
        return session;
    }

    public Proposal setSession(Session session) {
        this.session = session;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Proposal setLink(String link) {
        this.link = link;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposal session = (Proposal) o;
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

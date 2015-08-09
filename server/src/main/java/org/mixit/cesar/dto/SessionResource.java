package org.mixit.cesar.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mixit.cesar.model.member.Interest;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.member.SharedLink;
import org.mixit.cesar.model.member.Speaker;
import org.mixit.cesar.model.session.Session;
import org.mixit.cesar.model.session.Vote;
import org.mixit.cesar.web.api.MemberController;
import org.mixit.cesar.web.api.SessionController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * Member DTO
 */
public class SessionResource extends ResourceSupport {

    private long idSession;
    public int votes = 0;
    public int positiveVotes = 0;
    public long nbConsults;
    public String lang;
    public String format;
    public String title;
    public String summary;
    public String description;
    public String ideaForNow;
    public String room;
    public Date start;
    public Date end;
    public List<Long> speakers = new ArrayList<>();
    public List<String> interests = new ArrayList<>();

    public static <T extends Session> SessionResource convert(T session){
        //TODO room + start + end
        SessionResource sessionResource = new SessionResource()
                .setIdSession(session.getId())
                .setDescription(session.getDescription())
                .setFormat(session.getFormat().toString())
                .setIdeaForNow(session.getIdeaForNow())
                .setLang(session.getLang().toString())
                .setSummary(session.getSummary())
                .setTitle(session.getTitle())
                .setNbConsults(session.getNbConsults());

        List<Vote> votes = session.getVotes();
        if(!votes.isEmpty()){
            votes.stream().forEach(v -> {
                sessionResource.setVotes(sessionResource.getVotes() + 1);
                if (Boolean.TRUE.equals(v.getValue())) {
                    sessionResource.setPositiveVotes(sessionResource.getPositiveVotes() + 1);
                }
            });
        }

        Set<Interest> interests = session.getInterests();
        if(!interests.isEmpty()){
            sessionResource.setInterests(interests
                    .stream()
                    .map(Interest::getName)
                    .collect(Collectors.toList()));
        }

        sessionResource.add(ControllerLinkBuilder.linkTo(SessionController.class).slash(session.getId()).withSelfRel());
        Set<Speaker> speakers = session.getSpeakers();
        speakers
                .stream()
                .forEach(s -> sessionResource.add(ControllerLinkBuilder.linkTo(MemberController.class).slash(s.getId()).withRel("speaker")));
        return sessionResource;
    }

    public long getIdSession() {
        return idSession;
    }

    public SessionResource setIdSession(long idSession) {
        this.idSession = idSession;
        return this;
    }


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getVotes() {
        return votes;
    }

    public SessionResource setVotes(int votes) {
        this.votes = votes;
        return this;
    }

    public int getPositiveVotes() {
        return positiveVotes;
    }

    public SessionResource setPositiveVotes(int positiveVotes) {
        this.positiveVotes = positiveVotes;
        return this;
    }

    public long getNbConsults() {
        return nbConsults;
    }

    public SessionResource setNbConsults(long nbConsults) {
        this.nbConsults = nbConsults;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public SessionResource setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public SessionResource setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SessionResource setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public SessionResource setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SessionResource setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIdeaForNow() {
        return ideaForNow;
    }

    public SessionResource setIdeaForNow(String ideaForNow) {
        this.ideaForNow = ideaForNow;
        return this;
    }

    public List<Long> getSpeakers() {
        return speakers;
    }

    public SessionResource setSpeakers(List<Long> speakers) {
        this.speakers = speakers;
        return this;
    }

    public List<String> getInterests() {
        return interests;
    }

    public SessionResource setInterests(List<String> interests) {
        this.interests = interests;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SessionResource that = (SessionResource) o;
        return Objects.equals(idSession, that.idSession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSession);
    }
}

package org.mixit.cesar.site.web.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.model.session.Vote;
import org.mixit.cesar.site.web.api.MemberController;
import org.mixit.cesar.site.web.api.SessionController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * Member DTO for API HATEOAS
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
    public String year;
    public String start;
    public String end;
    public List<MemberDto> speakers = new ArrayList<>();
    public List<String> interests = new ArrayList<>();

    public static <T extends Session<T>> SessionResource convert(T session) {
        SessionResource sessionResource = convertWithoutLink(session);

        sessionResource.add(ControllerLinkBuilder.linkTo(SessionController.class).slash(session.getId()).withSelfRel());
        session.getSpeakers()
                .stream()
                .forEach(s -> {
                    sessionResource.add(ControllerLinkBuilder.linkTo(MemberController.class).slash(s.getId()).withRel("speaker"));
                    sessionResource.addSpeaker(MemberDto.convert(s));
                });
        return sessionResource;
    }

    public static <T extends Session<T>> SessionResource convertWithoutLink(T session) {
        SessionResource sessionResource = new SessionResource()
                .setIdSession(session.getId())
                .setDescription(session.getDescription())
                .setFormat(session.getFormat().toString())
                .setIdeaForNow(session.getIdeaForNow())
                .setLang(session.getLang().toString())
                .setSummary(session.getSummary())
                .setTitle(session.getTitle())
                .setYear(String.valueOf(session.getEvent()!=null ? session.getEvent().getYear() : ""))
                .setNbConsults(session.getNbConsults());

        List<Vote> votes = session.getVotes();
        if (!votes.isEmpty()) {
            sessionResource.setVotes(((Long)session.getVotes().stream().distinct().count()).intValue());
            sessionResource.setPositiveVotes(session.getPositiveVotes());
        }

        Set<Interest> interests = session.getInterests();
        if (!interests.isEmpty()) {
            sessionResource.setInterests(interests
                    .stream()
                    .map(Interest::getName)
                    .collect(Collectors.toList()));
        }

        if(session.getSlot()!=null){
            sessionResource
                    .setRoom(session.getSlot().getRoom()==null ? null : session.getSlot().getRoom().getName())
                    .setStart(session.getSlot().getStart().format(DateTimeFormatter.ISO_DATE_TIME))
                    .setEnd(session.getSlot().getEnd().format(DateTimeFormatter.ISO_DATE_TIME));
        }

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

    public SessionResource setRoom(String room) {
        this.room = room;
        return this;
    }

    public String getStart() {
        return start;
    }

    public SessionResource setStart(String start) {
        this.start = start;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public SessionResource setEnd(String end) {
        this.end = end;
        return this;
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

    public List<String> getInterests() {
        return interests;
    }

    public SessionResource setInterests(List<String> interests) {
        this.interests = interests;
        return this;
    }

    public String getYear() {
        return year;
    }

    public SessionResource setYear(String year) {
        this.year = year;
        return this;
    }

    public SessionResource addSpeaker(MemberDto memberDto){
        speakers.add(memberDto);
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

package org.mixit.cesar.cfp.web.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.mixit.cesar.cfp.model.Proposal;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 09/03/16.
 */
public class ProposalDTO {

    private Long id;
    protected String format;
    private String title;
    private String summary;
    private String addedAt;
    private String level;
    private List<SpeakerDTO> speakers;
    private String lang;
    private String status;
    private List<ProposalVoteDTO> votes;

    public static ProposalDTO convert(Proposal proposal) {
        return new ProposalDTO()
                .setId(proposal.getId())
                .setAddedAt(proposal.getAddedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .setSummary(proposal.getSummary())
                .setFormat(proposal.getFormat() != null ? proposal.getFormat().toString() : "Inconnu")
                .setLang(proposal.getLang() != null ? proposal.getLang().toString() : "fr")
                .setLevel(proposal.getLevel() != null ? proposal.getLevel().toString() : "Inconnu")
                .setSpeakers(proposal
                        .getSpeakers()
                        .stream()
                        .map(SpeakerDTO::convert)
                        .collect(Collectors.toList())
                )
                .setStatus(proposal.getStatus().toString())
                .setTitle(proposal.getTitle())
                .setVotes(proposal
                        .getVotes()
                        .stream()
                        .distinct()
                        .map(ProposalVoteDTO::convert)
                        .collect(Collectors.toList())
                );
    }

    public ProposalDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public ProposalDTO setFormat(String format) {
        this.format = format;
        return this;
    }

    public ProposalDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public ProposalDTO setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public ProposalDTO setAddedAt(String addedAt) {
        this.addedAt = addedAt;
        return this;
    }

    public ProposalDTO setLevel(String level) {
        this.level = level;
        return this;
    }

    public ProposalDTO setSpeakers(List<SpeakerDTO> speakers) {
        this.speakers = speakers;
        return this;
    }

    public ProposalDTO setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public ProposalDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    public ProposalDTO setVotes(List<ProposalVoteDTO> votes) {
        this.votes = votes;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getFormat() {
        return format;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public String getLevel() {
        return level;
    }

    public List<SpeakerDTO> getSpeakers() {
        return speakers;
    }

    public String getLang() {
        return lang;
    }

    public String getStatus() {
        return status;
    }

    public List<ProposalVoteDTO> getVotes() {
        return votes;
    }
}

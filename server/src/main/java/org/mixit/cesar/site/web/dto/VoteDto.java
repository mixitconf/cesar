package org.mixit.cesar.site.web.dto;

import org.mixit.cesar.site.model.session.Vote;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 23/02/16.
 */
public class VoteDto {

    private Long idSession;
    private Boolean value;

    public static VoteDto convert(Vote vote) {
        return new VoteDto()
                .setIdSession(vote.getSession() == null ? null : vote.getSession().getId())
                .setValue(vote.getValue());
    }

    public Long getIdSession() {
        return idSession;
    }

    public VoteDto setIdSession(Long idSession) {
        this.idSession = idSession;
        return this;
    }

    public Boolean getValue() {
        return value;
    }

    public VoteDto setValue(Boolean value) {
        this.value = value;
        return this;
    }
}

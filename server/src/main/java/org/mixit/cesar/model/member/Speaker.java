package org.mixit.cesar.model.member;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.model.session.Format;
import org.mixit.cesar.model.session.Session;

@Entity
public class Speaker extends Participant<Speaker> {

    public Speaker() {
        ROLES.add(Role.Member);
        ROLES.add(Role.Participant);
        ROLES.add(Role.Speaker);
    }

    private Boolean sessionAccepted;

    /**
     * We want to distinguish the user who just purpose a ligthning talk
     */
    @Enumerated(EnumType.STRING)
    private Format sessionType;

    @ManyToMany(mappedBy = "speakers")
    public Set<Session> sessions = new HashSet<>();


    public Boolean getSessionAccepted() {
        return sessionAccepted;
    }

    public Speaker setSessionAccepted(Boolean sessionAccepted) {
        this.sessionAccepted = sessionAccepted;
        return this;
    }

    public Format getSessionType() {
        return sessionType;
    }

    public Speaker setSessionType(Format sessionType) {
        this.sessionType = sessionType;
        return this;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public Participant clearSessions() {
        this.sessions.clear();
        return this;
    }

    public Participant addSession(Session session) {
        this.sessions.add(session);
        return this;
    }

    public Participant removeSharedLink(Session session) {
        this.sessions.remove(session);
        return this;
    }

}

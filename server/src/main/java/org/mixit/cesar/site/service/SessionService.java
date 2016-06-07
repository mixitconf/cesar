package org.mixit.cesar.site.service;

import java.util.List;
import java.util.Set;

import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.repository.InterestRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 04/06/16.
 */
@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InterestRepository interestRepository;

    /**
     * A session is created when a propsal on the CFP is marked like ACCEPTED
     */
    public Session createSession(Session session) {
        session.setSessionAccepted(true).setValid(true);
        return sessionRepository.save(session);
    }

    /**
     * A session is deleted when a propsal on the CFP is marked like REJECTED
     */
    public void deleteSession(Long idSession) {
        Session session = sessionRepository.findOne(idSession);

        if (session != null) {
            sessionRepository.delete(session);
        }
    }
}

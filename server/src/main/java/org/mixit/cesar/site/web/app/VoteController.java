package org.mixit.cesar.site.web.app;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.session.Vote;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/vote")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Return a member vote for a session
     */
    @RequestMapping("/session/{idSession}/member/{idMember}")
    @JsonView(FlatView.class)
    public ResponseEntity<Vote> getVote(@PathVariable("idSession") Long idSession, @PathVariable("idMember") Long idMember) {
        return ResponseEntity
                .ok()
                .body(voteRepository.findBySessionAndMember(idSession, idMember));
    }

    /**
     * Vote for a session
     */
    @RequestMapping(method = RequestMethod.PUT)
    public void vote(@RequestBody Vote vote) {
        checkNotNull(vote.getMember().getId());
        checkNotNull(vote.getSession().getId());

        Vote votePersisted = voteRepository.findBySessionAndMember(vote.getSession().getId(), vote.getMember().getId());
        if (votePersisted == null) {
            voteRepository.save(
                    new Vote()
                            .setMember(memberRepository.findOne(vote.getMember().getId()))
                            .setSession(sessionRepository.findOne(vote.getSession().getId()))
                            .setValue(vote.getValue())
            );
        }
        else {
            votePersisted.setValue(vote.getValue());
        }
    }


}
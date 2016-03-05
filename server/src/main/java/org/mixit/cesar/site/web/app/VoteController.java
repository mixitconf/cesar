package org.mixit.cesar.site.web.app;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.session.Vote;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.VoteRepository;
import org.mixit.cesar.site.web.dto.VoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/vote")
@Transactional
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Return a member vote for a session
     */
    @Authenticated
    @RequestMapping("/session/{idSession}")
    @JsonView(FlatView.class)
    public ResponseEntity<Vote> getVote(@PathVariable("idSession") Long idSession) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        return ResponseEntity
                .ok()
                .body(voteRepository.findBySessionAndMember(idSession, currentUser.getCredentials().get().getIdMember()));
    }

    /**
     * Vote for a session
     */
    @Authenticated
    @RequestMapping(method = RequestMethod.POST)
    public void vote(@RequestBody VoteDto vote) {
        checkNotNull(vote.getIdSession());

        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        Long idMember = currentUser.getCredentials().get().getIdMember();

        Vote votePersisted = voteRepository.findBySessionAndMember(vote.getIdSession(), idMember);
        if (votePersisted == null) {
            voteRepository.save(
                    new Vote()
                            .setMember(memberRepository.findOne(idMember))
                            .setSession(sessionRepository.findOne(vote.getIdSession()))
                            .setValue(vote.getValue())
            );
        }
        else {
            votePersisted.setValue(vote.getValue());
        }
    }


}
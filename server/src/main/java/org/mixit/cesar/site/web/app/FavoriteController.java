package org.mixit.cesar.site.web.app;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.session.Favorite;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.repository.FavoriteRepository;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * In the public API we expose only accepted sessions
 */
@Api(value = "Favorite sessions",
        description = "A connected user can define his favorites and see the marked sessions in the planning")
@RestController
@RequestMapping("/app/favorite")
@Transactional
public class FavoriteController {

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    EventService eventService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping
    @Authenticated
    public ResponseEntity<List<Long>> getFavorites() {
        Member member = applicationContext.getBean(CurrentUser.class).getCredentials().get().getMember();
        return
                ResponseEntity.ok(favoriteRepository
                        .findByMember(member)
                        .stream()
                        .map(f -> f.getSession().getId())
                        .collect(Collectors.toList())
                );
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.PUT)
    @Authenticated
    public void toggleFavorite(@PathVariable("sessionId") Long sessionId) {
        Member member = applicationContext.getBean(CurrentUser.class).getCredentials().get().getMember();
        Session session = sessionRepository.findOne(sessionId);

        if (session == null) {
            throw new IllegalArgumentException("Session does not exist");
        }

        Favorite favorite = favoriteRepository.findByMemberAndSession(member, session);

        if (favorite == null) {
            favoriteRepository.save(new Favorite().setMember(member).setSession(session));
        }
        else {
            favoriteRepository.delete(favorite);
        }
    }

}
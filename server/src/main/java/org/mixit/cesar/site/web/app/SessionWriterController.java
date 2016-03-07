package org.mixit.cesar.site.web.app;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_LIGHTNINGTALK;
import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_SPEAKER_LT;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.security.service.exception.ForbiddenException;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.session.Format;
import org.mixit.cesar.site.model.session.LightningTalk;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.VoteRepository;
import org.mixit.cesar.site.service.EventService;
import org.mixit.cesar.site.web.dto.SessionResource;
import org.mixit.cesar.site.web.dto.VoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to return the parameters used in the CFP
 */
@RestController
@RequestMapping("/app/session")
@Transactional
public class SessionWriterController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    CacheManager cacheManager;

    @RequestMapping(value = "/mylightnings")
    @Authenticated
    public List<SessionResource> getMyLightningsInfo() {
        Member currentUser = applicationContext.getBean(CurrentUser.class).getCredentials().get().getMember();
        return sessionRepository
                .findAllMyLightningTalks(EventService.getCurrent().getId(), currentUser.getId())
                .stream()
                .map(s -> SessionResource.convert(s))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/lightnings/votes")
    @Authenticated
    public List<VoteDto> getLightningsVotes() {
        Member currentUser = applicationContext.getBean(CurrentUser.class).getCredentials().get().getMember();
        return voteRepository.findByMember(currentUser)
                .stream()
                .filter(v -> v.getSession().getFormat().equals(Format.LightningTalk))
                .filter(v -> v.getSession().getEvent().equals(EventService.getCurrent()))
                .map(v -> VoteDto.convert(v))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @Authenticated
    public SessionResource save(@RequestBody SessionResource lightning) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        LightningTalk lightningTalkSaved;

        if (lightning.getIdSession() > 0) {
            lightningTalkSaved = (LightningTalk) sessionRepository.findOne(lightning.getIdSession());
            lightningTalkSaved
                    .setLang(lightning.getLang().equals("fr") ? SessionLanguage.fr : SessionLanguage.en)
                    .setDescription(lightning.getDescription())
                    .setSummary(lightning.getSummary())
                    .setTitle(lightning.getTitle());
        }
        else {
            lightningTalkSaved = sessionRepository.save(
                    new LightningTalk()
                            .setFormat(Format.LightningTalk)
                            .setEvent(EventService.getCurrent())
                            .setLang(lightning.getLang().equals("en") ? SessionLanguage.en : SessionLanguage.fr)
                            .setDescription(lightning.getDescription())
                            .setSummary(lightning.getSummary())
                            .setTitle(lightning.getTitle())
                            .addSpeaker(currentUser.getCredentials().get().getMember())
                            .setSessionAccepted(true)
            );
        }
        cacheManager.getCache(CACHE_LIGHTNINGTALK).clear();
        cacheManager.getCache(CACHE_SPEAKER_LT).clear();
        return SessionResource.convert(lightningTalkSaved);
    }


    @RequestMapping(value = "/{idSession}", method = RequestMethod.DELETE)
    @Authenticated
    public <T extends Session<T>> void delete(@PathVariable(value = "idSession") Long idSession) {
        Account currentUser = applicationContext.getBean(CurrentUser.class).getCredentials().get();

        Session session = sessionRepository.findOne(idSession);

        if(currentUser.getRoles().contains("ADMIN") ||
                (session.getFormat().equals(Format.LightningTalk) && session.containsSpeaker(currentUser.getMember().getId()))){
            sessionRepository.delete(session);
        }
        else {
            throw new ForbiddenException();
        }
    }

}
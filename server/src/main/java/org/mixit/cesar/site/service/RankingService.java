package org.mixit.cesar.site.service;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_RANKING;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.web.dto.SessionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Service
public class RankingService {

    private static final Logger logger = LoggerFactory.getLogger(RankingService.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * The messaging template allowing to broadcast the session ranking
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Object getSessionRanking(Integer year) {
        return sessionRepository
                .findAllSessionsForRanking(eventService.getEvent(year))
                .stream()
                .filter(s -> !s.getVotes().isEmpty())
                .map(SessionResource::convertWithoutLink)
                .collect(Collectors.groupingBy(SessionResource::getFormat));
    }

    @Scheduled(fixedDelay=10000)
    public void clearCache() {
        logger.info("Session ranking refresh... [start] ");
        cacheManager.getCache(CACHE_RANKING).clear();
        messagingTemplate.convertAndSend("/topic", getSessionRanking(null));
        logger.info("Session ranking refresh... [end] ");
    }
}
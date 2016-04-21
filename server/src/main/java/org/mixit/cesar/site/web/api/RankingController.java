package org.mixit.cesar.site.web.api;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_RANKING;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mixit.cesar.site.config.CesarCacheConfig;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.service.EventService;
import org.mixit.cesar.site.web.dto.SessionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * In the public API we expose only accepted sessions
 */
@Api(value = "Sessions ranking",
        description = "Attendees can vote for the sessions")
@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping
    @ApiOperation(value = "Finds the ranking by votes of the better sessions", httpMethod = "GET")
    public Object getSessionRanking(
            @ApiParam(required = false, name = "year", value = "Year if null return data for current year")
            @RequestParam(required = false) Integer year
    ) {
        return sessionRepository
                .findAllSessionsForRanking(eventService.getEvent(year))
                .stream()
                .filter(s -> !s.getVotes().isEmpty())
                .map(SessionResource::convert)
                .collect(Collectors.groupingBy(SessionResource::getFormat));
    }

    @Scheduled(fixedDelay=10000)
    public void clearCache() {
        cacheManager.getCache(CACHE_RANKING).clear();
    }
}
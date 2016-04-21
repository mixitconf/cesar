package org.mixit.cesar.site.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mixit.cesar.site.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RankingService rankingService;

    @RequestMapping
    @ApiOperation(value = "Finds the ranking by votes of the better sessions", httpMethod = "GET")
    public Object getSessionRanking(
            @ApiParam(required = false, name = "year", value = "Year if null return data for current year")
            @RequestParam(required = false) Integer year
    ) {
        return rankingService.getSessionRanking(year);
    }

}
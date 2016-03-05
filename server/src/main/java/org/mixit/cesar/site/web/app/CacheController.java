package org.mixit.cesar.site.web.app;

import static org.mixit.cesar.security.model.Role.ADMIN;

import java.util.Arrays;
import java.util.List;

import org.mixit.cesar.security.service.autorisation.NeedsRole;
import org.mixit.cesar.site.config.CesarCacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/cache")
@Transactional
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @NeedsRole(ADMIN)
    @RequestMapping
    public List<String> getCahcheRegion() {
        return Arrays.asList(
                CesarCacheConfig.CACHE_ARTICLE,
                CesarCacheConfig.CACHE_MEMBER,
                CesarCacheConfig.CACHE_SPONSOR,
                CesarCacheConfig.CACHE_SECURITY,
                CesarCacheConfig.CACHE_SESSION,
                CesarCacheConfig.CACHE_PLANNING);
    }

    @RequestMapping(value = "/{area}", method = RequestMethod.DELETE)
    @NeedsRole(ADMIN)
    public ResponseEntity delete(@PathVariable(value = "area") String area) {
        cacheManager.getCache(area).clear();
        return ResponseEntity.ok().build();
    }
}
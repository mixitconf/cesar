package org.mixit.cesar.site.web.app;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_ARTICLE;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.config.CesarCacheConfig;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.article.Article;
import org.mixit.cesar.site.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/article")
@Transactional
public class ArticleWriterController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CacheManager cacheManager;

    @RequestMapping(method = RequestMethod.POST)
    @JsonView(FlatView.class)
    public Article save(@RequestBody Article article) {

        Article articleSaved;

        if (article.getId() != null) {
            articleSaved = articleRepository.findOne(article.getId());
            articleSaved
                    .setContent(article.getContent())
                    .setHeadline(article.getHeadline())
                    .setTitle(article.getTitle())
                    .setValid(true);
        } else {
            articleSaved = article;
        }

        //TODO something cleaner
        cacheManager.getCache(CACHE_ARTICLE).clear();
        return articleRepository.save(articleSaved);
    }


}
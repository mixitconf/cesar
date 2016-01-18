package org.mixit.cesar.site.web.app;

import static org.mixit.cesar.security.model.Role.ADMIN;
import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_ARTICLE;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.security.service.autorisation.NeedsRole;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.article.Article;
import org.mixit.cesar.site.repository.ArticleRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping()
    @JsonView(FlatView.class)
    @NeedsRole(ADMIN)
    public List<Article> getAllArticle() {
        return StreamSupport
                .stream(articleRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    @RequestMapping("/{id}")
    @JsonView(FlatView.class)
    @NeedsRole(ADMIN)
    public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
        articleRepository.findArticleById(id);
        return ResponseEntity
                .ok()
                .body(articleRepository.findArticleById(id));
    }


    @RequestMapping(method = RequestMethod.POST)
    @JsonView(FlatView.class)
    @Authenticated
    @NeedsRole(ADMIN)
    public Article save(@RequestBody Article article) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        Article articleSaved;

        if (article.getId() != null) {
            articleSaved = articleRepository.findOne(article.getId());
            articleSaved
                    .setContent(article.getContent())
                    .setHeadline(article.getHeadline())
                    .setTitle(article.getTitle())
                    .setValid(article.isValid());
        }
        else {
            articleSaved = article.setAuthor(memberRepository.findOneStaff(currentUser.getCredentials().get().getMember().getId()));
        }

        //TODO something cleaner
        articleSaved = articleRepository.save(articleSaved);
        cacheManager.getCache(CACHE_ARTICLE).clear();

        return articleSaved;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @NeedsRole(ADMIN)
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {
        articleRepository.delete(id);
        cacheManager.getCache(CACHE_ARTICLE).clear();
        return ResponseEntity.ok().build();
    }

}
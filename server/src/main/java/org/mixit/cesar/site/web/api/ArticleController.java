package org.mixit.cesar.site.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.article.Article;
import org.mixit.cesar.site.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Articles",
        description = "You can display the list of the different articles displayed in the Mix-IT blog")
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/{id}")
    @ApiOperation(value = "Finds one article", httpMethod = "GET")
    @JsonView(FlatView.class)
    public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
        Article result;
        if (isCurrentUserAdmin()) {
            result = articleRepository.findArticleById(id);
        } else {
            result = articleRepository.findPublishedArticleById(id);
        }

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(result);
    }

    private boolean isCurrentUserAdmin() {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);

        if (currentUser == null || !currentUser.getCredentials().isPresent()) {
            return false;
        }
        return currentUser.getCredentials().get().getMember().getROLES().contains(Role.ADMIN);
    }

    @RequestMapping
    @ApiOperation(value = "Finds all articles", httpMethod = "GET")
    @JsonView(FlatView.class)
    public List<Article> getAllArticle() {
        return articleRepository.findAllPublishedArticle();
    }


}
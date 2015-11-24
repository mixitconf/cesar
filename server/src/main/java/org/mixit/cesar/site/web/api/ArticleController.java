package org.mixit.cesar.site.web.api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.article.Article;
import org.mixit.cesar.site.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Articles",
        description = "You can display the list of the different articles displayed in the Mix-IT blog")
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @RequestMapping("/{id}")
    @ApiOperation(value = "Finds one article", httpMethod = "GET")
    @JsonView(FlatView.class)
    public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(4, TimeUnit.DAYS))
                .body(articleRepository.findArticleById(id));
    }

    @RequestMapping
    @ApiOperation(value = "Finds all articles", httpMethod = "GET")
    @JsonView(FlatView.class)
    public List<Article> getAllArticle() {
        return  articleRepository.findAllPublishedArticle();
    }


}
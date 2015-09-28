package org.mixit.cesar.web.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.model.FlatView;
import org.mixit.cesar.model.article.Article;
import org.mixit.cesar.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(articleRepository.findArticleById(id), HttpStatus.OK);
    }

    @RequestMapping
    @ApiOperation(value = "Finds all articles", httpMethod = "GET")
    @JsonView(FlatView.class)
    public List<Article> getAllArticle() {
        return  articleRepository.findAllPublishedArticle();
    }


}
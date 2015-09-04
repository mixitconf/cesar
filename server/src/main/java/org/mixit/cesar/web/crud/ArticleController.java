package org.mixit.cesar.web.crud;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.model.FlatView;
import org.mixit.cesar.model.article.Article;
import org.mixit.cesar.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crud/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @RequestMapping("/{id}")
    @JsonView(FlatView.class)
    public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
        return new ResponseEntity<>(articleRepository.findArticleById(id), HttpStatus.OK);
    }

    @RequestMapping
    @JsonView(FlatView.class)
    public List<Article> getAllArticle() {
        return  articleRepository.findAllPublishedArticle();
    }


}
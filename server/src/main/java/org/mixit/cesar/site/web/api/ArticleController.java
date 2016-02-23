package org.mixit.cesar.site.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.web.AuthenticationFilter;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.article.Article;
import org.mixit.cesar.site.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "Articles",
        description = "You can display the list of the different articles displayed in the Mix-IT blog")
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private AccountRepository accountRepository;

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
        String token = httpServletRequest.getHeader(AuthenticationFilter.TOKEN_REQUEST_HEADER_PARAM);

        if (token != null) {
            Account account = accountRepository.findByToken(token);
            return account.getMember().getROLES().contains(Role.ADMIN);
        }

        return false;
    }

    @RequestMapping
    @ApiOperation(value = "Finds all articles", httpMethod = "GET")
    @JsonView(FlatView.class)
    public List<Article> getAllArticle() {
        return articleRepository.findAllPublishedArticle();
    }


}
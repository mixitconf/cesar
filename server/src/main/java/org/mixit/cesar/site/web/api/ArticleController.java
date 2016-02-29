package org.mixit.cesar.site.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.article.Article;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.site.repository.ArticleRepository;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(value = "Articles",
        description = "You can display the list of the different articles displayed in the Mix-IT blog")
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    public static final String MIX_IT_ARTICLE = "/article/";
    public static final String MIX_IT_ARTICLES = "/articles";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AbsoluteUrlFactory absoluteUrlFactory;

    @RequestMapping("/{id}")
    @ApiOperation(value = "Finds one article", httpMethod = "GET")
    @JsonView(FlatView.class)
    public ResponseEntity<Article> getArticlesFeed(@PathVariable("id") Long id) {
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

    @RequestMapping("/rss/fr_FR")
    @ApiOperation(value = "Articles RSS (fr)", httpMethod = "GET")
    @JsonView(FlatView.class)
    public String getArticlesRssFr() throws IOException, FeedException {
        return getArticlesFeed(SessionLanguage.fr);
    }

    @RequestMapping("/rss/us_US")
    @ApiOperation(value = "Articles RSS (en)", httpMethod = "GET")
    @JsonView(FlatView.class)
    public String getArticlesRssEn() throws IOException, FeedException {
        return getArticlesFeed(SessionLanguage.en);
    }

    private String getArticlesFeed(SessionLanguage language) throws IOException, FeedException {
        String baseUrl = absoluteUrlFactory.getBaseUrl();

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");

        feed.setTitle("Mix-IT articles");
        feed.setLink(baseUrl + MIX_IT_ARTICLES);
        feed.setDescription(" ");

        List<Article> articles = articleRepository.findAllPublishedArticle();

        List<SyndEntry> entries = new ArrayList<>();
        feed.setEntries(entries);

        for (Article article : articles) {
            SyndEntry entry = article.buildRssEntry(baseUrl, language);
            entries.add(entry);
        }

        SyndFeedOutput output = new SyndFeedOutput();
        return output.outputString(feed);
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
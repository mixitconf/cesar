package org.mixit.cesar.repository;

import java.util.List;

import org.mixit.cesar.model.article.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * {@link org.mixit.cesar.model.article.Article}
 */
public interface ArticleRepository extends CrudRepository<Article, Long> {

    @Query(value = "SELECT a FROM Article a where a.valid=true")
    List<Article> findAllPublishedArticle();

    @Query(value = "SELECT a FROM Article a left join fetch a.author aut left join a.comments c left join c.member where a.id=:idArticle and a.valid=true")
    Article findArticleById(@Param("idArticle") Long idArticle);
}

package org.mixit.cesar.site.repository;

import org.mixit.cesar.site.model.article.ArticleComment;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link ArticleComment}
 */
public interface ArticleCommentRepository extends CrudRepository<ArticleComment, Long> {

}

package org.mixit.cesar.repository;

import org.mixit.cesar.model.article.ArticleComment;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link ArticleComment}
 */
public interface ArticleCommentRepository extends CrudRepository<ArticleComment, Long> {

}

package org.mixit.cesar.model.article;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.model.FlatView;
import org.mixit.cesar.model.member.Member;

/**
 * A comment on e session talk.
 */
@Entity
@Table(name = "ARTICLECOMMENT")
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    @ManyToOne
    public Article article;

    @ManyToOne(optional = false)
    @JsonView(FlatView.class)
    public Member member;

    /**
     * Markdown enabled
     */
    @Lob
    @NotNull
    @JsonView(FlatView.class)
    public String content;

    @NotNull
    @JsonView(FlatView.class)
    public Instant postedAt = Instant.now();

    public Long getId() {
        return id;
    }

    public ArticleComment setId(Long id) {
        this.id = id;
        return this;
    }

    public Article getArticle() {
        return article;
    }

    public ArticleComment setArticle(Article article) {
        this.article = article;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public ArticleComment setMember(Member member) {
        this.member = member;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleComment setContent(String content) {
        this.content = content;
        return this;
    }

    public Instant getPostedAt() {
        return postedAt;
    }

    public ArticleComment setPostedAt(Instant postedAt) {
        this.postedAt = postedAt;
        return this;
    }


}

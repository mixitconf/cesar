package org.mixit.cesar.site.model.article;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Type;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.member.Staff;

/**
 * An article, i.e. a blog post
 */
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    @ManyToOne(optional = false)
    @JsonView(FlatView.class)
    public Staff author;

    @Column
    @JsonView(FlatView.class)
    @Type(type="org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    public LocalDateTime postedAt = LocalDateTime.now();

    @Size(max = 100)
    @JsonView(FlatView.class)
    public String title;

    /**
     * Markdown enabled
     */
    @Size(max = 255)
    @JsonView(FlatView.class)
    public String headline;

    /**
     * Markdown enabled
     */
    @Lob
    @JsonView(FlatView.class)
    public String content;

    @Size(max = 100)
    @JsonView(FlatView.class)
    public String titre;

    /**
     * Markdown enabled
     */
    @Size(max = 255)
    @JsonView(FlatView.class)
    public String resume;

    /**
     * Markdown enabled
     */
    @Lob
    @JsonView(FlatView.class)
    public String contenu;

    /**
     * True if Article validated : publicly visible
     */
    @JsonView(FlatView.class)
    public boolean valid;

    /**
     * Eventual comments
     */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    @JsonView(FlatView.class)
    List<ArticleComment> comments = new ArrayList<>();

    /**
     * Number of consultation
     */
    @JsonView(FlatView.class)
    public long nbConsults;


    public Long getId() {
        return id;
    }

    public Article setId(Long id) {
        this.id = id;
        return this;
    }

    public Staff getAuthor() {
        return author;
    }

    public Article setAuthor(Staff author) {
        this.author = author;
        return this;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public Article setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Article setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getHeadline() {
        return headline;
    }

    public Article setHeadline(String headline) {
        this.headline = headline;
        return this;
    }


    public String getContent() {
        return content;
    }

    public Article setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTitre() {
        return titre;
    }

    public Article setTitre(String titre) {
        this.titre = titre;
        return this;
    }

    public String getResume() {
        return resume;
    }

    public Article setResume(String resume) {
        this.resume = resume;
        return this;
    }

    public String getContenu() {
        return contenu;
    }

    public Article setContenu(String cntenu) {
        this.contenu = cntenu;
        return this;
    }

    public boolean isValid() {
        return valid;
    }

    public Article setValid(boolean valid) {
        this.valid = valid;
        return this;
    }

    public List<ArticleComment> getComments() {
        return comments;
    }

    public Article setComments(List<ArticleComment> comments) {
        this.comments = comments;
        return this;
    }

    public long getNbConsults() {
        return nbConsults;
    }

    public Article setNbConsults(long nbConsults) {
        this.nbConsults = nbConsults;
        return this;
    }
}

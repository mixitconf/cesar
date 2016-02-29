package org.mixit.cesar.site.model.article;

import com.fasterxml.jackson.annotation.JsonView;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.hibernate.annotations.Type;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.member.Staff;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.site.web.api.ArticleController;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public SyndEntry buildRssEntry(String baseUrl, SessionLanguage language) {

        SyndEntry entry = new SyndEntryImpl();

        entry.setTitle(this.getTitle(language));
        entry.setLink(baseUrl + ArticleController.MIX_IT_ARTICLE + getId());
        entry.setPublishedDate(Timestamp.valueOf(getPostedAt()));
        SyndContent description = new SyndContentImpl();
        description.setType("text/plain");
        description.setValue(getHeadline(language));
        entry.setDescription(description);

        return entry;
    }

    private String getHeadline(SessionLanguage language) {
        if (language == SessionLanguage.en) {
            return getHeadline();
        } else {
            return getResume();
        }
    }

    private String getTitle(SessionLanguage language) {
        if (language == SessionLanguage.en) {
            return getTitle();
        } else {
            return getTitre();
        }
    }
}

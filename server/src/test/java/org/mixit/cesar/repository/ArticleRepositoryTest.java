package org.mixit.cesar.repository;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceTestConfig.class)
public class ArticleRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ArticleRepository articleRepository;

    @Before
    public void setUp() throws Exception {
        DbSetup dbSetup = new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                        DataTest.DELETE_ALL,
                        sequenceOf(
                                DataTest.INSERT_MEMBER,
                                insertInto("ARTICLE")
                                        .withGeneratedValue("id", ValueGenerators.sequence())
                                        .columns("AUTHOR_ID", "CONTENT", "POSTEDAT", "TITLE", "HEADLINE", "VALID", "NBCONSULTS")
                                        .values(1, "Content *markdown*", LocalDateTime.now(), "title", "intro", true, 1)
                                        .build(),
                                insertInto("ARTICLECOMMENT")
                                        .withGeneratedValue("id", ValueGenerators.sequence())
                                        .columns("MEMBER_ID", "CONTENT", "POSTEDAT", "ARTICLE_ID")
                                        .values(1, "Comment *markdown*", LocalDateTime.now(), 1)
                                        .build()
                        )
                )
        );
        dbSetup.launch();
    }

    @Test
    public void findAllPublishedArticle(){
        assertThat(articleRepository.findAllPublishedArticle()).hasSize(1).extracting("title").containsExactly("title");
    }

    @Test
    public void findCurrentEvent(){
        assertThat(articleRepository.findArticleById(1L).getAuthor().getFirstname()).isEqualTo("Guillaume");
    }
}
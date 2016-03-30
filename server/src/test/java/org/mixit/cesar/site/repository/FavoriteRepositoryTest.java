package org.mixit.cesar.site.repository;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixit.cesar.site.model.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 04/03/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceTestConfig.class)
public class FavoriteRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void setUp() throws Exception {
        DbSetup dbSetup = new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                        DataTest.DELETE_ALL,
                        sequenceOf(
                                DataTest.INSERT_SESSION,
                                insertInto("Favorite")
                                        .withGeneratedValue("id", ValueGenerators.sequence())
                                        .columns("MEMBER_ID", "SESSION_ID")
                                        .values(1, 1)
                                        .values(1, 2)
                                        .build()
                        )

                )
        );
        dbSetup.launch();
    }

    @Test
    public void findUserFavorite() {
        assertThat(favoriteRepository.findByMember(new Member().setId(1L))).hasSize(2);
    }

    @Test
    public void findUserFavorite_for_unknown_memnber() {
        assertThat(favoriteRepository.findByMember(new Member().setId(999L))).hasSize(0);
    }
}
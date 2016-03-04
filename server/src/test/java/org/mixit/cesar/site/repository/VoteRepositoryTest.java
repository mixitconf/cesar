package org.mixit.cesar.site.repository;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

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

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 04/03/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceTestConfig.class)
public class VoteRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private VoteRepository voteRepository;

    @Before
    public void setUp() throws Exception {
        DbSetup dbSetup = new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                        DataTest.DELETE_ALL,
                        sequenceOf(
                                DataTest.INSERT_SESSION,
                                insertInto("Vote")
                                        .withGeneratedValue("id", ValueGenerators.sequence())
                                        .columns("MEMBER_ID", "SESSION_ID", "VALUE")
                                        .values(1, 1, Boolean.FALSE)
                                        .values(1, 2, Boolean.TRUE)
                                        .build()
                        )
                )
        );
        dbSetup.launch();
    }

    @Test
    public void findCurrentEvent(){
        assertThat(voteRepository.findBySessionAndMember(1L, 1L).getValue()).isFalse();
        assertThat(voteRepository.findBySessionAndMember(2L, 1L).getValue()).isTrue();
    }
}
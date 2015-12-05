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
import org.mixit.cesar.site.model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test de {@link MemberRepository}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceTestConfig.class)
public class MemberRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void setUp() throws Exception {
        DbSetup dbSetup = new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                        DataTest.DELETE_ALL,
                        sequenceOf(
                                DataTest.INSERT_EVENT,
                                DataTest.INSERT_MEMBER,
                                insertInto("MemberEvent")
                                        .withGeneratedValue("id", ValueGenerators.sequence())
                                        .columns("MEMBER_ID", "EVENT_ID").values(1, 1).build()
                        )
                )
        );
        dbSetup.launch();
    }

    @Test
    public void shouldFindMemberByEmail(){
        assertThat(memberRepository.findByEmail("guillaume@dev-mind.fr")).isNotEmpty();
    }

    @Test
    public void shouldNotFindMemberByEmail(){
        assertThat(memberRepository.findByEmail("quelquun@dev-mind.fr")).isEmpty();
    }
}
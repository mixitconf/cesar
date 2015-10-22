package org.mixit.cesar.repository;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
                                DataTest.INSERT_MEMBER
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
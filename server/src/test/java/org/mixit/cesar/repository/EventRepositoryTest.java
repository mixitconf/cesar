package org.mixit.cesar.repository;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test de {@link EventRepository}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceTestConfig.class)
public class EventRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EventRepository eventRepository;

    @Before
    public void setUp() throws Exception {
        DbSetup dbSetup = new DbSetup(
                new DataSourceDestination(dataSource),
                sequenceOf(
                        DataTest.DELETE_ALL,
                        DataTest.INSERT_EVENT
                )
        );
        dbSetup.launch();
    }

    @Test
    public void findCurrentEvent(){
        assertThat(eventRepository.findByCurrent(true)).hasSize(1).extracting("year").containsExactly(2016);
    }

    @Test
    public void findTheFirstEdition(){
        assertThat(eventRepository.findFirstYearEdition()).isEqualTo(2014);
    }

    @Test
    public void findTheLastEdition(){
        assertThat(eventRepository.findLatestYearEdition()).isEqualTo(2016);
    }
}
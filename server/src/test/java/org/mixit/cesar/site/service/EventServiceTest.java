package org.mixit.cesar.site.service;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.repository.EventRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.BeanInitializationException;

@RunWith(JUnitParamsRunner.class)
public class EventServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;


    @Test
    public void should_throw_BeanInitializationException_when_no_current_event(){
        when(eventRepository.findByCurrent(true)).thenReturn(new ArrayList<>());
        expectedException.expect(BeanInitializationException.class);
        expectedException.expectMessage("There's no current event in database");

        eventService.initCurrentEvent();
    }

    @Test
    public void should_throw_BeanInitializationException_when_more_than_one_event(){
        when(eventRepository.findByCurrent(true)).thenReturn(Lists.newArrayList(new Event(), new Event()));
        expectedException.expect(BeanInitializationException.class);
        expectedException.expectMessage("There's more than one current event in database");

        eventService.initCurrentEvent();
    }

    @Test
    public void should_return_current_event_when_year_is_null(){
        Event current = new Event().setId(1L).setYear(2016);
        when(eventRepository.findByCurrent(true)).thenReturn(Lists.newArrayList(current));

        eventService.initBean();

        assertThat(eventService.getEvent(null)).isEqualTo(current);
    }

    protected Object[] validEvent() {
        return $($(2014), $(2015), $(2016));
    }

    @Parameters(method = "validEvent")
    @Test
    public void should_return_event_when_year_is_valid(int year){
        when(eventRepository.findFirstYearEdition()).thenReturn(2014);
        when(eventRepository.findLatestYearEdition()).thenReturn(2016);
        when(eventRepository.findByYear(year)).thenReturn(new Event());

        eventService.initMinMaxEvent();

        assertThat(eventService.getEvent(year)).isNotNull();
    }

    protected Object[] invalidEvent() {
        return $($(2013), $(2017));
    }

    @Parameters(method = "invalidEvent")
    @Test
    public void should_throw_IllegalArgumentException_when_year_is_valid(int year){
        when(eventRepository.findFirstYearEdition()).thenReturn(2014);
        when(eventRepository.findLatestYearEdition()).thenReturn(2016);
        when(eventRepository.findByYear(year)).thenReturn(new Event());
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("No event was found for the year " + year);

        eventService.initMinMaxEvent();

        assertThat(eventService.getEvent(year)).isNotNull();
    }
}
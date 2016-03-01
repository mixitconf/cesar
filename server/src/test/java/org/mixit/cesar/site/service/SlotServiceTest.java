package org.mixit.cesar.site.service;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.planning.Room;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.model.session.Format;
import org.mixit.cesar.site.model.session.Talk;
import org.mixit.cesar.site.model.session.Workshop;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.SlotRepository;
import org.mixit.cesar.site.web.dto.SlotDto;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 26/02/16.
 */
public class SlotServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private SlotService slotService;

    private Event event = new Event().setId(2L);

    @Test(expected = SlotOverlapException.class)
    public void should_throw_exception_when_slots_overlap_with_start_date(){
        SlotDto slotDto = new SlotDto()
                .setId(null)
                .setRoom(Room.Amphi1)
                .setStart("2016-04-21 10:30:00")
                .setIdSession(1L);

        when(sessionRepository.findOne(1L)).thenReturn(new Talk().setFormat(Format.Talk));
        when(eventService.getEvent(2016)).thenReturn(event);
        when(slotRepository.findAllSlotsByRoom(event.getId(), Room.Amphi1)).thenReturn(
                Arrays.asList(
                    new Slot()
                            .setId(1L)
                            .setRoom(Room.Amphi1)
                            .setStart(LocalDateTime.of(2016, 4, 21, 9, 30))
                            .setEnd(LocalDateTime.of(2016, 4, 21, 10, 45)
                )
        ));

        slotService.save(slotDto);
    }

    @Test(expected = SlotOverlapException.class)
    public void should_throw_exception_when_slots_overlap_with_end_date(){
        SlotDto slotDto = new SlotDto()
                .setId(1L)
                .setRoom(Room.Amphi1)
                .setStart("2016-04-21 10:30:00")
                .setIdSession(1L);

        //This a talk so endTime will be 11:20
        when(slotRepository.findOne(1L)).thenReturn(slotDto.convert());
        when(sessionRepository.findOne(1L)).thenReturn(new Talk().setFormat(Format.Talk));
        when(eventService.getEvent(2016)).thenReturn(event);
        when(slotRepository.findAllSlotsByRoom(event.getId(), Room.Amphi1)).thenReturn(
                Arrays.asList(
                        new Slot()
                                .setId(2L)
                                .setRoom(Room.Amphi1)
                                .setStart(LocalDateTime.of(2016, 4, 21, 10, 40))
                                .setEnd(LocalDateTime.of(2016, 4, 21, 11, 21)
                                )
                ));

        slotService.save(slotDto);
    }

    @Test(expected = SlotOverlapException.class)
    public void should_throw_exception_when_slots_overlap(){
        SlotDto slotDto = new SlotDto()
                .setId(1L)
                .setRoom(Room.Amphi1)
                .setStart("2016-04-21 10:50:00")
                .setIdSession(1L);

        //This a talk so endTime will be 11:20
        when(slotRepository.findOne(1L)).thenReturn(slotDto.convert());
        when(sessionRepository.findOne(1L)).thenReturn(new Workshop().setFormat(Format.Workshop));
        when(eventService.getEvent(2016)).thenReturn(event);
        when(slotRepository.findAllSlotsByRoom(event.getId(), Room.Amphi1)).thenReturn(
                Arrays.asList(
                        new Slot()
                                .setId(2L)
                                .setRoom(Room.Amphi1)
                                .setStart(LocalDateTime.of(2016, 4, 21, 10, 50))
                                .setEnd(LocalDateTime.of(2016, 4, 21, 11, 10)
                                )
                ));

        slotService.save(slotDto);
    }

    @Test
    public void should_save_when_overlapped_slot_is_itself(){
        SlotDto slotDto = new SlotDto()
                .setId(1L)
                .setRoom(Room.Amphi1)
                .setStart("2016-04-21 10:30:00")
                .setIdSession(1L);

        //This a talk so endTime will be 11:20
        when(slotRepository.findOne(1L)).thenReturn(slotDto.convert());
        when(sessionRepository.findOne(1L)).thenReturn(new Talk().setFormat(Format.Talk));
        when(eventService.getEvent(2016)).thenReturn(event);
        when(slotRepository.findAllSlotsByRoom(event.getId(), Room.Amphi1)).thenReturn(
                Arrays.asList(
                        new Slot()
                                .setId(1L)
                                .setRoom(Room.Amphi1)
                                .setStart(LocalDateTime.of(2016, 4, 21, 10, 40))
                                .setEnd(LocalDateTime.of(2016, 4, 21, 11, 21)
                                )
                ));
        when(slotRepository.save(any(Slot.class))).thenReturn(new Slot());
        assertThat(slotService.save(slotDto)).isNotNull();
    }

    @Test
    public void should_save_when_start_date_just_after_slot(){
        SlotDto slotDto = new SlotDto()
                .setRoom(Room.Amphi1)
                .setLabel("test")
                .setStart("2016-04-21 10:30:00")
                .setEnd("2016-04-21 10:50:00");

        when(eventService.getEvent(2016)).thenReturn(event);
        when(slotRepository.findAllSlotsByRoom(event.getId(), Room.Amphi1)).thenReturn(
                Arrays.asList(
                        new Slot()
                                .setId(1L)
                                .setRoom(Room.Amphi1)
                                .setStart(LocalDateTime.of(2016, 4, 21, 9, 40))
                                .setEnd(LocalDateTime.of(2016, 4, 21, 10, 30)
                                )
                ));
        when(slotRepository.save(any(Slot.class))).thenReturn(new Slot());
        assertThat(slotService.save(slotDto)).isNotNull();
    }

    @Test
    public void should_save_when_end_date_justbefore_slot(){
        SlotDto slotDto = new SlotDto()
                .setRoom(Room.Amphi1)
                .setLabel("test")
                .setStart("2016-04-21 10:30:00")
                .setEnd("2016-04-21 10:50:00");

        when(eventService.getEvent(2016)).thenReturn(event);
        when(slotRepository.findAllSlotsByRoom(event.getId(), Room.Amphi1)).thenReturn(
                Arrays.asList(
                        new Slot()
                                .setId(1L)
                                .setRoom(Room.Amphi1)
                                .setStart(LocalDateTime.of(2016, 4, 21, 10, 50))
                                .setEnd(LocalDateTime.of(2016, 4, 21, 11, 30)
                                )
                ));
        when(slotRepository.save(any(Slot.class))).thenReturn(new Slot());
        assertThat(slotService.save(slotDto)).isNotNull();
    }
}
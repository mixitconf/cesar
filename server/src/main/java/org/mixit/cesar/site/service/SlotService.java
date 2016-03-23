package org.mixit.cesar.site.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.model.session.Session;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.SlotRepository;
import org.mixit.cesar.site.web.dto.SlotDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EventService eventService;

    /**
     * Saves a new slot. Before the saving we need to verify that the slot don't overlap another
     */
    public Slot save(SlotDto slotDto) {
        Slot slot;
        Session session = null;
        Event event = eventService.getEvent(slotDto.getStartDate().getYear());

        if (slotDto.getId() != null) {
            slot = slotRepository.findOne(slotDto.getId());
            slot
                    .setLabel(slotDto.getLabel())
                    .setRoom(slotDto.getRoom())
                    .setEnd(slotDto.getEndDate())
                    .setStart(slotDto.getStartDate());
        }
        else{
            slot = slotDto.convert();
        }
        slot.setEvent(event);
        if (slotDto.getIdSession() != null) {
            session = sessionRepository.findOne(slotDto.getIdSession());
            slot.setSession(session);
            slot.setEnd(LocalDateTime.from(slot.getStart()).plusMinutes(slot.getSession().getFormat().getDuration()));
        }

        //We need to verify the dates
        List<Slot> slots  = slotRepository.findAllSlotsByRoom(event.getId(), slot.getRoom());

        Optional<Slot> concurrent  = slots
                .stream()
                //We don't want our slot
                .filter(s -> !s.getId().equals(slot.getId()))
                //We compare the dates
                .filter(s ->
                        (slot.getStart().isAfter(s.getStart()) && slot.getStart().isBefore(s.getEnd())) ||
                                (slot.getStart().isEqual(s.getStart())) ||
                                (slot.getEnd().isEqual(s.getEnd())) ||
                                (slot.getEnd().isAfter(s.getStart()) && slot.getEnd().isBefore(s.getEnd()))
                )
                .findAny();

        if(concurrent.isPresent()){
            throw new SlotOverlapException();
        }

        Slot slotPersisted = slotRepository.save(slot);
        if (session != null) {
            session.setSlot(slot);
        }
        return slotPersisted;
    }

    /**
     * Deletes a slot
     */
    public void delete(Long id) {
        Slot slot = slotRepository.findOne(id);
        if(slot !=null){
            slot.getSession().setSlot(null);
        }
        slotRepository.delete(slot);
    }
}

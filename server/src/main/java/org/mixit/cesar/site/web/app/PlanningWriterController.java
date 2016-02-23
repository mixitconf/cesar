package org.mixit.cesar.site.web.app;

import static org.mixit.cesar.security.model.Role.ADMIN;
import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_PLANNING;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.security.service.autorisation.NeedsRole;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.SlotRepository;
import org.mixit.cesar.site.web.dto.SlotDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/planning")
@Transactional
public class PlanningWriterController {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    CacheManager cacheManager;

    @RequestMapping(method = RequestMethod.POST)
    @JsonView(FlatView.class)
    @Authenticated
    @NeedsRole(ADMIN)
    public Slot save(@RequestBody SlotDto slotDto) {
        Slot slot;

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

        if (slotDto.getIdSession() != null) {
            slot.setSession(sessionRepository.findOne(slotDto.getIdSession()));
            slot.setEnd(LocalDateTime.from(slot.getStart()).plusMinutes(slot.getSession().getFormat().getDuration()));
        }

        slotRepository.save(slot);
        cacheManager.getCache(CACHE_PLANNING).clear();

        return slot;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @NeedsRole(ADMIN)
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {
        slotRepository.delete(id);
        cacheManager.getCache(CACHE_PLANNING).clear();
        return ResponseEntity.ok().build();
    }


}
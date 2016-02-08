package org.mixit.cesar.site.web.app;

import static org.mixit.cesar.security.model.Role.ADMIN;
import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_PLANNING;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.security.service.autorisation.NeedsRole;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.planning.Slot;
import org.mixit.cesar.site.repository.SessionRepository;
import org.mixit.cesar.site.repository.SlotRepository;
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
    public Slot save(@RequestBody Slot slot) {
        Slot slotSaved;

        if (slot.getId() != null) {
            slotSaved = slotRepository.findOne(slot.getId());
            slotSaved
                    .setLabel(slot.getLabel())
                    .setRoom(slot.getRoom())
                    .setEnd(slot.getEnd())
                    .setStart(slot.getStart());

            if (slot.getSession() != null && slot.getSession().getId() != null) {
                slotSaved.setSession(sessionRepository.findOne(slot.getSession().getId()));
            }
        }
        slotSaved = slotRepository.save(slot);

        cacheManager.getCache(CACHE_PLANNING).clear();

        return slotSaved;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @NeedsRole(ADMIN)
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {
        slotRepository.delete(id);
        cacheManager.getCache(CACHE_PLANNING).clear();
        return ResponseEntity.ok().build();
    }


}
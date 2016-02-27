package org.mixit.cesar.site.service;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.mixit.cesar.site.model.event.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CesarService {

    @Value("${build.tag}")
    private String version;

    @Value("${project.name}")
    private String name;

    public Map<String, String> getParameters() {
        Event event = EventService.getCurrent();
        ZoneId zoneId = ZoneId.of(ZoneOffset.SHORT_IDS.get("ECT"));
        Map<String, String> params = new HashMap<>();

        params.put("version", version);
        params.put("name", name);
        params.put("current", String.valueOf(event.getYear()));
        params.put("day1", event.getDay1().atZone(zoneId).toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        if(event.getDay2()!=null) {
            params.put("day2", event.getDay2().atZone(zoneId).toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return params;
    }

}
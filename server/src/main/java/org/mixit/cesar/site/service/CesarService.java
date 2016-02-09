package org.mixit.cesar.site.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CesarService {

    @Value("${build.tag}")
    private String version;

    @Value("${project.name}")
    private String name;

    public Map<String, String> getParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("version", version);
        params.put("name", name);

        return params;
    }

}
package org.mixit.cesar.site.web.api;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Application", description = "Return info on the application")
@RestController
@RequestMapping("/api/cesar")
public class CesarController {

    @Value("${build.tag}")
    private String version;

    @Value("${project.name}")
    private String name;

    @RequestMapping
    @ApiOperation(value = "Return the number version", httpMethod = "GET")
    public ResponseEntity<Map<String, String>> getVersion() {
        Map<String, String> params = new HashMap<>();
        params.put("version", version);
        params.put("name", name);

        return ResponseEntity
                .ok()
                .body(params);
    }

}
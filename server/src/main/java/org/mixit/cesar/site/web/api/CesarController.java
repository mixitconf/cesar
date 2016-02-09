package org.mixit.cesar.site.web.api;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mixit.cesar.site.service.CesarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Application", description = "Return info on the application")
@RestController
@RequestMapping("/api/cesar")
public class CesarController {

    @Autowired
    private CesarService cesarService;

    @RequestMapping
    @ApiOperation(value = "Return the app parameters", httpMethod = "GET")
    public ResponseEntity<Map<String, String>> getParameters() {
        return ResponseEntity
                .ok()
                .body(cesarService.getParameters());
    }

}
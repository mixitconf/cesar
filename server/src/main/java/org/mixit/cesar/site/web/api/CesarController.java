package org.mixit.cesar.site.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Application", description = "Return info on the application")
@RestController
@RequestMapping("/api/cesar")
public class CesarController {

    @Value("${build.tag}")
    private String version;

    @RequestMapping
    @ApiOperation(value = "Return the number version", httpMethod = "GET")
    public String getVersion() {
        return version;
    }

}
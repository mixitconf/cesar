package org.mixit.cesar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Return the server URL
 */
@Component
public class AbsoluteUrlFactory {

    @Value("${cesar.baseurl}")
    private String baseUrl;

    public String getBaseUrl(){
        return baseUrl;
    }
}

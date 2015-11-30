package org.mixit.cesar.site.web.api;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.scribe.exceptions.OAuthConnectionException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mix-IT are stored on Google Drive, this controller is a redirection to the last version
 */
@RestController
public class LeafletRedirect {

    @RequestMapping(value = "/docs/sponsor/form/{language}")
    public void sponsorForm(@PathVariable("language") String language, HttpServletResponse response) throws IOException {
        if(SessionLanguage.en.equals(language.toLowerCase())){
            response.sendRedirect("https://drive.google.com/open?id=0B43BPeaKpurvc1ZCN0xFLXJDSzA");
        }
        else{
            response.sendRedirect("https://drive.google.com/open?id=0B43BPeaKpurvc1ZCN0xFLXJDSzA");
        }
    }

    @RequestMapping(value = "/docs/sponsor/leaflet/{language}")
    public void sponsorLeaflet(@PathVariable("language") String language, HttpServletResponse response) throws IOException {
        if(SessionLanguage.en.equals(language.toLowerCase())){
            response.sendRedirect("https://drive.google.com/open?id=0B43BPeaKpurvWTNnZU9WTDdUZ28");
        }
        else{
            response.sendRedirect("https://drive.google.com/open?id=0B43BPeaKpurvWTNnZU9WTDdUZ28");
        }
    }

    @RequestMapping(value = "/docs/speaker/leaflet/{language}")
    public void speakerLeaflet(@PathVariable("language") String language, HttpServletResponse response) throws IOException {
        if(SessionLanguage.en.equals(language.toLowerCase())){
            response.sendRedirect("https://drive.google.com/open?id=0B43BPeaKpurvWTNnZU9WTDdUZ28");
        }
        else{
            response.sendRedirect("https://drive.google.com/open?id=0B43BPeaKpurvWTNnZU9WTDdUZ28");
        }
    }
}

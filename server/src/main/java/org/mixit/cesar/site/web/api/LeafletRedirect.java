package org.mixit.cesar.site.web.api;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.site.model.session.SessionLanguage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mix-IT are stored on Google Drive, this controller is a redirection to the last version
 */
@RestController
public class LeafletRedirect {

    @Value("${drive.sponsor.form.fr}")
    private String sponsorFormFr;
    @Value("${drive.sponsor.form.en}")
    private String sponsorFormEn;
    @Value("${drive.sponsor.leaflet.fr}")
    private String sponsorLeafletFr;
    @Value("${drive.sponsor.leaflet.en}")
    private String sponsorLeafletEn;
    @Value("${drive.speaker.leaflet.fr}")
    private String speakerLeafletFr;
    @Value("${drive.speaker.leaflet.en}")
    private String speakerLeafletEn;
    @Value("${drive.presse.leaflet.fr}")
    private String presseLeafletFr;
    @Value("${drive.presse.leaflet.en}")
    private String presseLeafletEn;

    @RequestMapping(value = "/docs/sponsor/form/{language}")
    public void sponsorForm(@PathVariable("language") String language, HttpServletResponse response) throws IOException {
        if ("fr".equals(language.toLowerCase())) {
            response.sendRedirect("https://drive.google.com/open?id=" + sponsorFormFr);
        }
        else {
            response.sendRedirect("https://drive.google.com/open?id=" + sponsorFormEn);
        }
    }

    @RequestMapping(value = "/docs/sponsor/leaflet/{language}")
    public void sponsorLeaflet(@PathVariable("language") String language, HttpServletResponse response) throws IOException {
        if ("fr".equals(language.toLowerCase())) {
            response.sendRedirect("https://drive.google.com/open?id=" + sponsorLeafletFr);
        }
        else {
            response.sendRedirect("https://drive.google.com/open?id=" + sponsorLeafletEn);
        }
    }

    @RequestMapping(value = "/docs/speaker/leaflet/{language}")
    public void speakerLeaflet(@PathVariable("language") String language, HttpServletResponse response) throws IOException {
        if ("fr".equals(language.toLowerCase())) {
            response.sendRedirect("https://drive.google.com/open?id=" + speakerLeafletFr);
        }
        else {
            response.sendRedirect("https://drive.google.com/open?id=" + speakerLeafletEn);
        }
    }

    @RequestMapping(value = "/docs/presse/leaflet/{language}")
    public void presseLeaflet(@PathVariable("language") String language, HttpServletResponse response) throws IOException {
        if ("fr".equals(language.toLowerCase())) {
            response.sendRedirect("https://drive.google.com/open?id=" + presseLeafletFr);
        }
        else {
            response.sendRedirect("https://drive.google.com/open?id=" + presseLeafletEn);
        }
    }
}

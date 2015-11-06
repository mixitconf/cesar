package org.mixit.cesar.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@RestController
public class Routes {

    @RequestMapping({
            "/article/{id:\\w+}",
            "/articles",
            "/authent",
            "/conduite",
            "/compte",
            "/faq",
            "/favoris",
            "/home",
            "/error",
            "/lightningtalks",
            "/member",
            "/mixit15",
            "/mixit14",
            "/mixit13",
            "/mixit12",
            "/multimedia",
            "/planning",
            "/session",
            "/speakers",
            "/sponsors",
            "/staff",
            "/talks",
            "/venir"
    })
    public String index() {
        return "forward:/";
    }
}

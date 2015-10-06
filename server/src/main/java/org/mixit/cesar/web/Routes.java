package org.mixit.cesar.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@Controller
public class Routes {

    @RequestMapping({
            "/article/{id:\\w+}",
            "/articles",
            "/authent",
            "/conduite",
            "/compte",
            "/error",
            "/faq",
            "/favoris",
            "/home",
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
        return "forward:/index.html";
    }
}

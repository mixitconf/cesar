package org.mixit.cesar.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@Controller
public class Routes {

    @RequestMapping({
            "/home",
            "/article/{id:\\w+}",
            "/articles",
            "/planning",
            "/talks",
            "/lightningtalks",
            "/session",
            "/speakers",
            "/sponsors",
            "/staff",
            "/member",
            "/multimedia",
            "/conduite",
            "/faq",
            "/venir",
            "/mixit15",
            "/mixit14",
            "/mixit13",
            "/mixit12",
            "/favoris",
            "/compte",
            "/authent"
    })
    public String index() {
        return "forward:/index.html";
    }
}

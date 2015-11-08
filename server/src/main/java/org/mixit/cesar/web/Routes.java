package org.mixit.cesar.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@RestController
public class Routes {

    @RequestMapping({
            "/account",
            "/article/{id:\\w+}",
            "/articles",
            "/authent",
            "/conduite",
            "/compte",
            "/createaccount/*",
            "/createaccountsocial",
            "/faq",
            "/favoris",
            "/home",
            "/cerror/*",
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

package org.mixit.cesar.web;

import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@Controller
public class Routes {

    @Autowired
    private AbsoluteUrlFactory urlFactory;

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
            "/valid",
            "/venir"
    })
    public String index(HttpServletResponse response) {
        return "forward:/";
    }
}

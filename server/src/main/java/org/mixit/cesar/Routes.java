package org.mixit.cesar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@Controller
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
            "/valid",
            "/venir"
    })
    public String index() {
        return "forward:/";
    }

    @RequestMapping({
            "/cfp/home",
            "/cfp/profile",
            "/cfp/talks"
    })
    public String pageCfp() {
        return "forward:/cfp/index.html";
    }

    @RequestMapping({"/cfp"})
    public String indexCfp() {
        return "redirect:/cfp/index.html";
    }
}
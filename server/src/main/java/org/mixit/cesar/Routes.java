package org.mixit.cesar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@Controller
public class Routes {

    @RequestMapping({
            "/about",
            "/account",
            "/admaccounts",
            "/admcfp",
            "/admcfptalk/{id:\\w+}",
            "/admarticle/{id:\\w+}",
            "/admarticles",
            "/admplanning",
            "/article",
            "/article/{id:\\w+}",
            "/article/{id:\\w+}/{name:[A-Za-z0-9_\\-.]+}",
            "/articles",
            "/authent",
            "/cache",
            "/cerror",
            "/cerror/{id:\\w+}",
            "/cfp",
            "/cfptalk/{type:\\w+}",
            "/codeofconduct",
            "/compte",
            "/conduite",
            "/createaccount/*",
            "/createaccountsocial",
            "/interest/{name:[A-Za-z0-9_\\-.\\s]+ }",
            "/faq",
            "/favoris",
            "/home",
            "/lightning/{id:\\w+}",
            "/lightning/{id:\\w+}/{name:[A-Za-z0-9_\\-.]+}",
            "/lightnings",
            "/login",
            "/login/{id:\\w+}",
            "/login/with/{id:\\w+}",
            "/logout",
            "/facilities",
            "/member/{type:\\w+}/{id:\\w+}",
            "/member/{type:\\w+}",
            "/mixit16",
            "/mixit15",
            "/mixit14",
            "/mixit13",
            "/mixit12",
            "/mixit16/{type:\\w+}",
            "/mixit15/{type:\\w+}",
            "/mixit14/{type:\\w+}",
            "/mixit13/{type:\\w+}",
            "/mixit12/{type:\\w+}",
            "/mixteen",
            "/monitor",
            "/multimedia",
            "/news/{id:\\w+}",
            "/news/{id:\\w+}/{title:[A-Za-z0-9_\\-.]+}",
            "/passwordlost",
            "/passwordreinit",
            "/planning",
            "/timeline",
            "/timelineqr",
            "/profile/{login:\\w+}",
            "/ranking",
            "/rankingmonit",
            "/session/{id:\\w+}",
            "/session/{id:\\w+}/{title:[A-Za-z0-9_\\-.\\s]+}",
            "/sessions",
            "/speakers",
            "/sponsor/{id:\\w+}",
            "/sponsors",
            "/statistics",
            "/staff",
            "/talks",
            "/valid",
            "/venir"
    })
    public String index() {
        return "forward:/";
    }


}
package org.mixit.cesar.site.web.app;

import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.mixit.cesar.site.service.CesarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 09/02/16.
 */

@Controller
@RequestMapping(path = "/bot")
public class BotController {

    @Autowired
    private AbsoluteUrlFactory absoluteUrlFactory;
    @Autowired
    private CesarService cesarService;


    @RequestMapping(value = {"/", "/home"})
    public String forBot(Model model) {
        model.addAllAttributes(cesarService.getParameters());
        model.addAttribute("baseurl", absoluteUrlFactory.getBaseUrl());
        model.addAttribute("name", "Mix-IT");
        model.addAttribute("description", "Mix-IT, it's 2 intense days of conferences to discover new ideas, games to learn, development or creative workshops");
        return "bot";
    }

    @RequestMapping(value = "/forward")
    public String index() {
        return "forward:/";
    }
}

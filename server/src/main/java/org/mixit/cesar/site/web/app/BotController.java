package org.mixit.cesar.site.web.app;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.mixit.cesar.site.service.CesarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 09/02/16.
 */

@Controller
@RequestMapping(path = {"/bot", "/bot/", "/bot/home"})
public class BotController {

    @Autowired
    private AbsoluteUrlFactory absoluteUrlFactory;
    @Autowired
    private CesarService cesarService;

    @Autowired
    private VelocityEngine velocityEngine;

//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView showQuizz() {
//        Map<String, String> params = cesarService.getParameters();
//        params.put("baseurl", absoluteUrlFactory.getBaseUrl());
//        params.put("name", "Mix-IT");
//        params.put("description", "Mix-IT, it's 2 intense days of conferences to discover new ideas, games to learn, development or creative workshops");
//
//        VelocityEngineUtils.mergeTemplateIntoString(
//                this.velocityEngine,
//                "bot",
//                "UTF-8",
//                null);
//
//        return new ModelAndView("bot", params);
//    }

    @RequestMapping
    public String showQuizz(Model model) {
        //Map<String, String> params =
        model.addAllAttributes(cesarService.getParameters());
        model.addAttribute("baseurl", absoluteUrlFactory.getBaseUrl());
        model.addAttribute("name", "Mix-IT");
        model.addAttribute("description", "Mix-IT, it's 2 intense days of conferences to discover new ideas, games to learn, development or creative workshops");
        //return new ModelAndView("bot", params);
        return "bot";
    }

}

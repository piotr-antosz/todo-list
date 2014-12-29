package com.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApiConfigurationController {
    @Value("${api.authentication.url}")
    private String authenticationUrl;

    @Value("${api.user.url}")
    private String userUrl;

    @Value("${api.tasks.url}")
    private String tasksUrl;

    @RequestMapping(value = "/apiConfiguration.js")
    public ModelAndView getConfiguration() {
        ModelAndView modelAndView = new ModelAndView("apiConfiguration");
        modelAndView.addObject("authenticationUrl", authenticationUrl);
        modelAndView.addObject("userUrl", userUrl);
        modelAndView.addObject("tasksUrl", tasksUrl);
        return modelAndView;
    }
}

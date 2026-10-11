package com.Shashwat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Serves the React entry point after frontend/dist is copied to static/. */
@Controller
public class SpaController {

    @GetMapping({"/", "/post/{id}"})
    public String index() {
        return "forward:/index.html";
    }
}

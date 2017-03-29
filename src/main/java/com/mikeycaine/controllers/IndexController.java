package com.mikeycaine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
    String index() {
        return "index";
    }
	
	@RequestMapping("/points")
    String points() {
        return "points";
    }
	
	@RequestMapping("/polygons")
    String polygons() {
        return "polygons";
    }
}

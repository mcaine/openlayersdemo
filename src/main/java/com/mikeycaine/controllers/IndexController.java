package com.mikeycaine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
    String index(){
        return "index";
    }
	
	@RequestMapping("/usejson")
    String usejson(){
        return "usejson";
    }
}

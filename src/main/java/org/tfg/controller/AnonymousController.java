package org.tfg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnonymousController {

	@GetMapping("/")
	public String index(ModelMap m) {
		
		return "/home/index";
	}
	
	
	
}

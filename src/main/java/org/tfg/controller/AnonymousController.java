package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;

@Controller
public class AnonymousController {

	@GetMapping("/")
	public String index(ModelMap m, HttpSession s) throws DangerException {
		return "/home/home";
	}
	
	@PostMapping("/login")
	public String loginPost(ModelMap m) {
		return "";
	}
	
	@PostMapping("/registro")
	public String registroPost() {
		return "";
	}
	
}

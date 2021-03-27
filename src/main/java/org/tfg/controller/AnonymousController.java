package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.helper.PRG;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class AnonymousController {
	
	private UsuarioRepository usuarioRepository;

	@GetMapping("/")
	public String index(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("", s);
		
		return "/home/home";
	}

	@RequestMapping("/login.do")
	public @ResponseBody String loginGet(ModelMap m) {
		return "/home/login";
	}
	
	@PostMapping("/login")
	public String loginPost(ModelMap m, HttpSession s, @RequestParam("nUsuario") String loginName, @RequestParam("pass") String pass) {
		return "/home/login";
	}


	@GetMapping("/registro")
	public String registroGet() {
		return "/home/registro";
	}

	@PostMapping("/registro")
	public String registroPost(ModelMap m, @RequestParam("nUsuario") String loginName,
			@RequestParam("pass") String pass, @RequestParam("passConfirm") String passConfirm,
			@RequestParam("email") String email, @RequestParam("nombre") String nombre,
			@RequestParam("apellidos") String apellidos, @RequestParam("fNacimiento") String fNacimiento) {

		if (usuarioRepository.getByLoginName(loginName) != null) {
			
		}
		
		
		return "/home/registro";
	}

}

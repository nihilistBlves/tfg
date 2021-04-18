package org.tfg.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.helper.PRG;
import org.tfg.repositories.UsuarioRepository;
import org.tfg.services.MailService;

@Controller
public class AnonymousController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MailService ms;

	@GetMapping("/")
	public String index(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		
		return "/home/home";
	}

	@GetMapping("/login")
	public String loginGet() {
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
	public String registroPost(ModelMap m, @RequestParam("loginName") String loginName,
			@RequestParam("password") String pass, @RequestParam("passConfirm") String passConfirm,
			@RequestParam("email") String email, /*@RequestParam("nombre") String nombre,*/
			/*@RequestParam("apellido") String apellidos,*/ @RequestParam("edad") String fNacimiento) throws DangerException {
		
		if(pass != passConfirm) {
			
		}
		if (usuarioRepository.getByLoginName(loginName) != null) {
			PRG.error("Ya existe este nombre de usuario", "/login");
		}
		if (usuarioRepository.getByEmail(email) != null) {
			PRG.error("Ya existe una cuenta con este correo electrónico", "/login");
		}
		
		
		Usuario usuario = new Usuario();
		
		usuario.setLoginName(loginName);
		usuario.setPass(pass);
		usuario.setEmail(email);
		
		LocalDate fecha= LocalDate.parse(fNacimiento);
		
		usuario.setFechaNacimiento(fecha);
		
		usuarioRepository.save(usuario);
		
		//Cargar el servicio del email
		//MailService e= new MailService();
		
		//Cabecera del email
		String cabecera="Email de prueba";
		//cuerpo del email
		String cuerpo="Hola este es un mensaje de prueba";
		
		//EnviarEmail
		ms.enviarEmail(email, cabecera, cuerpo);
		
		
		
		return "redirect:/";
	}

}

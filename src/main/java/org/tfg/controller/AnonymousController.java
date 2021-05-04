package org.tfg.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.helper.PRG;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class AnonymousController {

	@Autowired
	private UsuarioRepository usuarioRepository;

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
	public String loginPost(ModelMap m, HttpSession s, @RequestParam("loginName") String loginName,
			@RequestParam("password") String pass) throws DangerException {

		if (usuarioRepository.getByLoginName(loginName) != null) {

			m.put("nombre", "ja");

			return "/usuario/buscar";
		} else {
			PRG.error("Ya existe este nombre de usuario", "/login");
		}

		return "/home/login";
	}

	@GetMapping("/registro")
	public String registroGet() {
		return "/home/registro";
	}

	@PostMapping("/registro")
	public String registroPost(ModelMap m, @RequestParam("loginName") String loginName,
			@RequestParam("password") String pass, @RequestParam("repass") String passConfirm,
			@RequestParam("email") String email, /* @RequestParam("nombre") String nombre, */
			/* @RequestParam("apellido") String apellidos, */ @RequestParam("fechaNacimiento") String fNacimiento)
			throws DangerException {

		if (pass != passConfirm) {

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

		LocalDate fecha = LocalDate.parse(fNacimiento);

		usuario.setFechaNacimiento(fecha);

		usuarioRepository.save(usuario);

		// Cargar el servicio del email
		// MailService e= new MailService();

		// Cabecera del email
		String cabecera = "Email de prueba";
		// cuerpo del email
		// editar para crear plantilla
		String cuerpo = "<h1>Hola Dani</h1>" + "<br>" + "Te hasregistrado ypienso comerme a tu perrito<b>=D</b>";

		// EnviarEmail

		return "redirect:/";
	}

}

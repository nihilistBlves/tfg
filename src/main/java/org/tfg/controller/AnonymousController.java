package org.tfg.controller;

import java.io.File;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.tfg.services.MailService;

@Controller
public class AnonymousController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private MailService mailService;

	@GetMapping("/")
	public String index(ModelMap m, HttpSession s) throws DangerException {
		String returner = "";
		if (s.getAttribute("user") != null) {
			returner = "redirect:/feed";
		} else {
			if (s.getAttribute("loginError") != null) {
				m.put("loginError", s.getAttribute("loginError"));
			}
			returner = "home/home";
		}
		return returner;
	}

	@GetMapping("/login")
	public String loginGet() {
		return "/home/login";
	}

	@PostMapping("/login")
	public String loginPost(ModelMap m, HttpSession s, @RequestParam("loginName") String loginName,
			@RequestParam("password") String pass) throws DangerException {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String returner = "";

		if (usuarioRepository.getByLoginName(loginName) != null
				&& passwordEncoder.matches(pass, usuarioRepository.getByLoginName(loginName).getPass())) {
			Usuario usuario = usuarioRepository.getByLoginName(loginName);
			s.setAttribute("user", usuario);
			returner = "redirect:/" + loginName;
		} else {
			s.setAttribute("loginError", "El usuario no existe o la contraseña es incorrecta");
			returner = "redirect:/";
		}

		return returner;
	}

	@GetMapping("/registro")
	public String registroGet() {
		return "/home/registro";
	}

	@PostMapping("/registro")
	public String registroPost(ModelMap m, @RequestParam("loginName") String loginName,
			@RequestParam("password") String pass, @RequestParam("repass") String passConfirm,
			@RequestParam("email") String email, @RequestParam("fechaNacimiento") String fNacimiento)
			throws DangerException {

		System.out.println(pass + passConfirm);

		if (!pass.equals(passConfirm)) {
			PRG.error("Las contraseñas no coinciden", "/login");
		}
		if (usuarioRepository.getByLoginName(loginName) != null) {
//			PRG.error("Ya existe este nombre de usuario", "/login");
		}
		if (usuarioRepository.getByEmail(email) != null) {
//			PRG.error("Ya existe una cuenta asociada a este correo electrónico", "/login");
		}

		Usuario usuario = new Usuario();

		usuario.setLoginName(loginName);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		usuario.setPass(passwordEncoder.encode(pass));
		usuario.setEmail(email);

		// Añadir el rol a los usuarios/admin etc
		// usuario.setRol();

		LocalDate fecha = LocalDate.parse(fNacimiento);

		usuario.setFechaNacimiento(fecha);

		usuarioRepository.save(usuario);

		File directorio = new File("src//main//resources/static/users/"+loginName);
		File directorioPerfil = new File("src//main//resources/static/users/"+loginName+"/perfil");
		File directorioPosts = new File("src//main//resources/static/users/"+loginName+"/posts");
		File directorioPostsImgs = new File("src//main//resources/static/users/"+loginName+"/posts/imgs");
		File directorioPostsAudios = new File("src//main//resources/static/users/"+loginName+"/posts/audios");
		File directorioPostsFilms = new File("src//main//resources/static/users/"+loginName+"/posts/films");
		
		if (!directorio.exists()) {
				directorio.mkdir();
				directorioPerfil.mkdir();
				directorioPosts.mkdir();
				directorioPostsImgs.mkdir();
				directorioPostsAudios.mkdir();
				directorioPostsFilms.mkdir();
				System.out.println("Directorio creado");
			} else {
				System.out.println("Error al crear directorio");
			}
		
		
		return "redirect:/";
	}

}
package org.tfg.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.domain.Usuario;
import org.tfg.domain.VerificationToken;
import org.tfg.events.EventoVerificacion;
import org.tfg.exception.DangerException;
import org.tfg.repositories.UsuarioRepository;
import org.tfg.repositories.VerificationTokenRepository;

@Controller
public class AnonymousController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@GetMapping("/")
	public String index(ModelMap m, HttpSession s) throws DangerException {
		String returner = "";
		if (s.getAttribute("userLogged") != null) {
			returner = "redirect:/feed";
		} else {
			if (s.getAttribute("infoModal") != null) {
				m.put("infoModal", "infoModal");
				m.put("infoTitulo", s.getAttribute("infoTitulo"));
				m.put("infoTexto", s.getAttribute("infoTexto"));
				m.put("infoEstado", s.getAttribute("infoEstado"));
				s.removeAttribute("infoTitulo");
				s.removeAttribute("infoTexto");
				s.removeAttribute("infoEstado");
				s.removeAttribute("infoModal");
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

			if (!usuario.isEnabled()) {

				s.setAttribute("infoModal", "true");
				s.setAttribute("infoTitulo", "Error");
				s.setAttribute("infoTexto", "La cuenta no ha sido verificada");
				s.setAttribute("infoEstado", "btn btn-danger");

				returner = "redirect:/";
			} else {
				s.setAttribute("userLogged", usuario);
				returner = "redirect:/user/" + loginName;
			}

		} else {
			s.setAttribute("infoModal", "true");
			s.setAttribute("infoTitulo", "Error");
			s.setAttribute("infoTexto", "El usuario no existe o la contraseña es incorrecta");
			s.setAttribute("infoEstado", "btn btn-danger");

			returner = "redirect:/";
		}

		return returner;
	}

	@GetMapping("/registro")
	public String registroGet() {
		return "/home/registro";
	}

	@PostMapping("/registro")
	public String registroPost(ModelMap m, HttpSession s, @RequestParam("loginName") String loginName,
			@RequestParam("password") String pass, @RequestParam("repass") String passConfirm,

			@RequestParam("email") String email, @RequestParam("fechaNacimiento") String fNacimiento,
			HttpServletRequest request) {

		Usuario usuario = new Usuario();

		try {
			if (usuarioRepository.getByLoginName(loginName) != null) {
				s.setAttribute("infoModal", "true");
				s.setAttribute("infoTitulo", "Error");
				s.setAttribute("infoTexto", "El nombre de usuario introducido ya existe");
				s.setAttribute("infoEstado", "btn btn-danger");

				return "redirect:/";
			} else {
				usuario.setLoginName(loginName);
			}

			if (!pass.equals(passConfirm)) {
				s.setAttribute("infoModal", "true");
				s.setAttribute("infoTitulo", "Error");
				s.setAttribute("infoTexto", "Las contraseñas no coinciden");
				s.setAttribute("infoEstado", "btn btn-danger");

				return "redirect:/";
			} else {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				usuario.setPass(passwordEncoder.encode(pass));
			}

			if (usuarioRepository.getByEmail(email) != null) {
				s.setAttribute("infoModal", "true");
				s.setAttribute("infoTitulo", "Error");
				s.setAttribute("infoTexto", "El correo electrónico introducido ya está registrado");
				s.setAttribute("infoEstado", "btn btn-danger");

				return "redirect:/";
			} else {
				usuario.setEmail(email);
			}
			// Añadir el rol a los usuarios/admin etc
			// usuario.setRol();

			LocalDate fecha = LocalDate.parse(fNacimiento);

			usuario.setFechaNacimiento(fecha);

			usuarioRepository.save(usuario);

			String appUrl = request.getContextPath();

			eventPublisher.publishEvent(new EventoVerificacion(usuario, request.getLocale(), appUrl));
		} catch (Exception e) {
			// TODO: handle exception
		}
		s.setAttribute("infoModal", "true");
		s.setAttribute("infoTitulo", "Info");
		s.setAttribute("infoTexto",
				"Te has registrado correctamente! Revisa tu bandeja de entrada para activar la cuenta antes de logear por primera vez");
		s.setAttribute("infoEstado", "btn btn-success");

		return "redirect:/";
	}

	@GetMapping("/registroConfirmado")
	public String confirmarRegistro(ModelMap m, HttpSession s, @RequestParam("token") String token) {

		VerificationToken verificationToken = verificationTokenRepository.getByToken(token);

		if (verificationToken == null) {
			s.setAttribute("infoModal", "true");
			s.setAttribute("infoTitulo", "Error");
			s.setAttribute("infoTexto", "El link al que has accedido no existe.");
			s.setAttribute("infoEstado", "btn btn-danger");

			return "redirect:/";
		} else if ((verificationToken != null) && (verificationToken.getUsuario().isEnabled())) {
			s.setAttribute("infoModal", "true");
			s.setAttribute("infoTitulo", "Error");
			s.setAttribute("infoTexto", "Esta cuenta ya ha sido activada anteriormente.");
			s.setAttribute("infoEstado", "btn btn-danger");

			return "redirect:/";
		}

		Usuario usuario = verificationToken.getUsuario();
		Calendar cal = Calendar.getInstance();

		if ((verificationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
			s.setAttribute("infoModal", "true");
			s.setAttribute("infoTitulo", "Error");
			s.setAttribute("infoTexto", "El link de activación de la cuenta ha expirado.");
			s.setAttribute("infoEstado", "btn btn-danger");

			return "redirect:/";
		}

		usuario.setEnabled(true);
		usuarioRepository.save(usuario);

		File directorio = new File("src//main//resources/static/users/" + usuario.getLoginName());
		File directorioPerfil = new File("src//main//resources/static/users/" + usuario.getLoginName() + "/perfil");
		File directorioPosts = new File("src//main//resources/static/users/" + usuario.getLoginName() + "/posts");
		File directorioPostsImgs = new File(
				"src//main//resources/static/users/" + usuario.getLoginName() + "/posts/imgs");
		File directorioPostsAudios = new File(
				"src//main//resources/static/users/" + usuario.getLoginName() + "/posts/audios");
		File directorioPostsFilms = new File(
				"src//main//resources/static/users/" + usuario.getLoginName() + "/posts/films");

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

		s.setAttribute("infoModal", "true");
		s.setAttribute("infoTitulo", "Info");
		s.setAttribute("infoTexto", "La cuenta se ha activado correctamente. Ya puedes hacer login.");
		s.setAttribute("infoEstado", "btn btn-success");

		return "redirect:/";

	}

}
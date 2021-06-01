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
import org.tfg.helper.H;
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
		if (s.getAttribute("userLogged") != null) {
			return "redirect:/feed";
		} else {
			if (s.getAttribute("infoModal") != null) {
				H.mPut(m, s);
			}
		}
		return "/home/home";
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

				H.setInfoModal("Error|La cuenta no ha sido verificada|btn-hover btn-red", s);

				returner = "redirect:/";
			} else {
				s.setAttribute("userLogged", usuario);
				returner = "redirect:/feed";
			}

		} else {
			H.setInfoModal("Error|El usuario no existe o la contraseña es incorrecta|btn-hover btn-red", s);

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
				H.setInfoModal("Error|El nombre de usuario introducido ya existe|btn-hover btn-red", s);

				return "redirect:/";
			} else {
				usuario.setLoginName(loginName);
			}

			if (!pass.equals(passConfirm)) {
				H.setInfoModal("Error|Las contraseñas no coinciden|btn-hover btn-red", s);

				return "redirect:/";
			} else {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				usuario.setPass(passwordEncoder.encode(pass));
			}

			if (usuarioRepository.getByEmail(email) != null) {
				H.setInfoModal("Error|El correo electrónico introducido ya está registrado|btn-hover btn-red", s);

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
		H.setInfoModal("Info|Te has registrado correctamente! Revisa tu bandeja de entrada para activar la cuenta antes de logear por primera vez|btn-hover btn-black", s);

		return "redirect:/";
	}

	@GetMapping("/registroConfirmado")
	public String confirmarRegistro(ModelMap m, HttpSession s, @RequestParam("token") String token) {

		VerificationToken verificationToken = verificationTokenRepository.getByToken(token);

		if (verificationToken == null) {
			H.setInfoModal("Error|No existe esta página|btn btn-danger", s);

			return "redirect:/";
		} else if ((verificationToken != null) && (verificationToken.getUsuario().isEnabled())) {
			H.setInfoModal("Error|Esta cuenta ya ha sido activada anteriormente.|btn-hover btn-red", s);

			return "redirect:/";
		}

		Usuario usuario = verificationToken.getUsuario();
		Calendar cal = Calendar.getInstance();

		if ((verificationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
			H.setInfoModal("Error|El link de activación de la cuenta ha expirado.|btn-hover btn-red", s);

			return "redirect:/";
		}

		usuario.setEnabled(true);
		usuarioRepository.save(usuario);

		File directorio = new File("src//main//resources/static/users/" + usuario.getLoginName());
		File directorioPerfil = new File("src//main//resources/static/users/" + usuario.getLoginName() + "/perfil");
		File directorioPostsImgs = new File(
				"src//main//resources/static/users/" + usuario.getLoginName() + "/posts/img");
		File directorioPostsAudios = new File(
				"src//main//resources/static/users/" + usuario.getLoginName() + "/posts/audio");
		File directorioPostsFilms = new File(
				"src//main//resources/static/users/" + usuario.getLoginName() + "/posts/video");

		if (!directorio.exists()) {
			directorio.mkdirs();
			directorioPerfil.mkdirs();
			directorioPostsImgs.mkdirs();
			directorioPostsAudios.mkdirs();
			directorioPostsFilms.mkdirs();
			System.out.println("Directorio creado");
		} else {
			System.out.println("Error al crear directorio");
		}

		H.setInfoModal("Info|La cuenta se ha activado correctamente. Ya puedes hacer login.|btn-hover btn-black", s);

		return "redirect:/";

	}

}
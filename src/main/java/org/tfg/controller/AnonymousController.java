package org.tfg.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.tfg.domain.Usuario;
import org.tfg.domain.VerificationToken;
import org.tfg.events.EventoVerificacion;
import org.tfg.exception.DangerException;
import org.tfg.helper.PRG;
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
			if (!usuario.isEnabled()) {
				s.setAttribute("loginError", "La cuenta no ha sido verificada");
				returner = "redirect:/";
			} else {
				s.setAttribute("user", usuario);
				returner = "redirect:/" + loginName;
			}
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
			@RequestParam("email") String email, @RequestParam("fechaNacimiento") String fNacimiento,
			HttpServletRequest request) throws DangerException {

		if (!pass.equals(passConfirm)) {
			PRG.error("Las contraseñas no coinciden", "/login");
		}
		if (usuarioRepository.getByLoginName(loginName) != null) {
			PRG.error("Ya existe este nombre de usuario", "/login");
		}
		if (usuarioRepository.getByEmail(email) != null) {
			PRG.error("Ya existe una cuenta asociada a este correo electrónico", "/login");
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

		String appUrl = request.getContextPath();

		eventPublisher.publishEvent(new EventoVerificacion(usuario, request.getLocale(), appUrl));

		return "redirect:/";
	}

	@GetMapping("/registroConfirmado")
	public String confirmarRegistro(ModelMap m, @RequestParam("token") String token, WebRequest request) {

		Locale locale = request.getLocale();

		VerificationToken verificationToken = verificationTokenRepository.getByToken(token);

		if (verificationToken == null) {
			String message = "El link de verificacion al que has accedido no existe.";
			m.addAttribute("message", message);
			return "home/badUser";
		}

		Usuario usuario = verificationToken.getUsuario();
		Calendar cal = Calendar.getInstance();

		if ((verificationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
			String messageValue = "El link de verificacion al que has accedido ha caducado:";
			m.addAttribute("message", messageValue);
			return "home/badUser";
		}

		usuario.setEnabled(true);
		usuarioRepository.save(usuario);
		return "redirect:/login";

	}

}
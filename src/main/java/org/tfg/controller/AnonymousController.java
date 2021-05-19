package org.tfg.controller;

import java.time.LocalDate;
import java.util.Calendar;

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
		if (s.getAttribute("user") != null) {
			returner = "redirect:/feed";
		} else {
			if (s.getAttribute("infoModal") != null) {
				m.put("infoModal", s.getAttribute("infoModal"));
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
				s.setAttribute("infoModal", "La cuenta no ha sido verificada");
				returner = "redirect:/";
			} else {
				s.setAttribute("user", usuario);
				returner = "redirect:/" + loginName;
			}
		} else {
			s.setAttribute("infoModal", "El usuario no existe o la contraseña es incorrecta");
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
				s.setAttribute("infoModal", "El nombre de usuario introducido ya existe");
				return "redirect:/";
			} else {
				usuario.setLoginName(loginName);
			}

			if (!pass.equals(passConfirm)) {
				s.setAttribute("infoModal", "Las contraseñas no coinciden");
				return "redirect:/";
			} else {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				usuario.setPass(passwordEncoder.encode(pass));
			}

			if (usuarioRepository.getByEmail(email) != null) {
				s.setAttribute("infoModal", "El correo electrónico introducido ya está registrado");
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

		s.setAttribute("infoModal", "Registrado correctamente! Revisa tu bandeja de entrada para activar la cuenta antes de logear por primera vez");
		return "redirect:/";
	}

	@GetMapping("/registroConfirmado")
	public String confirmarRegistro(ModelMap m, HttpSession s, @RequestParam("token") String token) {

		VerificationToken verificationToken = verificationTokenRepository.getByToken(token);

		if (verificationToken == null) {
			s.setAttribute("infoModal", "El link al que has accedido no existe.");
			return "redirect:/";
		} else if ((verificationToken != null) && (verificationToken.getUsuario().isEnabled())) {
			s.setAttribute("infoModal", "Esta cuenta ya ha sido activada anteriormente.");
			return "redirect:/";
		}

		Usuario usuario = verificationToken.getUsuario();
		Calendar cal = Calendar.getInstance();

		if ((verificationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
			s.setAttribute("infoModal", "El link de activación de la cuenta ha expirado.");
			return "redirect:/";
		}

		usuario.setEnabled(true);
		usuarioRepository.save(usuario);
		s.setAttribute("infoModal", "La cuenta se ha activado correctamente. Ya puedes hacer login.");
		return "redirect:/";

	}

}
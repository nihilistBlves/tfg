package org.tfg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.tfg.domain.Usuario;
import org.tfg.domain.VerificationToken;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.repositories.RolRepository;
import org.tfg.repositories.UsuarioRepository;
import org.tfg.repositories.VerificationTokenRepository;
import org.tfg.service.EmailService;

@Controller
public class AnonymousController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private EmailService emailService;

	@GetMapping("/")
	public String index(ModelMap m, HttpSession s) throws DangerException {

		if (s.getAttribute("userLogged") != null) {
			return "redirect:/feed";
		} else {
			if (s.getAttribute("infoModal") != null) {
				H.mPut(m, s);
			}
		}
		return "home/home";
	}

	@GetMapping("/login")
	public String loginGet() {
		return "home/login";
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
		return "home/registro";
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
			} else if (loginName.isEmpty() || loginName.length() < 5 || loginName.length() > 25) {
				H.setInfoModal("Error|El nombre de usuario debe contener de 5 a 25 caracteres|btn-hover btn-red", s);
				return "redirect:/";
			} else {
				usuario.setLoginName(loginName);
			}

			if (!pass.equals(passConfirm) || pass.isEmpty() || passConfirm.isEmpty()) {
				H.setInfoModal("Error|Las contraseñas no coinciden|btn-hover btn-red", s);
				return "redirect:/";
			} else {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				usuario.setPass(passwordEncoder.encode(pass));
			}

			if (usuarioRepository.getByEmail(email) != null) {
				H.setInfoModal("Error|El correo electrónico introducido ya está en uso|btn-hover btn-red", s);
				return "redirect:/";
			} else if (email.isEmpty()) {
				H.setInfoModal("Error|El correo electrónico introducido ya está en uso|btn-hover btn-red", s);
				return "redirect:/";
			} else {
				usuario.setEmail(email);
			}
			usuario.setRol(rolRepository.getByTipo("auth"));

			if (!fNacimiento.isEmpty()) {
				LocalDate fecha = LocalDate.parse(fNacimiento);
				usuario.setFechaNacimiento(fecha);
			} else {
				H.setInfoModal("Error|La fecha de nacimiento no es válida|btn-hover btn-red", s);
				return "redirect:/";
			}

			usuarioRepository.save(usuario);

			emailService.sendEmail(usuario);

			H.setInfoModal(
					"Info|Te has registrado correctamente! Revisa tu bandeja de entrada para activar la cuenta antes de logear por primera vez|btn-hover btn-black",
					s);

		} catch (Exception e) {
			System.out.println(e);
			H.setInfoModal(
					"Error|Ha ocurrido un error en el registro. Por favor vuelva a intentarlo.|btn-hover btn-red", s);

		}

		return "redirect:/";
	}

	@GetMapping("/registroConfirmado")
	public String confirmarRegistro(ModelMap m, HttpSession s, @RequestParam("token") String token) throws IOException, SerialException, SQLException {

		VerificationToken verificationToken = verificationTokenRepository.getByToken(token);

		if (verificationToken == null) {

			H.setInfoModal("Error|El link al que has accedido no existe|btn-hover btn-red", s);

			return "redirect:/";
		} else if ((verificationToken != null) && (verificationToken.getUsuario().isEnabled())) {

			H.setInfoModal("Error|Esta cuenta ya ha sido activada anteriormente|btn-hover btn-red", s);

			return "redirect:/";
		}

		Usuario usuario = verificationToken.getUsuario();
		Calendar cal = Calendar.getInstance();

		if ((verificationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {

			H.setInfoModal("Error|El link de activación de la cuenta ha expirado|btn-hover btn-red", s);

			return "redirect:/";
		}

		usuario.setEnabled(true);
		
		File file = new File("/app/src/main/resources/static/img/default_picture.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
		usuario.setFotoPerfil(H.convertidorBlob(multipartFile));
		usuarioRepository.save(usuario);


		H.setInfoModal("Info|La cuenta se ha activado correctamente. Ya puedes hacer login|btn-hover btn-black", s);
		return "redirect:/";

	}

}
package org.tfg.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tfg.domain.Ciudad;
import org.tfg.domain.Comentario;
import org.tfg.domain.Instrumento;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Reporte;
import org.tfg.domain.Seguimiento;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.repositories.CiudadRepository;
import org.tfg.repositories.ComentarioRepository;
import org.tfg.repositories.InstrumentoRepository;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.ReporteRepository;
import org.tfg.repositories.SeguimientoRepository;
import org.tfg.repositories.UsuarioRepository;
import org.tfg.repositories.WaveRepository;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private InstrumentoRepository instrumentoRepository;

	@Autowired
	private SeguimientoRepository seguimientoRepository;

	@Autowired
	private PublicacionRepository publicacionRepository;

	@Autowired
	private CiudadRepository ciudadRepository;

	@PostMapping("/logout")
	public String postLogout(ModelMap m, HttpSession s) {

		s.removeAttribute("userLogged");

		H.setInfoModal("Info|La sesión ha cerrado correctamente|btn-hover btn-black", s);

		return "redirect:/";
	}

	@GetMapping("/user/{loginName}")
	public String getPerfil(@PathVariable("loginName") String username, ModelMap m, HttpSession s) {
		H.actualizarPeticiones(s, seguimientoRepository);

		if (usuarioRepository.getByLoginName(username) == null) {

			H.setInfoModal("Error|No existe esta página|btn-hover btn-red", s);

			return "redirect:/feed";
		} else {

			H.mPut(m, s);

			Usuario usuarioCargado = usuarioRepository.getByLoginName(username);

			Collection<Long> seguidores = seguimientoRepository
					.findSeguidoresByIdUsuario(usuarioRepository.getByLoginName(username).getId());
			Collection<Long> seguidos = seguimientoRepository
					.findSeguidosByIdUsuario(usuarioRepository.getByLoginName(username).getId());
			Collection<Long> seguidoresNoAceptados = seguimientoRepository
					.findSeguidoresNoAceptadosByIdUsuario(usuarioRepository.getByLoginName(username).getId());

			if (s.getAttribute("userLogged") != null) {
				if (((Usuario) s.getAttribute("userLogged")).getLoginName().equals(usuarioCargado.getLoginName())) {
					m.put("propietario", true);
				}
				if (seguidores.contains(((Usuario) s.getAttribute("userLogged")).getId())) {
					m.put("seguido", true);
				} else if (seguidoresNoAceptados.contains(((Usuario) s.getAttribute("userLogged")).getId())) {
					m.put("seguidoSolicitado", true);
				}
			}

			m.put("publicaciones", publicacionRepository.getByDuenioPublicacion(usuarioCargado));
			m.put("usuario", usuarioRepository.getByLoginName(username));

			m.put("seguidores", seguidores.size());
			m.put("seguidos", seguidos.size());

			m.put("view", "usuario/perfilUsuario");

			return "t/frameFeed";
		}
	}

	// Opciones de perfil

	@GetMapping("/user/{loginName}/opciones")
	public String opcionesPerfil(@PathVariable("loginName") String username,
			@RequestParam(value = "solicitud", required = false) String solicitud, ModelMap m, HttpSession s) {
		H.actualizarPeticiones(s, seguimientoRepository);

		if (s.getAttribute("userLogged") != null) {
			if (username.equals(((Usuario) s.getAttribute("userLogged")).getLoginName())) {
				H.mPut(m, s);
				if (solicitud != null) {
					m.put("solicitud", true);
				}

				m.put("view", "perfil/opcionesPerfil");
				return "t/frameFeed";
			}
		}
		return "redirect:/user/" + username;

	}

	@GetMapping("/editarPerfil")
	public String editarPerfil(ModelMap m, HttpSession s) {
		Usuario userLogged = (Usuario) s.getAttribute("userLogged");
		if (!userLogged.getInstrumentos().isEmpty()) {
			ArrayList<String> instrumentosArray = new ArrayList<String>();
			for (Instrumento instrumento: userLogged.getInstrumentos()) {
				instrumentosArray.add(instrumento.getNombre());
			}
			m.put("instrumentosActuales", instrumentosArray);
		}
		if (userLogged.getCiudad() != null) {
			m.put("ciudadActual", userLogged.getCiudad());
		}
		if (userLogged.getDescripcionPerfil() != null) {
			if(!userLogged.getDescripcionPerfil().equals("")) {
				m.put("descripcionActual", userLogged.getDescripcionPerfil());
			}
		}
		m.put("instrumentos", instrumentoRepository.findAll());
		m.put("ciudades", ciudadRepository.findAll());
		return "perfil/opciones/editarPerfil";
	}

	@PostMapping("/editarPerfil")
	public String editarPerfil(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@RequestParam("idCiudad") Long idCiudad, @RequestParam("descripcion") String descripcion,
			@RequestParam(value = "instrumentos", required = false) List<String> instrumentos, HttpSession s)
			throws IOException, SerialException, SQLException {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		String originalFilename = file.getOriginalFilename().toLowerCase();

		if (idCiudad != null) {

			Ciudad ciudad = ciudadRepository.getOne(idCiudad);

			usuario.setCiudad(ciudad);
		}

		if (descripcion != null) {
			usuario.setDescripcionPerfil(descripcion);
		}
		if (!(instrumentos == null)) {
			usuario.setInstrumentos(instrumentoRepository.getInstrumentosByArray(instrumentos));
		}

//		 comprobamos si esta vacio o nulo
		if (!originalFilename.equals("")) {

			// comprobamos el tamaño del archivo en bytes y aqui 2MB
			if (file.getSize() <= 2000000) {

				// comprobamos la extension en la que termina el archivo
				if (originalFilename.endsWith(".png") || originalFilename.endsWith(".jpg")
						|| originalFilename.endsWith(".jpeg")) {

					usuario.setFotoPerfil(H.convertidorBlob(file));

				} else {
					H.setInfoModal("Error|Solo se permiten imagenes con extension png, jpg o jpeg|btn-hover btn-red",
							s);
					return "redirect:/user/" + usuario.getLoginName() + "/opciones";
				}

			} else {

				H.setInfoModal("Error|La imagen excede el tamaño permitido (2 MB)|btn-hover btn-red", s);

				return "redirect:/user/" + usuario.getLoginName() + "/opciones";

			}

		}

		H.setInfoModal("Info|Perfil modificado correctamente|btn-hover btn-black", s);

		usuarioRepository.save(usuario);

		return "redirect:/user/" + usuario.getLoginName();

	}

	@GetMapping("/editarPass")
	public String gestionarPass() {

		return "perfil/opciones/pass";
	}

	@PostMapping("/editarPass")
	public String gestionarPassword(@RequestParam("passActual") String pass, @RequestParam("pass") String newPass,
			@RequestParam("repass") String newRePass, HttpSession s) throws DangerException {
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (encoder.matches(pass, usuario.getPass())) {
			if (!encoder.matches(newPass, usuario.getPass())) {
				if (newPass.equals(newRePass)) {
					usuario.setPass(encoder.encode(newPass));
					usuarioRepository.save(usuario);
					s.removeAttribute("userLogged");
					H.setInfoModal(
							"Info|La contraseña ha sido modificada con éxito. Por favor, vuelva a hacer login|btn-hover btn-black",
							s);
					return "redirect:/";
				} else {
					H.setInfoModal("Error|La nueva contraseña no coincide|btn-hover btn-red", s);
					return "redirect:/user/" + usuario.getLoginName() + "/opciones";
				}
			} else {
				H.setInfoModal("Error|La nueva contraseña no puede ser igual a la antigua|btn-hover btn-red", s);
				return "redirect:/user/" + usuario.getLoginName() + "/opciones";
			}
		} else {
			H.setInfoModal("Error|La contraseña antigua no es correcta|btn-hover btn-red", s);
			return "redirect:/user/" + usuario.getLoginName() + "/opciones";
		}
	}

	@GetMapping("/editarCorreo")
	public String gestionarCorreo() {

		return "perfil/opciones/correo";
	}

	@PostMapping("/editarCorreo")
	public String gestionarCorreo(@RequestParam("email") String email, HttpSession s) {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");

		usuario.setEmail(email);

		usuarioRepository.save(usuario);

		return "redirect:/user/" + usuario.getLoginName() + "/opciones";
	}

	@GetMapping("seguidoresSeguidos")
	public String seguidoresSeguidos(ModelMap m, HttpSession s) {

		m.put("seguidores", usuarioRepository.findAllById(
				seguimientoRepository.findSeguidoresByIdUsuario(((Usuario) s.getAttribute("userLogged")).getId())));
		m.put("seguidos", usuarioRepository.findAllById(
				seguimientoRepository.findSeguidosByIdUsuario(((Usuario) s.getAttribute("userLogged")).getId())));

		return "perfil/opciones/seguidoresSeguidos";
	}

	@GetMapping("tasaWaves")
	public String tasaWaves() {

		return "perfil/opciones/tasaWaves";
	}

	@GetMapping("publicacionesFavoritas")
	public String publicacionesFavoritas() {

		return "perfil/opciones/publicacionesFavoritas";
	}

	@GetMapping("editarCuenta")
	public String seleccionTipoCuenta(ModelMap m, HttpSession s) {
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		System.out.println(usuario.isPrivada());
		m.put("tipo", usuario.isPrivada());
		return "perfil/opciones/cuenta";
	}

	@GetMapping("cuentaPublica")
	@Transactional
	@ResponseBody
	public String cambioPublica(HttpSession s) {
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		usuario.setPrivada(false);
		usuarioRepository.save(usuario);

		Collection<Long> seguidoresId = seguimientoRepository.findSeguidoresByIdUsuario(usuario.getId());
		if (!seguidoresId.isEmpty()) {
			for (Long id : seguidoresId) {

				Usuario seguidor = usuarioRepository.getOne(id);

				Seguimiento cambiarAceptado = seguimientoRepository.getBySeguidoAndSeguidor(usuario, seguidor);
				cambiarAceptado.setAceptado(true);
				seguimientoRepository.save(cambiarAceptado);

			}
		}

		return "redirect: user/" + usuario.getLoginName() + "/opciones";
	}

	@GetMapping("cuentaPrivada")
	@Transactional
	@ResponseBody
	public String cambioPrivada(HttpSession s) {
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		usuario.setPrivada(true);
		usuarioRepository.save(usuario);

		return "redirect: user/" + usuario.getLoginName() + "/opciones";
	}

	@GetMapping("cuentaEliminada")
	public String eliminar(HttpSession s) {

		H.setInfoModal("Info|Se ha borrado el usuario correctamente|btn-hover btn-black", s);
		usuarioRepository.delete((Usuario) s.getAttribute("userLogged"));
		s.removeAttribute("userLogged");
		return "redirect:/";
	}

	@GetMapping("notificaciones")
	public String notificaciones(HttpSession s, ModelMap m) {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");

		Collection<Long> idSeguidores = seguimientoRepository.findSeguidoresNoAceptadosByIdUsuario(usuario.getId());

		Collection<Usuario> usuariosSeguidores = usuarioRepository.findAllById(idSeguidores);

		if (!usuariosSeguidores.isEmpty()) {
			m.put("seguidores", usuariosSeguidores);
		} else {
			m.put("seguidores", false);
		}

		return "perfil/opciones/notificaciones";
	}




}

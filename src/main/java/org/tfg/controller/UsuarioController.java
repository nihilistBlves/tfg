package org.tfg.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.domain.Wave;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.helper.PRG;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.UsuarioRepository;
import org.tfg.repositories.WaveRepository;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PublicacionRepository publicacionRepository;
	
	@Autowired
	private WaveRepository waveRepository;
	
	@GetMapping("/feed")
	public String getFeed(ModelMap m, HttpSession s) {

		if (s.getAttribute("userLogged") != null) {

			if (s.getAttribute("infoModal") != null) {
				H.mPut(m, s);
			}
			
			Usuario usuario = (Usuario) s.getAttribute("userLogged");
			
			m.put("waves", waveRepository.findAll());
			m.put("publicaciones", publicacionRepository.findAll());
			m.put("view", "usuario/feed");
			return "_t/frameFeed";

		} else {
			H.setInfoModal("Error|Para acceder a este apartado debe estar logueado|btn-hover btn-red", s);

			return "redirect:/";
		}

	}

	@PostMapping("/logout")
	public String postLogout(ModelMap m, HttpSession s) {
		s.removeAttribute("userLogged");
		H.setInfoModal("Info|La sesión ha cerrado correctamente|btn-hover btn-black", s);
		return "redirect:/";
	}

	@GetMapping("/user/{loginName}")
	public String getPerfil(@PathVariable("loginName") String username, ModelMap m, HttpSession s) {

		if (usuarioRepository.getByLoginName(username) == null) {
			H.setInfoModal("Error|No existe esta página|btn-hover btn-red", s);

			return "redirect:/feed";
		} else {

			int seguidores = usuarioRepository.getByLoginName(username).getSeguidores().size();
			int seguidos = usuarioRepository.getByLoginName(username).getSeguidos().size();

			m.put("publicaciones", usuarioRepository.getByLoginName(username).getPublicaciones());
			m.put("usuario", usuarioRepository.getByLoginName(username));
			m.put("seguidores", seguidores);
			m.put("seguidos", seguidos);

			if (s.getAttribute("userLogged") != null) {
				if (username.equals(((Usuario) s.getAttribute("userLogged")).getLoginName())) {
					m.put("propietario", "si");
				}
			}

			m.put("view", "usuario/perfilUsuario");

			return "_t/frameFeed";
		}
	}

	// ===========================================================================
	// ============================================================================

	@GetMapping("/publicar")
	public String getPublicar(ModelMap m, HttpSession s) {
		if (s.getAttribute("userLogged") != null) {
			if (s.getAttribute("infoModal") != null) {
				H.mPut(m, s);
			}
			m.put("view", "usuario/publicar");
			return "_t/frameFeed";
		} else {
			H.setInfoModal("Error|Para acceder a este apartado debe estar logueado|btn-hover btn-red", s);
			return "redirect:/";
		}

	}

	@PostMapping("/publicar")
	public String getPublicar(@RequestParam("mensaje") String texto, @RequestParam("file") MultipartFile file,
			RedirectAttributes attributes, HttpSession s, ModelMap m) throws IOException {

		Path path = null;
		String originalFilename = file.getOriginalFilename().toLowerCase();
		String nuevoNombreRandom = UUID.randomUUID().toString();
		String extensionArchivo = "";
		String nuevoNombreArchivo = "";

		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		Publicacion publicacion = new Publicacion();

		if (!originalFilename.equals("")) {

			extensionArchivo = originalFilename.substring(originalFilename.lastIndexOf("."));
			nuevoNombreArchivo = nuevoNombreRandom + extensionArchivo;

			if ((!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")
					&& !originalFilename.endsWith(".jpeg"))
					&& (!originalFilename.endsWith(".mov") && !originalFilename.endsWith(".mp4")
							&& !originalFilename.endsWith(".mpg"))
					&& (!originalFilename.endsWith(".ogg") && !originalFilename.endsWith(".mp3"))) {
				H.setInfoModal(
						"Error|Solo se permiten fotos con extension png,jpg,jpeg. Y para videos : mp4,mov,mpg|btn btn-danger",
						s);

				return "redirect:/publicar";

			} else {

				if (originalFilename.endsWith(".png") || originalFilename.endsWith(".jpg")
						|| originalFilename.endsWith(".jpeg")) {

					if (file.getSize() <= 2000000) {

						byte[] fileBytes = file.getBytes();

						path = Paths.get("src//main//resources/static/users/" + usuario.getLoginName() + "/posts/img");
						String rutaRelativa = path.toFile().getAbsolutePath();
						Path rutaCompleta = Paths.get(rutaRelativa + "//" + nuevoNombreArchivo);
						Files.write(rutaCompleta, fileBytes);

						if (path != null) {
							publicacion.setContenido(
									"/users/" + usuario.getLoginName() + "/posts/img" + "/" + nuevoNombreArchivo);
						}

						publicacion.setTipoContenido("img");

					} else {

						H.setInfoModal("Error|La imagen excede el tamaño permitido (2 MB)|btn-hover btn-red", s);

						return "redirect:/publicar";

					}

				}

				if (originalFilename.endsWith(".mp4") || originalFilename.endsWith(".mov")
						|| originalFilename.endsWith(".mpg")) {

					if (file.getSize() <= 20000000) {

						byte[] fileBytes = file.getBytes();

						path = Paths
								.get("src//main//resources/static/users/" + usuario.getLoginName() + "/posts/video");
						String rutaRelativa = path.toFile().getAbsolutePath();
						Path rutaCompleta = Paths.get(rutaRelativa + "//" + nuevoNombreArchivo);
						Files.write(rutaCompleta, fileBytes);

						if (path != null) {
							publicacion.setContenido(
									"/users/" + usuario.getLoginName() + "/posts/video" + "/" + nuevoNombreArchivo);
						}

						publicacion.setTipoContenido("video");

					} else {

						H.setInfoModal("Error|El video excede el tamaño permitido (20 MB)|btn-hover btn-red", s);

						return "redirect:/publicar";

					}

				}

				if (originalFilename.endsWith(".mp3") || originalFilename.endsWith(".ogg")) {

					if (file.getSize() <= 10000000) {

						byte[] fileBytes = file.getBytes();

						path = Paths
								.get("src//main//resources/static/users/" + usuario.getLoginName() + "/posts/audio");
						String rutaRelativa = path.toFile().getAbsolutePath();
						Path rutaCompleta = Paths.get(rutaRelativa + "//" + nuevoNombreArchivo);
						Files.write(rutaCompleta, fileBytes);

						if (path != null) {
							publicacion.setContenido(
									"/users/" + usuario.getLoginName() + "/posts/audio" + "/" + nuevoNombreArchivo);
						}

						publicacion.setTipoContenido("audio");

					} else {

						H.setInfoModal("Error|El audio excede el tamaño permitido (10 MB)|btn-hover btn-red", s);

						return "redirect:/publicar";

					}

				}

				if (!texto.equals("")) {
					publicacion.setDescripcion(texto);
				}
				publicacion.setDuenioPublicacion(usuario);
				publicacionRepository.save(publicacion);
				Collection<Publicacion> publicacionesActualizadas = usuario.getPublicaciones();
				publicacionesActualizadas.add(publicacion);
				usuario.setPublicaciones(publicacionesActualizadas);
				usuarioRepository.save(usuario);

				H.setInfoModal("Info|Publicación creada correctamente|btn-hover btn-black", s);
				return "redirect:/feed";
			}

		} else {

			if (texto.equals("")) {
				H.setInfoModal(
						"Error|Para crear una publicación sin archivo se requiere mínimo un texto|btn-hover btn-red",
						s);
				return "redirect:/publicar";
			} else {
				publicacion.setDescripcion(texto);
				publicacion.setDuenioPublicacion(usuario);
				publicacionRepository.save(publicacion);
				Collection<Publicacion> publicacionesActualizadas = usuario.getPublicaciones();
				publicacionesActualizadas.add(publicacion);
				usuario.setPublicaciones(publicacionesActualizadas);
				usuarioRepository.save(usuario);

				H.setInfoModal("Info|Publicación creada correctamente|btn-hover btn-black", s);

				return "redirect:/feed";
			}
		}
	}

	// ============================================================================
	// ============================================================================

	// Opciones de perfil controller

	@GetMapping("/user/{loginName}/opciones")
	public String opcionesPerfil(@PathVariable("loginName") String username, ModelMap m, HttpSession s) {
		if (s.getAttribute("userLogged") != null) {
			if (username.equals(((Usuario) s.getAttribute("userLogged")).getLoginName())) {
				H.mPut(m, s);
				m.put("view", "perfil/opcionesPerfil");
				return "/_t/frameFeed";
			}
		}
		return "redirect:/user/" + username;

	}

	@GetMapping("/editarPerfil")
	public String editarPerfil() {

		return "perfil/opciones/editarPerfil";
	}

	@PostMapping("/editarPerfil")
	public String editarPerfil(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@RequestParam("nombre") String nombre, @RequestParam("apellidos") String apellidos,
			@RequestParam("edad") String edad, @RequestParam("descripcion") String descripcion, HttpSession s)
			throws IOException {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		String originalFilename = file.getOriginalFilename().toLowerCase();
		String nuevoNombreRandom = UUID.randomUUID().toString();
		String extensionArchivo = originalFilename.substring(originalFilename.lastIndexOf("."));
		String nuevoNombreArchivo = nuevoNombreRandom + extensionArchivo;

		if (nombre != null) {
			usuario.setNombre(nombre);
		}
		if (apellidos != null) {
			usuario.setApellidos(apellidos);
		}

		if (!edad.equals("")) {
			LocalDate date = LocalDate.parse(edad);
			usuario.setFechaNacimiento(date);
		}
		if (descripcion != null)
			usuario.setDescripcionPerfil(descripcion);

//		 comprobamos si esta vacio o nulo
		if (!file.isEmpty()) {
			// comprobamos el tamaño del archivo en bytes y aqui 2MB
			if (file.getSize() <= 2000000) {

				// comprobamos la extension en la que termina el archivo
				if (originalFilename.endsWith(".png") || originalFilename.endsWith(".jpg")
						|| originalFilename.endsWith(".jpeg")) {

					byte[] fileBytes = file.getBytes();

					Path path = Paths.get("src//main//resources/static/users/" + usuario.getLoginName() + "/perfil");
					String rutaRelativa = path.toFile().getAbsolutePath();
					Path rutaCompleta = Paths.get(rutaRelativa + "//" + nuevoNombreArchivo);
					Files.write(rutaCompleta, fileBytes);

					usuario.setFotoPerfil("/users/" + usuario.getLoginName() + "/perfil/" + nuevoNombreArchivo);

				} else {
					H.setInfoModal("Error|Solo se permiten imagenes con extension png, jpg o jpeg|btn-hover btn-red",
							s);
					return "redirect:/user/" + usuario.getLoginName() + "/opciones";
				}

			} else {

				H.setInfoModal("Error|La imagen excede el tamaño permitido (2 MB)|btn-hover btn-red", s);

				return "redirect:/publicar";

			}

		}

		H.setInfoModal("Info|Perfil modificado correctamente|btn-hover btn-black", s);

		usuarioRepository.save(usuario);

		return "redirect:/user/" + usuario.getLoginName();

	}

	@GetMapping("editarPass")
	public String gestionarPass() {

		return "perfil/opciones/pass";
	}

	@PostMapping("editarPass")
	public String gestionarPass(@RequestParam("passActual") String pass, @RequestParam("pass") String newPass,
			@RequestParam("repass") String newRePass, HttpSession s) throws DangerException {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (encoder.matches(pass, usuario.getPass())) {

			if (!encoder.matches(newPass, usuario.getPass())) {

				if (newPass.contains(newRePass)) {

					usuario.setPass(encoder.encode(newPass));

					usuarioRepository.save(usuario);

					// TODO Añadir correo de informacion de cambio de contraseña
				} else {
					PRG.error("La contraseña no coincide", "/editarPass");
				}

			} else {

				PRG.error("La contraseña antigua y la nueva no deben ser iguales", "/editarPass");
			}

		} else {
			PRG.error("La nueva contraseña y la contraseña repetida nueva no coinciden", "/editarPass");

		}

		return "redirect:/menuOpciones";
	}

	@GetMapping("editarCorreo")
	public String gestionarCorreo() {

		return "perfil/opciones/correo";
	}

	@PostMapping("editarCorreo")
	public String gestionarCorreo(@RequestParam("email") String email, HttpSession s) {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");

		usuario.setEmail(email);

		usuarioRepository.save(usuario);

		return "redirect:/menuOpciones";
	}

	@GetMapping("seguidoresSeguidos")
	public String seguidoresSeguidos(ModelMap m) {

		// cargar usuarios de la bd y pasar a la vista
		m.put("", "");

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
	public String seleccionTipoCuenta() {

		return "perfil/opciones/cuenta";
	}

	@GetMapping("notificaciones")
	public String notificaciones() {

		return "perfil/opciones/notificaciones";
	}

}

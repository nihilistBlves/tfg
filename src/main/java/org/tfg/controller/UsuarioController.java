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
import org.tfg.exception.DangerException;
import org.tfg.helper.PRG;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PublicacionRepository publicacionRepository;

	@GetMapping("/feed")
	public String getFeed(ModelMap m, HttpSession s) {

		if (s.getAttribute("user") != null) {
			
			s.setAttribute("infoModal","true");
			s.setAttribute("infoTitulo", "Error");
			s.setAttribute("infoTexto", "No existe este usuario");
			s.setAttribute("infoEstado", "btn btn-danger");
			
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
			Usuario usuario = (Usuario) s.getAttribute("user");
			m.put("publicaciones", publicacionRepository.findAll());
			m.put("view", "usuario/feed");
			return "_t/frameFeed";

		} else {
			s.setAttribute("infoModal","true");
			s.setAttribute("infoTitulo", "Error");
			s.setAttribute("infoTexto", "Para acceder a este apartado debe estar logueado");
			s.setAttribute("infoEstado", "btn btn-danger");
			
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
			
			return "redirect:/";
		}

	}

	@GetMapping("/{loginName}")
	public String getPerfil(@PathVariable("loginName") String username, ModelMap m, HttpSession s) {

		String returner = "";

		if (usuarioRepository.getByLoginName(username) == null) {
			// DEVOLVER PRG CON ERROR DE QUE NO EXISTE
			
			returner = "redirect:/feed";
		} else {

			Usuario userRequested = usuarioRepository.getByLoginName(username);

			Usuario userLogged = (Usuario) s.getAttribute("user");

			// m.put("usuario", userLogged);

			// List <Publicacion> publicaciones =
			// publicacionRepository.getByDuenioPublicacion(usuario);

			// m.addAttribute("publicaciones",publicaciones);
			
	
			//te falta pasar el usuario por el put y de ahi coger sus datos como descripcion foto y 
			//sus publicaciones
			
			
			m.addAttribute("usuario", userRequested);
			m.put("view", "usuario/perfilUsuario");
			returner = "_t/frameFeed";
		}
		return returner;
	}

	// ===========================================================================
	// ============================================================================

	@GetMapping("/publicar")
	public String getPublicar(ModelMap m,HttpSession s) {

		
		
		
		m.put("view", "usuario/publicar");
		return "_t/frameFeed";

	}

	@PostMapping("/publicar")
	public String getPublicar(@RequestParam("mensaje") String texto, @RequestParam("file") MultipartFile file,
			RedirectAttributes attributes, HttpSession s) throws IOException {

		Path path = null;
		String originalFilename = file.getOriginalFilename();

		if (file != null) {

//			System.out.println(file.getOriginalFilename() + file.getSize());
			if ((!originalFilename.toLowerCase().endsWith(".png") && !originalFilename.toLowerCase().endsWith(".jpg")
					&& !originalFilename.toLowerCase().endsWith(".jpeg"))
					&& (!originalFilename.toLowerCase().endsWith(".mov")
							&& !originalFilename.toLowerCase().endsWith(".mp4")
							&& !originalFilename.toLowerCase().endsWith(".mpg"))
					&& (!originalFilename.toLowerCase().endsWith(".ogg")
							&& !originalFilename.toLowerCase().endsWith(".mp3"))) {

				attributes.addFlashAttribute("message",
						"Solo se permiten fotos con extension png,jpg,jpeg. Y para videos : mp4,mov,mpg");
				return "redirect:/status";

			} else {

				if (originalFilename.toLowerCase().endsWith(".png") || originalFilename.toLowerCase().endsWith(".jpg")
						|| originalFilename.toLowerCase().endsWith(".jpeg")) {

					// archivo en bytes se compara con 800kb

					if (file.getSize() <= 800000) {

						Usuario usuario = (Usuario) s.getAttribute("user");
						LocalDate date = LocalDate.now();
						Publicacion publicacion = new Publicacion();

						byte[] fileBytes = file.getBytes();

						path = Paths.get("src//main//resources/static/users/" + usuario.getLoginName() + "/posts/imgs");
						String rutaRelativa = path.toFile().getAbsolutePath();
						Path rutaCompleta = Paths.get(rutaRelativa + "//" + file.getOriginalFilename());
						Files.write(rutaCompleta, fileBytes);

						attributes.addFlashAttribute("message", "Archivo cargado correctamente [" + rutaCompleta + "]");

						if (!texto.equals("")) {
							publicacion.setDescripcion(texto);

						}
						if (path != null) {
							publicacion.setContenido("/users/" + usuario.getLoginName() + "/posts/imgs" + "/"
									+ file.getOriginalFilename());
						}

						publicacion.setFechaPublicacion(date);
						publicacion.setDuenioPublicacion(usuario);
						publicacion.setTipoContenido("imagen");
						publicacionRepository.save(publicacion);
						Collection<Publicacion> publicacionesActualizadas = usuario.getPublicaciones();
						publicacionesActualizadas.add(publicacion);
						usuario.setPublicaciones(publicacionesActualizadas);
						usuarioRepository.save(usuario);

						return "redirect:/status";

					} else {

						attributes.addFlashAttribute("message", "La foto excede el tamaño permitido");

						return "redirect:/status";

					}

				}
				if (originalFilename.toLowerCase().endsWith(".mp4") || originalFilename.toLowerCase().endsWith(".mov")
						|| originalFilename.toLowerCase().endsWith(".mpg")) {

					if (file.getSize() <= 1000000000) {

						Usuario usuario = (Usuario) s.getAttribute("user");
						LocalDate date = LocalDate.now();
						Publicacion publicacion = new Publicacion();

						byte[] fileBytes = file.getBytes();

						path = Paths
								.get("src//main//resources/static/users/" + usuario.getLoginName() + "/posts/films");
						String rutaRelativa = path.toFile().getAbsolutePath();
						Path rutaCompleta = Paths.get(rutaRelativa + "//" + file.getOriginalFilename());
						Files.write(rutaCompleta, fileBytes);

						attributes.addFlashAttribute("message", "Archivo cargado correctamente [" + rutaCompleta + "]");

						if (!texto.equals("")) {
							publicacion.setDescripcion(texto);

						}
						if (path != null) {
							publicacion.setContenido("/users/" + usuario.getLoginName() + "/posts/films" + "/"
									+ file.getOriginalFilename());
						}

						publicacion.setFechaPublicacion(date);
						publicacion.setDuenioPublicacion(usuario);
						publicacion.setTipoContenido("video");
						publicacionRepository.save(publicacion);
						Collection<Publicacion> publicacionesActualizadas = usuario.getPublicaciones();
						publicacionesActualizadas.add(publicacion);
						usuario.setPublicaciones(publicacionesActualizadas);
						usuarioRepository.save(usuario);

						return "redirect:/status";

					} else {

						attributes.addFlashAttribute("message", "El video excede el tamaño permitido");

						return "redirect:/status";

					}

				}

				if (originalFilename.toLowerCase().endsWith(".mp3")
						|| originalFilename.toLowerCase().endsWith(".ogg")) {

					if (file.getSize() <= 10000000) {

						Usuario usuario = (Usuario) s.getAttribute("user");
						LocalDate date = LocalDate.now();
						Publicacion publicacion = new Publicacion();

						byte[] fileBytes = file.getBytes();

						path = Paths
								.get("src//main//resources/static/users/" + usuario.getLoginName() + "/posts/audios");
						String rutaRelativa = path.toFile().getAbsolutePath();
						Path rutaCompleta = Paths.get(rutaRelativa + "//" + file.getOriginalFilename());
						Files.write(rutaCompleta, fileBytes);

						attributes.addFlashAttribute("message", "Archivo cargado correctamente [" + rutaCompleta + "]");

						if (!texto.equals("")) {
							publicacion.setDescripcion(texto);

						}
						if (path != null) {
							publicacion.setContenido("/users/" + usuario.getLoginName() + "/posts/audios" + "/"
									+ file.getOriginalFilename());
						}

						publicacion.setFechaPublicacion(date);
						publicacion.setDuenioPublicacion(usuario);
						publicacion.setTipoContenido("audio");
						publicacionRepository.save(publicacion);
						Collection<Publicacion> publicacionesActualizadas = usuario.getPublicaciones();
						publicacionesActualizadas.add(publicacion);
						usuario.setPublicaciones(publicacionesActualizadas);
						usuarioRepository.save(usuario);
						return "redirect:/status";

					} else {

						attributes.addFlashAttribute("message", "El video excede el tamaño permitido");

						return "redirect:/status";

					}

				}

			}

		}

		attributes.addFlashAttribute("message", "debede seleccionar un archivo");

		return "redirect:/status";

	}

	// ============================================================================
	// ============================================================================

	// Opciones de perfil controller

	@GetMapping("menuOpciones")
	public String opcionesPerfil() {

		return "perfil/opcionesPerfil";

	}

	@GetMapping("editarPerfil")
	public String editarPerfil() {

		return "perfil/opciones/editarPerfil";
	}

	@PostMapping("editarPerfil")
	public String editarPerfil(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@RequestParam("nombre") String nombre, @RequestParam("apellidos") String apellidos,
			@RequestParam("edad") String edad, @RequestParam("descripcion") String descripcion, HttpSession s)
			throws IOException {

		Usuario usuario = (Usuario) s.getAttribute("user");
		String originalFilename = file.getOriginalFilename();

		if (nombre != null)
			usuario.setNombre(nombre);
		if (apellidos != null)
			usuario.setApellidos(apellidos);
		if (edad != null) {
			LocalDate date = LocalDate.parse(edad);
			usuario.setFechaNacimiento(date);
		}
		if (descripcion != null)
			usuario.setDescripcionPerfil(descripcion);

		usuarioRepository.save(usuario);

//		 comprobamos si esta vacio o nulo
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Por favor seleccione un archivo");
			return "redirect:/status";
		} else {
			// comprobamos el tamaño del archivo en bytes y aqui 800KB
			if (file.getSize() <= 800000) {

				// comprobamos la extension en la que termina el archivo
				if (originalFilename.toLowerCase().endsWith(".png") || originalFilename.toLowerCase().endsWith(".jpg")
						|| originalFilename.toLowerCase().endsWith(".jpeg")) {

					byte[] fileBytes = file.getBytes();

					Path path = Paths.get("src//main//resources//static/users/" + usuario.getLoginName() + "/perfil");
					String rutaRelativa = path.toFile().getAbsolutePath();
					Path rutaCompleta = Paths.get(rutaRelativa + "//" + file.getOriginalFilename());
					Files.write(rutaCompleta, fileBytes);

					usuario.setFotoPerfil(
							"/users/" + usuario.getLoginName() + "/perfil" + "/" + file.getOriginalFilename());

					attributes.addFlashAttribute("message", "Archivo cargado correctamente [" + rutaCompleta + "]");

					return "redirect:/status";

				} else {

					attributes.addFlashAttribute("message", "Solo se permiten fotos con extension png,jpg,jpeg");
					return "redirect:/status";
				}

			} else {

				attributes.addFlashAttribute("message", "excede el tamaño permitido");
				return "redirect:/status";

			}

		}

	}

	@GetMapping("editarPass")
	public String gestionarPass() {

		return "perfil/opciones/pass";
	}

	@PostMapping("editarPass")
	public String gestionarPass(@RequestParam("passActual") String pass, @RequestParam("pass") String newPass,
			@RequestParam("repass") String newRePass, HttpSession s) throws DangerException {

		Usuario usuario = (Usuario) s.getAttribute("user");

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

		Usuario usuario = (Usuario) s.getAttribute("user");

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

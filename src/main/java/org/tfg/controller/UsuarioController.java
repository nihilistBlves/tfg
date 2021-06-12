package org.tfg.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpSession;
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
import org.tfg.domain.Comentario;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Seguimiento;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.helper.PRG;
import org.tfg.repositories.ComentarioRepository;
import org.tfg.repositories.InstrumentoRepository;
import org.tfg.repositories.PublicacionRepository;
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
	private WaveRepository waveRepository;

	@Autowired
	private ComentarioRepository comentarioRepository;

	@GetMapping("/feed")
	public String getFeed(ModelMap m, HttpSession s) {

		if (s.getAttribute("userLogged") != null) {

			if (s.getAttribute("infoModal") != null) {
				H.mPut(m, s);
			}

			Usuario usuario = (Usuario) s.getAttribute("userLogged");

			ArrayList<Publicacion> publicacionesSeguidos = (ArrayList<Publicacion>) publicacionRepository
					.feedDelUsuarioLogeado(usuario.getId());
			Collections.sort(publicacionesSeguidos, Collections.reverseOrder());

			Long[] publicacionesWavedByUserLogged = waveRepository.idsPublicacionWavedByUser(usuario.getId());

			m.put("nombre", usuario.getLoginName());
			m.put("publicacionesWaved", publicacionesWavedByUserLogged);
			m.put("publicaciones", publicacionesSeguidos);
			m.put("view", "usuario/feed");
			return "_t/frameFeed";

		} else {

			H.setInfoModal("Error|Para acceder a este apartado debe estar logueado|btn-hover btn-red", s);

			return "redirect:/";
		}

	}

	@PostMapping("/logout")
	public String postLogout(ModelMap m, HttpSession s) {

		H.setInfoModal("!Exito!|La sesión ha cerrado correctamente|btn-hover btn-green", s);

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

			H.mPut(m, s);

			Usuario usuarioCargado = usuarioRepository.getByLoginName(username);

			Collection<Long> seguidores = seguimientoRepository
					.findSeguidoresByIdUsuario(usuarioRepository.getByLoginName(username).getId());
			Collection<Long> seguidos = seguimientoRepository
					.findSeguidosByIdUsuario(usuarioRepository.getByLoginName(username).getId());

			if (s.getAttribute("userLogged") != null) {
				if (seguidores.contains(((Usuario) s.getAttribute("userLogged")).getId())) {
					m.put("seguido", true);
				}
			}

			m.put("publicaciones", publicacionRepository.getByDuenioPublicacion(usuarioCargado));
			m.put("usuario", usuarioRepository.getByLoginName(username));

			m.put("seguidores", seguidores.size());
			m.put("seguidos", seguidos.size());

			boolean sigue = false;

			Usuario usuarioSesion = (Usuario) s.getAttribute("userLogged");
			for (Long seg : seguidores) {

				if (usuarioSesion.getId() == seg) {
					sigue = true;
					break;
				}
			}
			System.out.println(sigue);
			m.put("mesigue", sigue);

			if (s.getAttribute("userLogged") != null) {
				if (username.equals(((Usuario) s.getAttribute("userLogged")).getLoginName())) {
					m.put("propietario", "si");
				}
			}

			Usuario logedId = (Usuario) s.getAttribute("userLogged");

			m.put("logedId", logedId.getId());
			m.put("perfilId", usuarioCargado.getId());

			Usuario usuarioPerfil = usuarioRepository.getByLoginName(username);

			Seguimiento seguimiento = seguimientoRepository.getBySeguidoAndSeguidor(usuarioPerfil,
					(Usuario) s.getAttribute("userLogged"));

			if (seguimiento != null) {
				m.put("aceptar", seguimiento.getAceptado());
			} else {
				m.put("aceptar", false);
			}

			m.put("view", "usuario/perfilUsuario");

			return "_t/frameFeed";
		}
	}

	@PostMapping("/tipoArchivo")
	@ResponseBody
	public String elegirArchivo(@RequestParam("tipo") String tipo, @RequestParam("nombre") String nombre) {

		System.out.println(tipo);

		String publicacionesTipo = "";
		Usuario usuario = usuarioRepository.getByLoginName(nombre);

		Collection<Publicacion> publicaciones = publicacionRepository.getByDuenioPublicacion(usuario);

		if (tipo.equals("texto")) {

			for (Publicacion p : publicaciones) {

				if (p.getDescripcion() != null) {

					publicacionesTipo += "<div class=' publicacion bg-white ' width='200px' heigth='800px'>"
							+ "	<p class='text-dark text-center text-uppercase font-weight-bold publicacion-text'>"
							+ p.getDescripcion() + "</p>" + "</div>";
				}

			}

			return publicacionesTipo;

		}

		if (tipo.equals("img")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='' width='200px' heigth='800px'>"
							+ "<img class='publicacion-img' src=" + p.getContenido() + ">" + "</div>";
				}

			}
			return publicacionesTipo;
		}

		if (tipo.equals("audio")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='publicacion' width='200px' heigth='800px'>" + "<audio src="
							+ p.getContenido() + " controls type='audio/mpeg'>" + "</audio>" + "</div>";

				}

			}
			return publicacionesTipo;
		}

		if (tipo.equals("video")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='' width='200px' heigth='800px'>"
							+ "<video class='publicacion-video'  controls>" + "<source src=" + p.getContenido()
							+ " type='video/mp4' />" + "</video>" + "</div>";

				}

			}
			return publicacionesTipo;
		}

		return publicacionesTipo;

	}

	@PostMapping("/user/{loginName}/seguir")
	public String postSeguir(@PathVariable("loginName") String username, ModelMap m, HttpSession s) {
		Usuario usuarioAlQueSeguir = usuarioRepository.getByLoginName(username);

		Seguimiento nuevoSeguimiento = new Seguimiento();
		nuevoSeguimiento.setSeguido(usuarioAlQueSeguir);
		nuevoSeguimiento.setSeguidor((Usuario) s.getAttribute("userLogged"));
		if (!usuarioAlQueSeguir.isTipoCuenta()) {
			nuevoSeguimiento.setAceptado(true);
		} else {
			nuevoSeguimiento.setAceptado(false);
		}
		seguimientoRepository.save(nuevoSeguimiento);
		return "redirect:/user/" + username;
	}

	@PostMapping("/user/{loginName}/dejarDeSeguir")
	public String postDejarDeSeguir(@PathVariable("loginName") String username, ModelMap m, HttpSession s) {
		Usuario usuarioSeguido = usuarioRepository.getByLoginName(username);
		Seguimiento seguimientoParaBorrar = seguimientoRepository.getBySeguidoAndSeguidor(usuarioSeguido,
				(Usuario) s.getAttribute("userLogged"));
		seguimientoRepository.delete(seguimientoParaBorrar);
		return "redirect:/user/" + username;
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
		;
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
				publicacion.setTipoContenido("texto");
				publicacion.setDuenioPublicacion(usuario);
				publicacionRepository.save(publicacion);

				H.setInfoModal("Info|Publicación creada correctamente|btn-hover btn-black", s);

				return "redirect:/feed";
			}
		}
	}

	// ============================================================================
	// ============================================================================

	// Opciones de perfil controller

	@GetMapping("/user/{loginName}/opciones")
	public String opcionesPerfil(@PathVariable("loginName") String username,
			@RequestParam(value = "solicitud", required = false) String solicitud, ModelMap m, HttpSession s) {
		if (s.getAttribute("userLogged") != null) {
			if (username.equals(((Usuario) s.getAttribute("userLogged")).getLoginName())) {
				H.mPut(m, s);
				if (solicitud != null) {
					m.put("solicitud", true);
				}

				m.put("view", "perfil/opcionesPerfil");
				return "/_t/frameFeed";
			}
		}
		return "redirect:/user/" + username;

	}

	@GetMapping("/editarPerfil")
	public String editarPerfil(ModelMap m) {
		m.put("instrumentos", instrumentoRepository.findAll());
		return "perfil/opciones/editarPerfil";
	}

	@PostMapping("/editarPerfil")
	public String editarPerfil(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@RequestParam("nombre") String nombre, @RequestParam("apellidos") String apellidos,
			@RequestParam("edad") String edad, @RequestParam("descripcion") String descripcion,
			@RequestParam("instrumentos") List<String> instrumentos, HttpSession s) throws IOException {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		String originalFilename = file.getOriginalFilename().toLowerCase();
		String nuevoNombreRandom = UUID.randomUUID().toString();
		String extensionArchivo = "";
		String nuevoNombreArchivo = "";

		if (nombre != null) {
			usuario.setNombre(nombre);
		}
		if (apellidos != null) {
			usuario.setApellidos(apellidos);
		}

//		if (!edad.equals("")) {
//			LocalDate date = LocalDate.parse(edad);
//			usuario.setFechaNacimiento(date);
//		}
		if (descripcion != null) {
			usuario.setDescripcionPerfil(descripcion);
		}
		if (!instrumentos.isEmpty()) {
			usuario.setInstrumentos(instrumentoRepository.getInstrumentosByArray(instrumentos));
		}

//		 comprobamos si esta vacio o nulo
		if (!originalFilename.equals("")) {

			extensionArchivo = originalFilename.substring(originalFilename.lastIndexOf("."));
			nuevoNombreArchivo = nuevoNombreRandom + extensionArchivo;

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

	// =============================================================================
	// ================================EDITAR=PERFIL================================
	// =============================================================================
	@GetMapping("editarCuenta")
	public String seleccionTipoCuenta(ModelMap m, HttpSession s) {
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		System.out.println(usuario.isTipoCuenta());
		m.put("tipo", usuario.isTipoCuenta());
		return "perfil/opciones/cuenta";
	}

	@GetMapping("cuentaPublica")
	@Transactional
	@ResponseBody
	public String cambioPublica(HttpSession s) {
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		usuario.setTipoCuenta(false);
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
		usuario.setTipoCuenta(true);
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

	// =========================================================================
	// Notificaciones
	// =========================================================================
	@GetMapping("notificaciones")
	public String notificaciones(HttpSession s, ModelMap m) {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");

		Collection<Long> idSeguidores = seguimientoRepository.findSeguidoresByIdUsuario(usuario.getId());

		Collection<Usuario> usuariosSeguidores = new ArrayList<Usuario>();

		if (!idSeguidores.isEmpty()) {
			for (Long isS : idSeguidores) {

				System.out.println(isS);
				Usuario seguidor = usuarioRepository.getOne(isS);
				if (seguidor.isTipoCuenta()) {
					Seguimiento thisSeguimiento = seguimientoRepository.getBySeguidoAndSeguidor(usuario, seguidor);
					if (thisSeguimiento != null) {
						if (thisSeguimiento.getAceptado() == false) {
							usuariosSeguidores.add(seguidor);
						}
					}
				}
			}
		}

		if (!usuariosSeguidores.isEmpty()) {
			m.put("seguidores", usuariosSeguidores);
		} else {
			m.put("seguidores", false);
		}

		return "perfil/opciones/notificaciones";
	}

	@PostMapping("/eliminarPeticion")
	@Transactional
	@ResponseBody
	public String eliminarPeticion(@RequestParam("id_seguidor") Long id_seguidor, HttpSession s) {

		Usuario seguidor = usuarioRepository.getOne(id_seguidor);
		Usuario seguido = (Usuario) s.getAttribute("userLogged");

		Seguimiento seguimientoParaBorrar = seguimientoRepository.getBySeguidoAndSeguidor(seguido, seguidor);
		seguimientoRepository.delete(seguimientoParaBorrar);

		return "";
	}

	@PostMapping("/aceptarPeticion")
	@Transactional
	@ResponseBody
	public String aceptarPeticion(@RequestParam("id_seguidor") Long id_seguidor, HttpSession s) {

		Usuario seguidor = usuarioRepository.getOne(id_seguidor);
		Usuario seguido = (Usuario) s.getAttribute("userLogged");

		Seguimiento seguimiento = seguimientoRepository.getBySeguidoAndSeguidor(seguido, seguidor);
		seguimiento.setAceptado(true);
		seguimientoRepository.save(seguimiento);

		return "";
	}

	// =========================================================================
	// COMENTARIOS
	// =========================================================================

	@PostMapping("crearComentario")
	@Transactional
	@ResponseBody
	public String comentar(@RequestParam("comentario") String comentario,
			@RequestParam("idPublicacion") Long idPublicacion, HttpSession s) {

		Publicacion publicacion = publicacionRepository.getOne(idPublicacion);
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		Comentario coment = new Comentario();

		coment.setTexto(comentario);
		coment.setPublicacionComentada(publicacion);
		coment.setComentador(usuario);
		comentarioRepository.save(coment);

		Collection<Comentario> comentarios = comentarioRepository.getByPublicacionComentada(publicacion);

		String allComentarios = "";
		for (Comentario c : comentarios) {

			allComentarios += "<div class='card'>" + "<div class='card-body'>" + "<span class='userComent'>"
					+ "<img src='" + c.getComentador().getFotoPerfil() + "' class='fotoComent' />"
					+ c.getComentador().getLoginName() + "</span>" + c.getTexto() + "</div></div>";

		}
		return allComentarios;

		// return "redirect:/feed";

		// return "redirect:/feed";
	}

	@GetMapping("verComentarios")
	@ResponseBody
	public String verComentarios(@RequestParam("idPublicacion") Long idPublicacion) {

		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		Collection<Comentario> comentarios = comentarioRepository.getByPublicacionComentada(publicacion);
		String allComentarios = "";
		for (Comentario c : comentarios) {

			allComentarios += "<div class='card'>" + "<div class='card-body'>" + "<span class='userComent'>"
					+ "<img src='" + c.getComentador().getFotoPerfil() + "' class='fotoComent' />"
					+ c.getComentador().getLoginName() + "</span>" + c.getTexto() + "</div></div>";

		}
		return allComentarios;
	}

}

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
	private WaveRepository waveRepository;

	@Autowired
	private ComentarioRepository comentarioRepository;

	@Autowired
	private CiudadRepository ciudadRepository;
	
	@Autowired
	private ReporteRepository reporteRepository;

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
			return "t/frameFeed";

		} else {

			H.setInfoModal("Error|Para acceder a este apartado debe estar logueado|btn-hover btn-red", s);

			return "redirect:/";
		}

	}

	@GetMapping("/publicacion/{id}")
	public String getPublicacion(@PathVariable("id") Long idPublicacion, ModelMap m, HttpSession s) {
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);

		if (s.getAttribute("userLogged") == null) {
			if (publicacion.getDuenioPublicacion().isPrivada()) {
				H.setInfoModal(
						"Error|Debes hacer login y seguir al dueño de la publicación para poder ver su contenido|btn-hover btn-red",
						s);
				return "redirect:/user/" + publicacion.getDuenioPublicacion().getLoginName();
			} else {
				m.put("publicacion", publicacion);
				m.put("view", "usuario/publicacion");
				return "t/frameFeed";
			}
		} else {
			Usuario userLogged = (Usuario) s.getAttribute("userLogged");
			Collection<Long> seguidosPorUserLogged = seguimientoRepository.findSeguidosByIdUsuario(userLogged.getId());
			
			if (Arrays.asList(waveRepository.idsPublicacionWavedByUser(userLogged.getId())).contains(userLogged.getId())) {
				m.put("waved", true);
			}

			if (publicacion.getDuenioPublicacion().isPrivada()) {
				if (!seguidosPorUserLogged.contains(publicacion.getDuenioPublicacion().getId())) {
					H.setInfoModal(
							"Error|Debes seguir al dueño de la publicación para poder ver su contenido|btn-hover btn-red",
							s);
					return "redirect:/user/" + publicacion.getDuenioPublicacion().getLoginName();
				} else {
					m.put("publicacion", publicacion);
					m.put("view", "usuario/publicacion");
					return "t/frameFeed";
				}
			} else {
				m.put("publicacion", publicacion);
				m.put("view", "usuario/publicacion");
				return "t/frameFeed";
			}
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

	@PostMapping("/tipoArchivo")
	@ResponseBody
	public String elegirArchivo(@RequestParam("tipo") String tipo, @RequestParam("nombre") String nombre) {

		System.out.println(tipo);

		String publicacionesTipo = "";
		Usuario usuario = usuarioRepository.getByLoginName(nombre);

		Collection<Publicacion> publicaciones = publicacionRepository.getByDuenioPublicacion(usuario);

		if (tipo.equals("texto")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class=' publicacion bg-white ' width='200px' heigth='800px' data-id="+p.getId()+" onclick='irPublicacion(this)' role='button'>"
							+ "	<p class='text-dark text-center text-uppercase font-weight-bold publicacion-text'>"
							+ p.getDescripcion() + "</p>" + "</div>";
				}

			}

			return publicacionesTipo;

		}

		if (tipo.equals("img")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='' width='200px' heigth='800px' data-id="+p.getId()+" onclick='irPublicacion(this)' role='button'>"
							+ "<img class='publicacion-img' src=" + p.getContenido() + ">" + "</div>";
				}

			}
			return publicacionesTipo;
		}

		if (tipo.equals("audio")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='publicacion' width='200px' heigth='800px' data-id="+p.getId()+" onclick='irPublicacion(this)' role='button'>" + "<audio src="
							+ p.getContenido() + " controls type='audio/mpeg'>" + "</audio>" + "</div>";

				}

			}
			return publicacionesTipo;
		}

		if (tipo.equals("video")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='' width='200px' heigth='800px' data-id="+p.getId()+" onclick='irPublicacion(this)' role='button'>"
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
		if (s.getAttribute("userLogged") != null) {
			Usuario usuarioAlQueSeguir = usuarioRepository.getByLoginName(username);
			Seguimiento nuevoSeguimiento = new Seguimiento();
			nuevoSeguimiento.setSeguido(usuarioAlQueSeguir);
			nuevoSeguimiento.setSeguidor((Usuario) s.getAttribute("userLogged"));
			if (!usuarioAlQueSeguir.isPrivada()) {
				nuevoSeguimiento.setAceptado(true);
			} else {
				nuevoSeguimiento.setAceptado(false);
			}
			seguimientoRepository.save(nuevoSeguimiento);
			return "redirect:/user/" + username;
		} else {
			H.setInfoModal("Error|Debes estar logueado para realizar esta acción|btn-hover btn-red", s);
			return "redirect:/user/" + username;
		}

	}

	@PostMapping("/user/{loginName}/dejarDeSeguir")
	public String postDejarDeSeguir(@PathVariable("loginName") String username, ModelMap m, HttpSession s) {
		Usuario usuarioSeguido = usuarioRepository.getByLoginName(username);
		Usuario usuarioLogged = (Usuario) s.getAttribute("userLogged");
		Seguimiento seguimientoParaBorrar = seguimientoRepository
				.getSeguimientoParaBorrarSeguido(usuarioLogged.getId(), usuarioSeguido.getId());
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
			return "t/frameFeed";
		} else {
			H.setInfoModal("Error|Para acceder a este apartado debe estar logueado|btn-hover btn-red", s);
			return "redirect:/";
		}

	}

	@PostMapping("/publicar")
	public String getPublicar(@RequestParam("mensaje") String texto, @RequestParam("file") MultipartFile file,
			RedirectAttributes attributes, HttpSession s, ModelMap m) throws IOException, SerialException, SQLException {

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

						publicacion.setContenido(H.convertidorBlob(file));

						publicacion.setTipoContenido("img");

					} else {

						H.setInfoModal("Error|La imagen excede el tamaño permitido (2 MB)|btn-hover btn-red", s);

						return "redirect:/publicar";

					}

				}

				if (originalFilename.endsWith(".mp4") || originalFilename.endsWith(".mov")
						|| originalFilename.endsWith(".mpg")) {

					if (file.getSize() <= 20000000) {

						publicacion.setContenido(H.convertidorBlob(file));

						publicacion.setTipoContenido("img");

						publicacion.setTipoContenido("video");

					} else {

						H.setInfoModal("Error|El video excede el tamaño permitido (20 MB)|btn-hover btn-red", s);

						return "redirect:/publicar";

					}

				}

				if (originalFilename.endsWith(".mp3") || originalFilename.endsWith(".ogg")) {

					if (file.getSize() <= 10000000) {

						publicacion.setContenido(H.convertidorBlob(file));

						publicacion.setTipoContenido("img");

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
				return "t/frameFeed";
			}
		}
		return "redirect:/user/" + username;

	}

	@GetMapping("/editarPerfil")
	public String editarPerfil(ModelMap m) {
		m.put("instrumentos", instrumentoRepository.findAll());
		m.put("ciudades", ciudadRepository.findAll());
		return "perfil/opciones/editarPerfil";
	}

	@PostMapping("/editarPerfil")
	public String editarPerfil(@RequestParam("file") MultipartFile file, RedirectAttributes attributes
			, @RequestParam("idCiudad") Long idCiudad,
			@RequestParam("descripcion") String descripcion,
			@RequestParam(value = "instrumentos", required = false) List<String> instrumentos, HttpSession s)
			throws IOException {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		String originalFilename = file.getOriginalFilename().toLowerCase();
		String nuevoNombreRandom = UUID.randomUUID().toString();
		String extensionArchivo = "";
		String nuevoNombreArchivo = "";
		

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

	@PostMapping("eliminarSeguido")
	public String eliminarSeguido(@RequestParam("idSeguido") Long id, HttpSession s) {
		Usuario usuarioLogged = (Usuario) s.getAttribute("userLogged");
		Seguimiento seguimientoEliminar = seguimientoRepository.getSeguimientoParaBorrarSeguido(usuarioLogged.getId(),
				id);

		System.out.println(seguimientoEliminar);
		seguimientoRepository.delete(seguimientoEliminar);

//		H.setInfoModal("Info|Seguidor eleminado correctamente|btn-hover btn-black", s);

		return "redirect:/editarPerfil";

	}

	@PostMapping("eliminarSeguidor")
	public String eliminarSeguidor(@RequestParam("idSeguidor") Long id, HttpSession s) {

		Usuario usuarioLogged = (Usuario) s.getAttribute("userLogged");
		Seguimiento seguimientoEliminar = seguimientoRepository.getSeguimientoParaBorrarSeguidor(usuarioLogged.getId(),
				id);

		System.out.println(seguimientoEliminar);
		seguimientoRepository.delete(seguimientoEliminar);

//		H.setInfoModal("Info|Seguidor eleminado correctamente|btn-hover btn-black", s);

		return "redirect:/opciones";

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

	// =========================================================================
	// Notificaciones
	// =========================================================================
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

		ArrayList<Comentario> comentarios = (ArrayList<Comentario>) comentarioRepository
				.getByPublicacionComentada(publicacion);

		Collections.sort(comentarios, Collections.reverseOrder());

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
	@Transactional
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
	
	@GetMapping("/publicacion/{id}/reportar")
	public String getReportar(@PathVariable("id") Long idPublicacion, ModelMap m, HttpSession s) {
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		m.put("publicacion", publicacion);
		m.put("view", "usuario/reportar");
		return "t/frameFeed";
	}
	
	@PostMapping("/reportar")
	public String postReportar(@RequestParam("idPublicacion") Long idPublicacion, @RequestParam("motivo") String motivo, HttpSession s) {
		Usuario userLogged = (Usuario) s.getAttribute("userLogged");
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		Reporte reporte = new Reporte();
		reporte.setDenunciante(userLogged);
		reporte.setMotivo(motivo);
		reporte.setPublicacionReportada(publicacion);
		reporteRepository.save(reporte);
		
		return "redirect:/";
	}

	
	
	
	
}

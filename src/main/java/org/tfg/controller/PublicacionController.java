package org.tfg.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.helper.H;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.SeguimientoRepository;
import org.tfg.repositories.UsuarioRepository;
import org.tfg.repositories.WaveRepository;

@Controller
public class PublicacionController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PublicacionRepository publicacionRepository;
	
	@Autowired
	private WaveRepository waveRepository;
	
	@Autowired
	private SeguimientoRepository seguimientoRepository;
	
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

			Long[] publicacionesWavedByUserLogged = waveRepository.idsPublicacionWavedByUser(userLogged.getId());
			m.put("publicacionesWaved", publicacionesWavedByUserLogged);

			if (publicacion.getDuenioPublicacion().isPrivada()) {
				if (seguidosPorUserLogged.contains(publicacion.getDuenioPublicacion().getId())) {
					m.put("publicacion", publicacion);
					m.put("view", "usuario/publicacion");
					return "t/frameFeed";
				} else if (userLogged.getRol().getId() == 2) {
					m.put("publicacion", publicacion);
					m.put("view", "usuario/publicacion");
					return "t/frameFeed";
				}else if (publicacion.getDuenioPublicacion().getLoginName().equals(userLogged.getLoginName())) {
					m.put("publicacion", publicacion);
					m.put("view", "usuario/publicacion");
					return "t/frameFeed";
				} else {
					H.setInfoModal(
							"Error|Debes seguir al dueño de la publicación para poder ver su contenido|btn-hover btn-red",
							s);
					return "redirect:/user/" + publicacion.getDuenioPublicacion().getLoginName();
					
				}
			} else {
				
				m.put("publicacion", publicacion);
				m.put("view", "usuario/publicacion");
				return "t/frameFeed";
			}
		}
	}
	
	@PostMapping("/tipoArchivo")
	@ResponseBody
	public String elegirArchivo(@RequestParam("tipo") String tipo, @RequestParam("nombre") String nombre)
			throws SerialException {

		System.out.println(tipo);

		String publicacionesTipo = "";
		Usuario usuario = usuarioRepository.getByLoginName(nombre);

		Collection<Publicacion> publicaciones = publicacionRepository.getByDuenioPublicacion(usuario);

		if (tipo.equals("texto")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class=' publicacion bg-white ' width='200px' heigth='800px' data-id="
							+ p.getId() + " onclick='irPublicacion(this)' role='button'>"
							+ "	<p class='text-dark text-center text-uppercase font-weight-bold publicacion-text'>"
							+ p.getDescripcion() + "</p>" + "</div>";
				}

			}

			return publicacionesTipo;

		}

		if (tipo.equals("img")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='' width='200px' heigth='800px' data-id=" + p.getId()
							+ " onclick='irPublicacion(this)' role='button'>"
							+ "<img class='publicacion-img' src='data:image;base64," + p.getContenido() + "'>"
							+ "</div>";
				}

			}
			return publicacionesTipo;
		}

		if (tipo.equals("audio")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='publicacion' width='200px' heigth='800px' data-id=" + p.getId()
							+ " onclick='irPublicacion(this)' role='button'>" + "<audio src='data:audio/mp3;base64,"
							+ p.getContenido() + "' controls type='audio/mpeg'>" + "</audio>" + "</div>";

				}

			}
			return publicacionesTipo;
		}

		if (tipo.equals("video")) {

			for (Publicacion p : publicaciones) {

				if (p.getTipoContenido().equals(tipo)) {

					publicacionesTipo += "<div class='' width='200px' heigth='800px' data-id=" + p.getId()
							+ " onclick='irPublicacion(this)' role='button'>"
							+ "<video class='publicacion-video' controls poster>" + "<source src='data:video;base64,"
							+ p.getContenido() + "' type='video/mp4' />" + "</video>" + "</div>";

				}

			}
			return publicacionesTipo;
		}

		return publicacionesTipo;

	}
	
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

		String originalFilename = file.getOriginalFilename().toLowerCase();

		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		Publicacion publicacion = new Publicacion();

		if (!originalFilename.equals("")) {

			if ((!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")
					&& !originalFilename.endsWith(".jpeg"))
					&& (!originalFilename.endsWith(".mov") && !originalFilename.endsWith(".mp4")
							&& !originalFilename.endsWith(".mpg"))
					&& !originalFilename.endsWith(".mp3")) {
				H.setInfoModal(
						"Error|Solo se permiten fotos con extension png, jpg o jpeg. Videos con extension mp4, mov o mpg. Audios con extension mp3.|btn btn-danger",
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

				if (originalFilename.endsWith(".mp3")) {

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

}

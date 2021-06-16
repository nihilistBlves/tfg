package org.tfg.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tfg.domain.Comentario;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.repositories.ComentarioRepository;
import org.tfg.repositories.PublicacionRepository;

@Controller
public class ComentarioController {
	
	@Autowired
	private PublicacionRepository publicacionRepository;

	@Autowired
	private ComentarioRepository comentarioRepository;
	
	@PostMapping("crearComentario")
	@Transactional
	@ResponseBody
	public String comentar(@RequestParam("comentario") String comentario,
			@RequestParam("idPublicacion") Long idPublicacion, HttpSession s) throws SerialException {

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
					+ "<img src='data:image;base64," + c.getComentador().getFotoPerfil() + "' class='fotoComent' />"
					+ c.getComentador().getLoginName() + "</span>" + c.getTexto() + "</div></div>";

		}
		return allComentarios;
	}

	@GetMapping("verComentarios")
	@Transactional
	@ResponseBody
	public String verComentarios(@RequestParam("idPublicacion") Long idPublicacion) throws SerialException {

		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		Collection<Comentario> comentarios = comentarioRepository.getByPublicacionComentada(publicacion);
		String allComentarios = "";
		for (Comentario c : comentarios) {

			allComentarios += "<div class='card'>" + "<div class='card-body'>" + "<span class='userComent'>"
					+ "<img src='data:image;base64," + c.getComentador().getFotoPerfil() + "' class='fotoComent' />"
					+ c.getComentador().getLoginName() + "</span>" + c.getTexto() + "</div></div>";

		}
		return allComentarios;
	}

}

package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tfg.domain.Seguimiento;
import org.tfg.domain.Usuario;
import org.tfg.helper.H;
import org.tfg.repositories.SeguimientoRepository;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class SeguimientoController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private SeguimientoRepository seguimientoRepository;

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
		Seguimiento seguimientoParaBorrar = seguimientoRepository.getSeguimientoParaBorrarSeguido(usuarioLogged.getId(),
				usuarioSeguido.getId());
		seguimientoRepository.delete(seguimientoParaBorrar);
		return "redirect:/user/" + username;
	}
	
	@PostMapping("eliminarSeguido")
	public String eliminarSeguido(@RequestParam("idSeguido") Long id, HttpSession s) {
		Usuario usuarioLogged = (Usuario) s.getAttribute("userLogged");
		Seguimiento seguimientoEliminar = seguimientoRepository.getSeguimientoParaBorrarSeguido(usuarioLogged.getId(),
				id);

		System.out.println(seguimientoEliminar);
		seguimientoRepository.delete(seguimientoEliminar);

		return "redirect:/editarPerfil";

	}

	@PostMapping("eliminarSeguidor")
	public String eliminarSeguidor(@RequestParam("idSeguidor") Long id, HttpSession s) {

		Usuario usuarioLogged = (Usuario) s.getAttribute("userLogged");
		Seguimiento seguimientoEliminar = seguimientoRepository.getSeguimientoParaBorrarSeguidor(usuarioLogged.getId(),
				id);

		System.out.println(seguimientoEliminar);
		seguimientoRepository.delete(seguimientoEliminar);

		return "redirect:/opciones";

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

}

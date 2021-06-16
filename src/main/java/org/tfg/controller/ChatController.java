package org.tfg.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.domain.Mensaje;
import org.tfg.domain.Usuario;
import org.tfg.helper.H;
import org.tfg.repositories.MensajeRepository;
import org.tfg.repositories.SeguimientoRepository;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class ChatController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MensajeRepository mensajeRepository;
	
	@Autowired
	private SeguimientoRepository seguimientoRepository;
	
	@GetMapping("/user/{loginName}/messages")
	public String getListaChat(@PathVariable("loginName") String username, @RequestParam(value = "idPerfil", required = false) Long idPerfil, HttpSession s, ModelMap m) {
		H.actualizarPeticiones(s, seguimientoRepository);

		if (s.getAttribute("userLogged") == null || !username.equals(((Usuario) s.getAttribute("userLogged")).getLoginName())) {
			H.setInfoModal("Error|No tienes permisos para acceder a este apartado|btn-hover btn-red", s);
			return "redirect:/user/"+username;
		} else {
			if (idPerfil != null) {
				m.put("mensajeDirecto", true);
				m.put("idPerfil", idPerfil);
			}
			m.put("usuariosConChatAbierto",usuarioRepository.findUsuariosByChatOpened(((Usuario) s.getAttribute("userLogged")).getId()));
			m.put("view", "usuario/mensajes");
			return "t/frameFeed";
		}		
	}
	
	@PostMapping("/cargarChat")
	public String getChatConUsuario(@RequestParam("idUsuario") Long id, ModelMap m, HttpSession s) {
		Usuario usuarioLogged = (Usuario) s.getAttribute("userLogged");
		Usuario usuarioChat = usuarioRepository.getOne(id);
		
		ArrayList<Mensaje> mensajesChat = (ArrayList<Mensaje>) mensajeRepository.getMensajesChat(usuarioLogged.getId(), usuarioChat.getId());
		Collections.sort(mensajesChat);
		
		m.put("usuarioChat", usuarioChat);
		m.put("mensajes", mensajesChat);
		
		return "usuario/chat";
	}
	
	@PostMapping("/mandarMensaje")
	public String postMandarMensaje(@RequestParam("mensaje") String mensaje, @RequestParam("idDestinatario") Long idDestinatario, HttpSession s) {
		Usuario userLogged = (Usuario) s.getAttribute("userLogged");
		Usuario destinatario = usuarioRepository.getOne(idDestinatario);
				
		Mensaje nuevoMensaje = new Mensaje();
		nuevoMensaje.setDestinatario(destinatario);
		nuevoMensaje.setRemitente(userLogged);
		nuevoMensaje.setTexto(mensaje);
		
		mensajeRepository.save(nuevoMensaje);
		
		return "redirect:/";
	}

}

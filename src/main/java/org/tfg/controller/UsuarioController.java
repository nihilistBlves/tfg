package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.tfg.domain.Usuario;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/feed")
	public String getFeed(ModelMap m, HttpSession s) {
		String returner = "";
		
		if (s.getAttribute("user") != null) {
			Usuario usuario = (Usuario) s.getAttribute("user");
			
			returner = "principal/feedPrincipal";
		} else {
			returner = "redirect:/";
		}
		
		return returner;
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
			
			//m.put("usuario", userLogged);
			
			//List <Publicacion> publicaciones = publicacionRepository.getByDuenioPublicacion(usuario);
			
			//m.addAttribute("publicaciones",publicaciones);
			m.addAttribute("usuario",userRequested);
			m.put("view", "usuario/perfilUsuario");
			returner = "_t/frameFeed";
		}
		return returner;
	}

}

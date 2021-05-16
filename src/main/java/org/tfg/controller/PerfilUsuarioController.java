package org.tfg.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.UsuarioRepository;


@Controller
@RequestMapping("usuario")
public class PerfilUsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PublicacionRepository publicacionRepository;
	
	@GetMapping("perfilUsuario")
	public String feedPerfilUsuario(ModelMap m,@RequestParam("idUsuario")Long idUsuario) throws DangerException{
		//Long idUser = new Long(idUsuario);
		
		Usuario usuario=usuarioRepository.getOne(idUsuario);
		
		List <Publicacion> publicaciones = publicacionRepository.getByDuenioPublicacion(usuario);
		
		m.addAttribute("publicaciones",publicaciones);
		m.addAttribute("usuario",usuario);
		
		m.put("view", "usuario/perfilUsuario");
		
		return "_t/frameFeed";
	}
	
	
	

}

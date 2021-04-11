package org.tfg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("usuario")
public class PerfilUsuarioController {
	
	@GetMapping("perfilUsuario")
	public String feedPerfilUsuario(ModelMap m) {
		
		m.put("view", "usuario/perfilUsuario");
		
		return "_t/frameFeed";
	}
	

}

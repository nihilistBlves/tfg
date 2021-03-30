package org.tfg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("opcionesPerfil")
public class OpcionesPerfilController {

	@GetMapping("menuOpciones")
	public String opcionesPerfil() {
		
		return "perfil/opcionesPerfil";
		
	}
	
	@GetMapping("editarPerfil")
	public String editarPerfil() {
		
		
		return "perfil/opciones/editarPerfil";
	}
	
	@GetMapping("gestionarCorreoPass")
	public String gestionarCorreoPass() {
		
		
		return "perfil/opciones/gestionarCorreoPass";
	}
	
	@GetMapping("seguidoresSeguidos")
	public String seguidoresSeguidos() {
		
		
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
	
	@GetMapping("seleccionTipoCuenta")
	public String seleccionTipoCuenta() {
		
		
		return "perfil/opciones/seleccionTipoCuenta";
	}
	
	@GetMapping("notificaciones")
	public String notificaciones() {
		
		
		return "perfil/opciones/notificaciones";
	}
	
	@GetMapping("desactivarCuenta")
	public String desactivarCuenta() {
		
		
		return "perfil/opciones/desactivarCuenta";
	}
	
	@GetMapping("eliminarCuenta")
	public String eliminarCuenta() {
		
		
		return "perfil/opciones/eliminarCuenta";
	}
}

package org.tfg.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.helper.PRG;
import org.tfg.repositories.UsuarioRepository;

@Controller
@RequestMapping("opcionesPerfil")
public class OpcionesPerfilController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("menuOpciones")
	public String opcionesPerfil() {

		return "perfil/opcionesPerfil";

	}

	@GetMapping("editarPerfil")
	public String editarPerfil() {

		return "perfil/opciones/editarPerfil";
	}

	@PostMapping("editarPerfil")
	public String editarPerfil(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@RequestParam("nombre") String nombre, @RequestParam("apellidos") String apellidos,
			@RequestParam("edad") String edad) throws IOException {

		
		//para descripcion esperamos a hablar los tres para hacer que al coger el id de la persona por su loginame 
		//nos lleve a ciertos atributos de el perfil usuario donde meteriamos tambien el telefono ,correo y descripcion,pagina etc
//	     	@RequestParam("descripcion") String descripcion

		Usuario usuario = usuarioRepository.getByLoginName("pepepe");

		if (nombre != null) {

			usuario.setNombre(nombre);

			usuarioRepository.save(usuario);
		}
		if (apellidos != null) {

			usuario.setApellidos(apellidos);

			usuarioRepository.save(usuario);

		}
		if (edad != null) {

			LocalDate date = LocalDate.parse(edad);
			usuario.setFechaNacimiento(date);

			usuarioRepository.save(usuario);

		}

			//comprobamos si esta vacio o nulo
		if (file == null || file.isEmpty()) {
			attributes.addFlashAttribute("message", "Por favor seleccione un archivo");
			return "redirect:/status";
		} else {
			//comprobamos el tamñao del archivo en KB
			if(file.getSize()<=300) {
				
				//comprobamos la extension en la que termina el archivo
			if (!file.getName().endsWith(".png") || !file.getName().endsWith(".jpg")
					|| !file.getName().endsWith(".jpeg")) {

				attributes.addFlashAttribute("message", "Solo se permiten fotos con extension png,jpg,jpeg");
				return "redirect:/status";
			} else {

				byte[] fileBytes = file.getBytes();

				//hay que cambiar pepepe y poner el loginName que pasemos de la session
				Path path = Paths.get("src//main//resources//static/users/pepepe");
				String rutaRelativa = path.toFile().getAbsolutePath();
				Path rutaCompleta = Paths.get(rutaRelativa + "//" + file.getOriginalFilename());
				Files.write(rutaCompleta, fileBytes);

				attributes.addFlashAttribute("message", "Archivo cargado correctamente [" + rutaCompleta + "]");

				return "redirect:/status";
			}
			
			}else {
				attributes.addFlashAttribute("message", "excede el tamaño permitido");
				return "redirect:/status";
				
			}
		}

//		return "perfil/opcionesPerfil";

	}

	@GetMapping("editarPass")
	public String gestionarPass(@RequestParam("passActual") String pass,@RequestParam("passNueva") String newPass,
			@RequestParam("repNueva") String newRePass) throws DangerException {
		
		Usuario usuario = usuarioRepository.getByLoginName("pepepe");
		
		if(usuario.getPass()==pass) {
			
			if(pass==newRePass) {
				
				usuario.setPass(newPass);

				usuarioRepository.save(usuario);
			}else {
				PRG.error("La contraseña no coincide", "/login");
			}
			
			
		}else {
			PRG.error("La nueva contraseña y la contraseña repetida nueva no coinciden", "/login");
			
		}
		
		return "perfil/opciones/pass";
	}
	
	
	@GetMapping("editarCorreo")
	public String gestionarCorreo(@RequestParam("passActual") String email) {
		
		
		Usuario usuario = usuarioRepository.getByLoginName("pepepe");
		
		usuario.setEmail(email);

		usuarioRepository.save(usuario);

		return "perfil/opciones/correo";
	}

	@GetMapping("seguidoresSeguidos")
	public String seguidoresSeguidos( ModelMap m) {
		
		//cargar usuarios de la bd y pasar a la vista
		m.put("","");

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

	@GetMapping("editarCuenta")
	public String seleccionTipoCuenta() {

		return "perfil/opciones/cuenta";
	}

	@GetMapping("notificaciones")
	public String notificaciones() {

		return "perfil/opciones/notificaciones";
	}


}

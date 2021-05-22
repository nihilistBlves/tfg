package org.tfg.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.helper.PRG;
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

			// m.put("usuario", userLogged);

			// List <Publicacion> publicaciones =
			// publicacionRepository.getByDuenioPublicacion(usuario);

			// m.addAttribute("publicaciones",publicaciones);
			m.addAttribute("usuario", userRequested);
			m.put("view", "usuario/perfilUsuario");
			returner = "_t/frameFeed";
		}
		return returner;
	}

	// Opciones de perfil controller

	@GetMapping("menuOpciones")
	public String opcionesPerfil(HttpSession s) {

		System.out.println(s.getAttribute("user"));
		return "perfil/opcionesPerfil";

	}

	@GetMapping("editarPerfil")
	public String editarPerfil() {

		return "perfil/opciones/editarPerfil";
	}

	@PostMapping("editarPerfil")
	public String editarPerfil(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@RequestParam("nombre") String nombre, @RequestParam("apellidos") String apellidos,
			@RequestParam("edad") String edad, @RequestParam("descripcion") String descripcion ,HttpSession s) throws IOException {

		Usuario usuario= (Usuario) s.getAttribute("user");

		if (nombre != null) usuario.setNombre(nombre);
		if (apellidos != null) usuario.setApellidos(apellidos);
		if (edad != null) {
			LocalDate date = LocalDate.parse(edad);
			usuario.setFechaNacimiento(date);
		}
		if(descripcion != null) usuario.setDescripcionPerfil(descripcion);
		
		
		usuarioRepository.save(usuario);
		
		
		// comprobamos si esta vacio o nulo
		if (file == null || file.isEmpty()) {
			attributes.addFlashAttribute("message", "Por favor seleccione un archivo");
			return "redirect:/status";
		} else {
			// comprobamos el tamñao del archivo en KB
			if (file.getSize() <= 300) {

				// comprobamos la extension en la que termina el archivo
				if (!file.getName().endsWith(".png") || !file.getName().endsWith(".jpg")
						|| !file.getName().endsWith(".jpeg")) {

					attributes.addFlashAttribute("message", "Solo se permiten fotos con extension png,jpg,jpeg");
					return "redirect:/status";
				} else {

					byte[] fileBytes = file.getBytes();

					// hay que cambiar pepepe y poner el loginName que pasemos de la session
					Path path = Paths.get("src//main//resources//static/users/pepepe");
					String rutaRelativa = path.toFile().getAbsolutePath();
					Path rutaCompleta = Paths.get(rutaRelativa + "//" + file.getOriginalFilename());
					Files.write(rutaCompleta, fileBytes);

					attributes.addFlashAttribute("message", "Archivo cargado correctamente [" + rutaCompleta + "]");

					return "redirect:/status";
				}

			} else {
				attributes.addFlashAttribute("message", "excede el tamaño permitido");
				return "redirect:/status";

			}

		}

	}

	@GetMapping("editarPass")
	public String gestionarPass() {

		return "perfil/opciones/pass";
	}

	@PostMapping("editarPass")
	public String gestionarPass(@RequestParam("passActual") String pass, @RequestParam("pass") String newPass,
			@RequestParam("repass") String newRePass, HttpSession s)
			throws DangerException {

		Usuario usuario = (Usuario) s.getAttribute("user");
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (encoder.matches(pass, usuario.getPass())) {
			
			if (!encoder.matches(newPass, usuario.getPass())) {

				if (newPass.contains(newRePass)) {
					
					usuario.setPass(encoder.encode(newPass));
					usuarioRepository.save(usuario);
					
					//TODO Añadir correo de informacion de cambio de contraseña
				} else {
					PRG.error("La contraseña no coincide", "/editarPass");
				}

			}else {
				
				PRG.error("La contraseña antigua y la nueva no deben ser iguales","/editarPass");
			}

		} else {
			PRG.error("La nueva contraseña y la contraseña repetida nueva no coinciden", "/editarPass");

		}

		return "redirect:/menuOpciones";
	}
	
	@GetMapping("editarCorreo")
	public String gestionarCorreo() {
		
		return "perfil/opciones/correo";
	}

	@PostMapping("editarCorreo")
	public String gestionarCorreo(@RequestParam("email") String email, HttpSession s) {

		Usuario usuario = (Usuario) s.getAttribute("user");

		usuario.setEmail(email);

		usuarioRepository.save(usuario);

		return "redirect:/menuOpciones";
	}

	@GetMapping("seguidoresSeguidos")
	public String seguidoresSeguidos(ModelMap m) {

		// cargar usuarios de la bd y pasar a la vista
		m.put("", "");

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

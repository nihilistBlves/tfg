package org.tfg.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

	@GetMapping("/upload")
	public String index() {
		return "archivos/upload";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws IOException {

		//Se comprueba si el archivo no es nulo o vacio si es asi se redirige con un mensaje a la otra web
		// con redirect atributes hacemos un sistema parecido a prg donde llevamos al usuario a otra pagina con un mensaje
		if (file == null || file.isEmpty()) {
			attributes.addFlashAttribute("message", "Por favor seleccione un archivo");
			return "redirect:archivos/status";
		}
		
		//CREAR LA CARPETA Y RUTAS
		
				//TAREA=====
		
			//crear carpeta si no existe y hacer que no se repitan nombres o prohibir ciertos tipos de terminaciones

		byte[] fileBytes = file.getBytes();

		Path path = Paths.get("src//main//resources//static/multimedia");
		String rutaAbsoluta=path.toFile().getAbsolutePath();
		Path rutaCompleta=Paths.get(rutaAbsoluta+"//"+file.getOriginalFilename());
		Files.write(rutaCompleta, fileBytes);
		
		
		attributes.addFlashAttribute("message", "Archivo cargado correctamente ["+rutaCompleta+"]");

		return "redirect:/status";
	}
	
	@GetMapping("/status")
	public String status() {
		return "archivos/status";
	}

}

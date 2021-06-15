package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Reporte;
import org.tfg.domain.Usuario;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.ReporteRepository;

@Controller
public class ReporteController {
	
	@Autowired
	private ReporteRepository reporteRepository;
	
	@Autowired
	private PublicacionRepository publicacionRepository;
	
	@GetMapping("/publicacion/{id}/reportar")
	public String getReportar(@PathVariable("id") Long idPublicacion, ModelMap m, HttpSession s) {
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		m.put("publicacion", publicacion);
		m.put("view", "usuario/reportar");
		return "t/frameFeed";
	}

	@PostMapping("/reportar")
	public String postReportar(@RequestParam("idPublicacion") Long idPublicacion, @RequestParam("motivo") String motivo,
			HttpSession s) {
		Usuario userLogged = (Usuario) s.getAttribute("userLogged");
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		Reporte reporte = new Reporte();
		reporte.setDenunciante(userLogged);
		reporte.setMotivo(motivo);
		reporte.setPublicacionReportada(publicacion);
		reporteRepository.save(reporte);

		return "redirect:/";
	}

}

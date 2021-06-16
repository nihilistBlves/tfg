package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.helper.H;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.ReporteRepository;
import org.tfg.repositories.SeguimientoRepository;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class AdminController {
	
	@Autowired
	private PublicacionRepository publicacionRepository;
	
	@Autowired
	private ReporteRepository reporteRepository;
	
	@Autowired
	private SeguimientoRepository seguimientoRepository;
	
	@GetMapping("/admin")
	public String getPanelAdmin(ModelMap m, HttpSession s) {
		H.actualizarPeticiones(s, seguimientoRepository);

		if ((s.getAttribute("userLogged") != null) && (((Usuario) s.getAttribute("userLogged")).getRol().getId() == 2)) {
			m.put("reportes", reporteRepository.findAll());
			m.put("view", "admin/panel");
			return "t/frameFeed";
		} else {
			H.setInfoModal("Error|La página a la que intenta acceder no existe o no tiene permisos para acceder a ella|btn-hover btn-red", s);
			return "redirect:/";
		}
	}
	
	@PostMapping("/eliminarPublicacion")
	public String postEliminarPublicacion(@RequestParam("idPublicacionEliminar") Long id, HttpSession s) {
		Publicacion publicacion = publicacionRepository.getById(id);
		if (s.getAttribute("userLogged") != null) {
			if ((((Usuario) s.getAttribute("userLogged")).getRol().getId() == 2) || (((Usuario) s.getAttribute("userLogged")).getId() == publicacion.getDuenioPublicacion().getId())) {
				publicacionRepository.delete(publicacion);
				return "redirect:/user/"+publicacion.getDuenioPublicacion().getLoginName();
			} else {
				H.setInfoModal("Error|La acción que deseas realizar no existe o no tiene permisos para realizarla|btn-hover btn-red", s);
				return "redirect:/";
			}
		} else {
			H.setInfoModal("Error|La acción que deseas realizar no existe o no tiene permisos para realizarla|btn-hover btn-red", s);
			return "redirect:/";
		}
	}
	
}

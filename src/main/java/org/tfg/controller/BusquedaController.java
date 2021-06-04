package org.tfg.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class BusquedaController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PublicacionRepository publicacionRepository;

	@PostMapping("/buscar")
	@ResponseBody
	private String busqueda(@RequestParam("busqueda") String busqueda,@RequestParam("tipo") String tipo ) {
		
		
		
		Collection<Usuario> usuarios = usuarioRepository.findUsuariosByLoginNameBusqueda(busqueda);
		String usuariosEncontrados="";
		
		if(busqueda.length()!=0) {
	
		for( Usuario usuario : usuarios) {
			
			usuariosEncontrados+="<div id='centro' class=' mx-auto col-6'>"
					+ "			<div class='card mb-3' style='max-width: 540px;'>"
					+ "				<div class='row no-gutters\'>"
					+ "					<div class='col-md-4'>"
					+ "						<img src="+usuario.getFotoPerfil()+" class='card-img' alt=''>"
					+ "					</div>\r\n"
					+ "					<div class='col-md-8'>"
					+ "						<div class='card-body'>"
					+ "							<h5 class='card-title'>"+usuario.getLoginName()+"</h5>"
					+ "							<p class='card-text d-block '>"+usuario.getDescripcionPerfil()+"</p>"
					+ "							<p class='card-text'>"
					+ "								<small class='text-muted'>"+"#Trompeta"+usuario.getInstrumentos()+"</small>"
					+ "							</p>\r\n"
					+ "						</div>\r\n"
					+ "					</div>\r\n"
					+ "				</div>\r\n"
					+ "			</div>\r\n"
					+ "		</div>";
		}
		
		}else {
			return usuariosEncontrados = "";
		}
		return usuariosEncontrados;

}
	//instrumentos por input
	//select desde tablas bdd que busquen usuaruios y publicaciones
}

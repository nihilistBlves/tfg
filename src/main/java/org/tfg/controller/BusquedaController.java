package org.tfg.controller;


import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tfg.domain.Ciudad;
import org.tfg.domain.Usuario;
import org.tfg.repositories.CiudadRepository;
import org.tfg.repositories.UsuarioRepository;

@Controller
public class BusquedaController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CiudadRepository ciudadRepository;
	
	@PostMapping("/buscar")
	@ResponseBody
	private String busqueda(@RequestParam("busqueda") String busqueda, @RequestParam("tipo") String tipo,@RequestParam("id") String id) {

		Collection<Usuario> usuarios = usuarioRepository.findUsuariosByLoginNameBusqueda(busqueda);
		String usuariosEncontrados = "";

			if (tipo.equals("usuario")) {

				for (Usuario usuario : usuarios) {

					usuariosEncontrados += "<div class='d-block mx-auto card m-3 ' style='max-width: 540px;'>"
							+ " <div class='row no-gutters'>" + "<div class='col-md-4'>" + " <img src="
							+ usuario.getFotoPerfil() + " class='img-fluid buscar-img' alt='...'>" + " </div>"
							+ " <div class='col-md-8'>" + " <div class='card-body'>" + " <h5 class='card-title'>"
							+ usuario.getLoginName() + "</h5>" + "  <p class='card-text'>"
							+ usuario.getDescripcionPerfil() + "</p>"
							+ "  <p class='card-text'><small class='text-muted'>" + "trompeta"
							+ usuario.getInstrumentos() + "</small></p>" + "  </div>" + " </div>" + "  </div>"
							+ "</div>";

						}
					return usuariosEncontrados;
				}
				if (tipo.equals("ciudad")) {
					Long idParseado = Long.parseLong(id);

					Ciudad ciudad = ciudadRepository.getOne(idParseado);
					
					Collection<Usuario> usuariosCiudad = usuarioRepository.findUsuarioByCiudad(ciudad);
					
					for (Usuario usuario : usuariosCiudad) {

						usuariosEncontrados += "<div class='d-block mx-auto card m-3 ' style='max-width: 540px;'>"
								+ " <div class='row no-gutters'>" + "<div class='col-md-4'>" + " <img src="
								+ usuario.getFotoPerfil() + " class='img-fluid buscar-img' alt='...'>" + " </div>"
								+ " <div class='col-md-8'>" + " <div class='card-body'>" + " <h5 class='card-title'>"
								+ usuario.getLoginName() + "</h5>" + "  <p class='card-text'>"
								+ usuario.getDescripcionPerfil() + "</p>"
								+ "  <p class='card-text'><small class='text-muted'>" + "trompeta"
								+ usuario.getInstrumentos() + "</small></p>" + "  </div>" + " </div>" + "  </div>"
								+ "</div>";
					}
					
					return usuariosEncontrados;
				}
				if (tipo.equals("instrumento")) {

					Long idParseado = Long.parseLong(id);
					
					Collection<Usuario> usuariosInstrumentos = usuarioRepository.findUsuariosByIdInstrumento(idParseado);
					
					
					for (Usuario usuario : usuariosInstrumentos) {

						usuariosEncontrados += "<div class='d-block mx-auto card m-3 ' style='max-width: 540px;'>"
								+ " <div class='row no-gutters'>" + "<div class='col-md-4'>" + " <img src="
								+ usuario.getFotoPerfil() + " class='img-fluid buscar-img' alt='...'>" + " </div>"
								+ " <div class='col-md-8'>" + " <div class='card-body'>" + " <h5 class='card-title'>"
								+ usuario.getLoginName() + "</h5>" + "  <p class='card-text'>"
								+ usuario.getDescripcionPerfil() + "</p>"
								+ "  <p class='card-text'><small class='text-muted'>" + "trompeta"
								+ usuario.getInstrumentos() + "</small></p>" + "  </div>" + " </div>" + "  </div>"
								+ "</div>";
						}
									}
				return usuariosEncontrados;
			

				}
}

		

package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.domain.Wave;
import org.tfg.repositories.PublicacionRepository;
import org.tfg.repositories.WaveRepository;

@Controller
public class WaveController {

	@Autowired
	private PublicacionRepository publicacionRepository;

	@Autowired
	private WaveRepository waveRepository;

	@PostMapping("/crearWave")
	public String crearWave(@RequestParam("idPublicacion") Long idPublicacion, HttpSession s, ModelMap m) {

		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);

		Wave wave = new Wave();

		wave.setDaWave(usuario);
		wave.setPublicacionWaved(publicacion);
		waveRepository.save(wave);
		publicacion.setCantidadWaves(publicacion.getCantidadWaves()+1);
		publicacionRepository.save(publicacion);
		return "redirect:/feed";

	}

	@PostMapping("/borrarWave")
	@Transactional
	public String borrarWave(@RequestParam("idPublicacion") Long idPublicacion, HttpSession s, ModelMap m) {

		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		waveRepository.deleteByDaWaveAndPublicacionWaved((Usuario) s.getAttribute("userLogged"), publicacion);
		publicacion.setCantidadWaves(publicacion.getCantidadWaves()-1);
		publicacionRepository.save(publicacion);
		return "redirect:/feed";
	}

}

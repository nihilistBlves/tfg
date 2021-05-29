package org.tfg.controller;

import java.time.LocalDate;
import java.util.Optional;

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
import org.tfg.repositories.UsuarioRepository;
import org.tfg.repositories.WaveRepository;

@Controller
public class WaveController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PublicacionRepository publicacionRepository;

	@Autowired
	private WaveRepository waveRepository;
	
	@PostMapping("/crearWave")
	public String crearWave(@RequestParam("wave") Long idPublicacion, HttpSession s, ModelMap m) {
		
		Usuario usuario = (Usuario) s.getAttribute("user");
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		
		LocalDate date = LocalDate.now();
		
		Wave wave = new Wave(date,usuario,publicacion);
		
		waveRepository.save(wave);
		
		System.out.println(wave.getId());
		//m.put("idWave", wave.getId());
		return "redirect:/feed";
		
	}
	
	@PostMapping("/borrarWave")
	@Transactional
	public String borrarWave(@RequestParam("wave") Long idPublicacion, HttpSession s) {
		
		//waveRepository.deleteByPublicacionWavedId(idPublicacion);
		//waveRepository.deleteById(idWave);
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		waveRepository.deleteByDaWaveAndPublicacionWaved((Usuario) s.getAttribute("user"), publicacion);
		return "redirect:/feed";
	}
	
}

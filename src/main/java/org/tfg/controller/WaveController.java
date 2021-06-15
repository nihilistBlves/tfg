package org.tfg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public String borrarWave(@RequestParam("idPublicacion") Long idPublicacion, HttpSession s, ModelMap m) {
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		Publicacion publicacion = publicacionRepository.getById(idPublicacion);
		Wave waveBorrar = waveRepository.getByDaWaveAndPublicacionWaved(usuario, publicacion);
		waveRepository.delete(waveBorrar);
		publicacion.setCantidadWaves(publicacion.getCantidadWaves()-1);
		publicacionRepository.save(publicacion);
		return "redirect:/feed";
	}
	
	@GetMapping("/wavesMes")
	@ResponseBody
	public String wavesMes(HttpSession s) {
		
		Usuario usuario = (Usuario) s.getAttribute("userLogged");
		
		Long [] ene = waveRepository.idsPublicacionWavedByEne(usuario.getId());
		Long [] feb = waveRepository.idsPublicacionWavedByFeb(usuario.getId());
		Long [] mar = waveRepository.idsPublicacionWavedByMar(usuario.getId());
		Long [] abr = waveRepository.idsPublicacionWavedByAbr(usuario.getId());
		Long [] may = waveRepository.idsPublicacionWavedByMay(usuario.getId());
		Long [] jun = waveRepository.idsPublicacionWavedByJun(usuario.getId());
		Long [] jul = waveRepository.idsPublicacionWavedByJul(usuario.getId());
		Long [] ago = waveRepository.idsPublicacionWavedByAgo(usuario.getId());
		Long [] sep = waveRepository.idsPublicacionWavedBySep(usuario.getId());
		Long [] oct = waveRepository.idsPublicacionWavedByOct(usuario.getId());
		Long [] nov = waveRepository.idsPublicacionWavedByNov(usuario.getId());
		Long [] dic = waveRepository.idsPublicacionWavedByDic(usuario.getId());
		
		String numWaves=ene.length+"-"
		+feb.length+"-"
		+mar.length+"-"
		+abr.length+"-"
		+may.length+"-"
		+jun.length+"-"
		+jul.length+"-"
		+ago.length+"-"
		+sep.length+"-"
		+oct.length+"-"
		+nov.length+"-"
		+dic.length;
		
		return numWaves;
	}

}

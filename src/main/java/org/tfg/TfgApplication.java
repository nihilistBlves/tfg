package org.tfg;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tfg.domain.Ciudad;
import org.tfg.domain.Instrumento;
import org.tfg.domain.Rol;
import org.tfg.repositories.CiudadRepository;
import org.tfg.repositories.InstrumentoRepository;
import org.tfg.repositories.RolRepository;

@SpringBootApplication
public class TfgApplication implements CommandLineRunner {

	@Autowired
	InstrumentoRepository instrumentoRepository;

	@Autowired
	RolRepository rolRepository;
	
	@Autowired
	CiudadRepository ciudadRepository;

	public static void main(String[] args) {
		SpringApplication.run(TfgApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		if (instrumentoRepository.findAll().isEmpty()) {
			String[] instrumentos = { "Saxofón", "Flauta", "Clarinete", "Trompeta", "Oboe", "Guitarra acústica", "Arpa",
					"Violín", "Piano", "Bajo", "Bajo eléctrico", "Guitarra eléctrica", "Guitarra española", "Viola",
					"Violonchelo", "Batería", "Teclado eléctrico", "Platos DJ", "Voz", "Beatbox"};
			List<Instrumento> objetosInstrumentos = new ArrayList<Instrumento>();
			for (int i = 0; i < instrumentos.length; i++) {
				Instrumento nuevoInstrumento = new Instrumento(instrumentos[i]);
				objetosInstrumentos.add(nuevoInstrumento);
			}
			instrumentoRepository.saveAll(objetosInstrumentos);
		}
		if (rolRepository.findAll().isEmpty()) {
			rolRepository.save(new Rol("auth"));
			rolRepository.save(new Rol("admin"));
		}

		if(ciudadRepository.findAll().isEmpty()) {
			
			String [] ciudades = {"Madrid","Barcelona","Zaragoza","Sevilla","Málaga","Valencia"};
			
			List<Ciudad> listaCiudades = new ArrayList<Ciudad>();
			for(int i =0; i< ciudades.length; i++) {
				
				Ciudad nuevaCiudad = new Ciudad(ciudades[i]);
				listaCiudades.add(nuevaCiudad);
			}
			
				ciudadRepository.saveAll(listaCiudades);
		}
	}

}
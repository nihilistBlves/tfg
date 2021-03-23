package org.tfg.domain;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

//import org.springframework.web.multipart.MultipartFile;

@Entity
public class Publicacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//private MultipartFile contenido;
	
	private String descripcion;
	
	private LocalDate fechaPublicacion;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario duenioPublicacion;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Comentario> comentarios;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Wave> waves;
	
}

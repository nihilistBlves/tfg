package org.tfg.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comentario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario comentador;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Publicacion publicacionComentada;
	
}

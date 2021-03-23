package org.tfg.domain;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Wave {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate fechaLike;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario daWave;

	@ManyToOne(cascade = CascadeType.ALL)
	private Publicacion publicacionWaved;
	
}

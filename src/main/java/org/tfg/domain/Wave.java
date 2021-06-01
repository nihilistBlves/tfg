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

	// @ManyToOne(cascade = CascadeType.ALL)
	@ManyToOne(cascade = CascadeType.REFRESH)
	private Usuario daWave;

	@ManyToOne(cascade = CascadeType.REFRESH)
	private Publicacion publicacionWaved;

	public Wave() {
		this.fechaLike = LocalDate.now();
	}

	public Wave(Usuario daWave, Publicacion publicacionWaved) {
		super();
		this.fechaLike = LocalDate.now();
		this.daWave = daWave;
		this.publicacionWaved = publicacionWaved;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getFechaLike() {
		return fechaLike;
	}

	public void setFechaLike(LocalDate fechaLike) {
		this.fechaLike = fechaLike;
	}

	public Usuario getDaWave() {
		return daWave;
	}

	public void setDaWave(Usuario daWave) {
		this.daWave = daWave;
	}

	public Publicacion getPublicacionWaved() {
		return publicacionWaved;
	}

	public void setPublicacionWaved(Publicacion publicacionWaved) {
		this.publicacionWaved = publicacionWaved;
	}

}

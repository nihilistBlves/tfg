package org.tfg.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Wave {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime fechaLike;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(cascade = CascadeType.REFRESH)
	private Usuario daWave;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	private Publicacion publicacionWaved;

	public Wave() {
		this.fechaLike = LocalDateTime.now();
	}

	public Wave(Usuario daWave, Publicacion publicacionWaved) {
		super();
		this.fechaLike = LocalDateTime.now();
		this.daWave = daWave;
		this.publicacionWaved = publicacionWaved;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getFechaLike() {
		return fechaLike;
	}

	public void setFechaLike(LocalDateTime fechaLike) {
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

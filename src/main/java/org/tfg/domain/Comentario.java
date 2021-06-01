package org.tfg.domain;

import java.time.LocalDate;

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
	
	private LocalDate fechaPublicacion;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	private Usuario comentador;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	private Publicacion publicacionComentada;

	public Comentario() {}
	
	public Comentario(String texto, LocalDate fechaPublicacion, Usuario comentador, Publicacion publicacionComentada) {
		super();
		this.texto = texto;
		this.fechaPublicacion = fechaPublicacion;
		this.comentador = comentador;
		this.publicacionComentada = publicacionComentada;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDate getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDate fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Usuario getComentador() {
		return comentador;
	}

	public void setComentador(Usuario comentador) {
		this.comentador = comentador;
	}

	public Publicacion getPublicacionComentada() {
		return publicacionComentada;
	}

	public void setPublicacionComentada(Publicacion publicacionComentada) {
		this.publicacionComentada = publicacionComentada;
	}
	
	
	
}

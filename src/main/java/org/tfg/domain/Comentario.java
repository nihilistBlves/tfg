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
public class Comentario implements Comparable<Comentario>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;
	
	private LocalDateTime fechaPublicacion;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	private Usuario comentador;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	private Publicacion publicacionComentada;

	public Comentario() {
		this.fechaPublicacion = LocalDateTime.now();
	}
	
	public Comentario(String texto, LocalDate fechaPublicacion, Usuario comentador, Publicacion publicacionComentada) {
		super();
		this.texto = texto;
		this.fechaPublicacion = LocalDateTime.now();
		this.comentador = comentador;
		this.publicacionComentada = publicacionComentada;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
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

	@Override
	public int compareTo(Comentario comentario) {		
		return getFechaPublicacion().compareTo(comentario.getFechaPublicacion());
	}
	
	
	
}

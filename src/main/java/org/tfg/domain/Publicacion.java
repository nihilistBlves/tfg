package org.tfg.domain;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

	private String contenido;

	@Column(columnDefinition = "TEXT")
	private String descripcion;

	private LocalDate fechaPublicacion;

	private String tipoContenido;

	@ManyToOne(cascade = CascadeType.REFRESH)
	private Usuario duenioPublicacion;

	

	public Publicacion(Long id, String contenido, String descripcion, String tipoContenido, Usuario duenioPublicacion) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.descripcion = descripcion;
		this.fechaPublicacion = LocalDate.now();
		this.tipoContenido = tipoContenido;
		this.duenioPublicacion = duenioPublicacion;

	}

	public Publicacion() {
		super();
		this.fechaPublicacion = LocalDate.now();
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDate fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Usuario getDuenioPublicacion() {
		return duenioPublicacion;
	}

	public void setDuenioPublicacion(Usuario duenioPublicacion) {
		this.duenioPublicacion = duenioPublicacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContenido() {
		return contenido.toString();
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getTipoContenido() {
		return tipoContenido;
	}

	public void setTipoContenido(String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}

}

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

	private String contenido;

	private String descripcion;

	private LocalDate fechaPublicacion;

	private String tipoContenido;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Usuario duenioPublicacion;

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Comentario> comentarios;

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Wave> waves;

	public Publicacion(Long id, String contenido, String descripcion, LocalDate fechaPublicacion, String tipoContenido,
			Usuario duenioPublicacion, Collection<Comentario> comentarios, Collection<Wave> waves) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.descripcion = descripcion;
		this.fechaPublicacion = fechaPublicacion;
		this.tipoContenido = tipoContenido;
		this.duenioPublicacion = duenioPublicacion;
		this.comentarios = comentarios;
		this.waves = waves;
	}

	public Publicacion() {
		super();

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

	public Collection<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(Collection<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public Collection<Wave> getWaves() {
		return waves;
	}

	public void setWaves(Collection<Wave> waves) {
		this.waves = waves;
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

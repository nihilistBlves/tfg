package org.tfg.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class Publicacion implements Comparable<Publicacion> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String contenido;
	
	private int cantidadWaves;

	@Column(columnDefinition = "TEXT")
	private String descripcion;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime fechaPublicacion;

	private String tipoContenido;

	@ManyToOne(cascade = CascadeType.REFRESH)
	private Usuario duenioPublicacion;

	

	public Publicacion(Long id, String contenido, String descripcion, String tipoContenido, Usuario duenioPublicacion) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.descripcion = descripcion;
		this.cantidadWaves = 0;
		this.fechaPublicacion = LocalDateTime.now();
		this.tipoContenido = tipoContenido;
		this.duenioPublicacion = duenioPublicacion;

	}

	public Publicacion() {
		super();
		this.fechaPublicacion = LocalDateTime.now();
		this.cantidadWaves = 0;
	}

	public int getCantidadWaves() {
		return cantidadWaves;
	}

	public void setCantidadWaves(int cantidadWaves) {
		this.cantidadWaves = cantidadWaves;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
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
		return contenido;
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

	@Override
	public int compareTo(Publicacion publicacion) {
		return getFechaPublicacion().compareTo(publicacion.getFechaPublicacion());
	}
}

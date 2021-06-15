package org.tfg.domain;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Publicacion implements Comparable<Publicacion> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private SerialBlob contenido;
	
	private int cantidadWaves;

	@Column(columnDefinition = "TEXT")
	private String descripcion;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime fechaPublicacion;

	private String tipoContenido;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	private Usuario duenioPublicacion;

	public Publicacion(Long id, SerialBlob contenido, String descripcion, String tipoContenido, Usuario duenioPublicacion) {
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

	public SerialBlob getContenido() {
		return contenido;
	}

	public void setContenido(SerialBlob contenido) {
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

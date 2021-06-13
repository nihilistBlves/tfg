package org.tfg.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Reporte {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;
	
	private LocalDateTime fechaReporte;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	private Usuario denunciante;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	private Publicacion publicacionReportada;

	public Reporte() {
		super();
		this.fechaReporte = LocalDateTime.now();
	}

	public Reporte(String texto, Usuario denunciante, Publicacion publicacionReportada) {
		super();
		this.texto = texto;
		this.denunciante = denunciante;
		this.publicacionReportada = publicacionReportada;
		this.fechaReporte = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getFechaReporte() {
		return fechaReporte;
	}

	public void setFechaReporte(LocalDateTime fechaReporte) {
		this.fechaReporte = fechaReporte;
	}

	public Usuario getDenunciante() {
		return denunciante;
	}

	public void setDenunciante(Usuario denunciante) {
		this.denunciante = denunciante;
	}

	public Publicacion getPublicacionReportada() {
		return publicacionReportada;
	}

	public void setPublicacionReportada(Publicacion publicacionReportada) {
		this.publicacionReportada = publicacionReportada;
	}
	
	

}

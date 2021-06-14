package org.tfg.domain;

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
public class Reporte {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String motivo;
	
	private LocalDateTime fechaReporte;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	private Usuario denunciante;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	private Publicacion publicacionReportada;

	public Reporte() {
		super();
		this.fechaReporte = LocalDateTime.now();
	}

	public Reporte(String motivo, Usuario denunciante, Publicacion publicacionReportada) {
		super();
		this.motivo = motivo;
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

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
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

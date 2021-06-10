package org.tfg.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Mensaje implements Comparable<Mensaje>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario remitente;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario destinatario;
	
	private LocalDateTime fechaEnvio;

	public Mensaje(String texto, Usuario remitente, Usuario destinatario) {
		super();
		this.texto = texto;
		this.remitente = remitente;
		this.destinatario = destinatario;
		this.fechaEnvio = LocalDateTime.now();
	}

	public Mensaje() {
		super();
		this.fechaEnvio = LocalDateTime.now();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Usuario getRemitente() {
		return remitente;
	}

	public void setRemitente(Usuario remitente) {
		this.remitente = remitente;
	}

	public Usuario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}

	@Override
	public int compareTo(Mensaje mensaje) {
		return getFechaEnvio().compareTo(mensaje.getFechaEnvio());
	}

	
}

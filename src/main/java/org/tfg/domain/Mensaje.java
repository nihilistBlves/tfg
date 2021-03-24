package org.tfg.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Mensaje {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario remitente;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario destinatario;

	public Mensaje(String texto, Usuario remitente, Usuario destinatario) {
		super();
		this.texto = texto;
		this.remitente = remitente;
		this.destinatario = destinatario;
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

	
}

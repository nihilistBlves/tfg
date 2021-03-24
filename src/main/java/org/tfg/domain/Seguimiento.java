package org.tfg.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Seguimiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double valoracion;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario seguidor;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario seguido;

	public Seguimiento(double valoracion, Usuario seguidor, Usuario seguido) {
		super();
		this.valoracion = valoracion;
		this.seguidor = seguidor;
		this.seguido = seguido;
	}

	public double getValoracion() {
		return valoracion;
	}

	public void setValoracion(double valoracion) {
		this.valoracion = valoracion;
	}

	public Usuario getSeguidor() {
		return seguidor;
	}

	public void setSeguidor(Usuario seguidor) {
		this.seguidor = seguidor;
	}

	public Usuario getSeguido() {
		return seguido;
	}

	public void setSeguido(Usuario seguido) {
		this.seguido = seguido;
	}
	
	

}

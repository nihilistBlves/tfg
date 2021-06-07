package org.tfg.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Instrumento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Collection<Usuario> usuariosUsando;

	public Instrumento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Instrumento(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Collection<Usuario> getUsuariosUsando() {
		return usuariosUsando;
	}

	public void setUsuariosUsando(Collection<Usuario> usuariosUsando) {
		this.usuariosUsando = usuariosUsando;
	}
	
	
	
	
	
}

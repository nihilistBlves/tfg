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

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String loginName;
	@Column(unique = true)
	private String email;
	
	private String pass;
	private String nombre;
	private String apellidos;
	private LocalDate fechaNacimiento;
	private String extensionImg;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Rol rol;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Publicacion> publicaciones;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Comentario> comentariosHechos;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Wave> wavesDados;
	
}

package org.tfg.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(unique = true)
	String tipo;
	
	@OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
	private Collection<Usuario> usuarios;
	
	@OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
	private Collection<Admin> admins;
	
	
}

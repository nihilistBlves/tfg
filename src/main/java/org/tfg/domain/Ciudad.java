package org.tfg.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ciudad {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(unique = true)
	private String ciudadName;
	

	public Ciudad(String ciudadName) {
		super();
		this.ciudadName = ciudadName;
	}


	public Ciudad() {
		super();
	}


	public String getCiudadName() {
		return ciudadName;
	}


	public void setCiudadName(String ciudadName) {
		this.ciudadName = ciudadName;
	}
	
	
}
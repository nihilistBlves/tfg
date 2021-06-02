package org.tfg.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	private LocalDateTime fechaCreacion;

	private boolean enabled;

	private String descripcionPerfil;
	private String fotoPerfil;

	@ManyToOne
	private Rol rol;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Collection<Instrumento> instrumentos;

	public Usuario(String loginName, String email, String pass, String nombre, String apellidos,
			LocalDate fechaNacimiento,boolean enabled, LocalDate fechaCreacion, String descripcionPerfil, String fotoPerfil,
			Collection<Instrumento> instrumentos, Rol rol) {
		super();
		this.loginName = loginName;
		this.email = email;
		this.pass = pass;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;

		this.fechaCreacion = LocalDateTime.now();
		this.enabled = false;
		this.descripcionPerfil = descripcionPerfil;
		this.fotoPerfil = fotoPerfil;

		this.instrumentos = instrumentos;
		this.rol = rol;
	}

	public Usuario() {
		super();
		this.enabled = false;
		this.fechaCreacion = LocalDateTime.now();
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}	

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Collection<Instrumento> getInstrumentos() {
		return instrumentos;
	}

	public void setInstrumentos(Collection<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAdmin() {
		return (this.getRol()!= null && this.getRol().getTipo().equals("admin"));
	}

	public String getDescripcionPerfil() {
		return descripcionPerfil;
	}

	public void setDescripcionPerfil(String descripcionPerfil) {
		this.descripcionPerfil = descripcionPerfil;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}
	
	
}

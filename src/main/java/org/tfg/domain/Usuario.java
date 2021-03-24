package org.tfg.domain;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
	private LocalDate fechaCreacion;
	//private String extensionImg;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Collection<Instrumento> instrumentos;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Rol rol;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Publicacion> publicaciones;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Comentario> comentariosHechos;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Wave> wavesDados;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Seguimiento> seguidores;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Seguimiento> seguidos;

	public Usuario(String loginName, String email, String pass, String nombre, String apellidos,
			LocalDate fechaNacimiento, LocalDate fechaCreacion, Collection<Instrumento> instrumentos, Rol rol) {
		super();
		this.loginName = loginName;
		this.email = email;
		this.pass = pass;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaCreacion = fechaCreacion;
		this.instrumentos = instrumentos;
		this.rol = rol;
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

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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

	public Collection<Publicacion> getPublicaciones() {
		return publicaciones;
	}

	public void setPublicaciones(Collection<Publicacion> publicaciones) {
		this.publicaciones = publicaciones;
	}

	public Collection<Comentario> getComentariosHechos() {
		return comentariosHechos;
	}

	public void setComentariosHechos(Collection<Comentario> comentariosHechos) {
		this.comentariosHechos = comentariosHechos;
	}

	public Collection<Wave> getWavesDados() {
		return wavesDados;
	}

	public void setWavesDados(Collection<Wave> wavesDados) {
		this.wavesDados = wavesDados;
	}

	public Collection<Seguimiento> getSeguidores() {
		return seguidores;
	}

	public void setSeguidores(Collection<Seguimiento> seguidores) {
		this.seguidores = seguidores;
	}

	public Collection<Seguimiento> getSeguidos() {
		return seguidos;
	}

	public void setSeguidos(Collection<Seguimiento> seguidos) {
		this.seguidos = seguidos;
	}
	
	
}

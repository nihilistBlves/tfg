package org.tfg.domain;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class VerificationToken {

	private static final int expiration = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String token;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToOne
	@JoinColumn(nullable = false, name = "usuario_id")
	private Usuario usuario;

	private Date expirationDate;

	@SuppressWarnings("unused")
	private Date calculateExpirationDate(int expTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}
	
	public VerificationToken() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public VerificationToken(String token, Usuario usuario) {
		super();
		this.token = token;
		this.usuario = usuario;
		this.expirationDate = calculateExpirationDate(expiration);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public static int getExpiration() {
		return expiration;
	}

}

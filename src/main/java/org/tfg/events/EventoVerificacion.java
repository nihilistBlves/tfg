package org.tfg.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;
import org.tfg.domain.Usuario;

public class EventoVerificacion extends ApplicationEvent {

	private String appUrl;
	private Locale locale;
	private Usuario usuario;
	
	public EventoVerificacion(Usuario usuario, Locale locale, String appUrl) {
		super(usuario);
		
		this.usuario = usuario;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}

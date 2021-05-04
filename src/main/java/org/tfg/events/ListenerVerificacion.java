package org.tfg.events;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.tfg.domain.Usuario;
import org.tfg.domain.VerificationToken;

@Component
public class ListenerVerificacion implements ApplicationListener<EventoVerificacion> {
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(EventoVerificacion event) {
		this.confirmarRegistro(event);
	}

	private void confirmarRegistro(EventoVerificacion event) {
		Usuario usuario = event.getUsuario();
		String token = 	UUID.randomUUID().toString();
		VerificationToken nuevoToken = new VerificationToken(token, usuario);
	}
	
}

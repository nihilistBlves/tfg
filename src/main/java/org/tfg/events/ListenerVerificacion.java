package org.tfg.events;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.tfg.domain.Usuario;
import org.tfg.domain.VerificationToken;
import org.tfg.repositories.VerificationTokenRepository;

@Component
public class ListenerVerificacion implements ApplicationListener<EventoVerificacion> {

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public void onApplicationEvent(EventoVerificacion event) {
		this.confirmarRegistro(event);
	}

	private void confirmarRegistro(EventoVerificacion event) {
		Usuario usuario = event.getUsuario();
		String token = UUID.randomUUID().toString();
		VerificationToken nuevoToken = new VerificationToken(token, usuario);
		verificationTokenRepository.save(nuevoToken);

		String recipientAddress = usuario.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/registroConfirmado?token=" + token;
		String message = "¡Registro completado! Por favor, finaliza la verificación accediendo al siguiente enlace:\n";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
		mailSender.send(email);
	}

}

package org.tfg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	//Inteccion de la dependencia de JavaMailSender

	@Autowired
	private JavaMailSender mailSender;
	
	//Pasamos parametros: destinatario,asunto y el mensaje
	public void enviarEmail(String destinatario, String asunto, String mensaje) {
		
		SimpleMailMessage email = new SimpleMailMessage();
		
		email.setFrom("waveit.notification@gmail.com");
		email.setTo(destinatario);
		email.setSubject(asunto);
		email.setText(mensaje);
		
		mailSender.send(email);
	}
}

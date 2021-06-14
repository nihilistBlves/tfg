package org.tfg.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.tfg.domain.Usuario;
import org.tfg.domain.VerificationToken;
import org.tfg.events.EventoVerificacion;
import org.tfg.repositories.VerificationTokenRepository;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
@Component
public class EmailService implements ApplicationListener<EventoVerificacion>{
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Value("${app.sendgrid.templateId}")
	private String templateId;
	@Autowired
	SendGrid sendGrid;
	
	@Override
	public void onApplicationEvent(EventoVerificacion event) {
		this.sendEmail(event);
	}
	

//	private void confirmarRegistro(EventoVerificacion event) {
//		Usuario usuario = event.getUsuario();
//		String token = UUID.randomUUID().toString();
//		VerificationToken nuevoToken = new VerificationToken(token, usuario);
//		verificationTokenRepository.save(nuevoToken);
//
//		String recipientAddress = usuario.getEmail();
//		String subject = "Registration Confirmation";
//		String confirmationUrl = event.getAppUrl() + "/registroConfirmado?token=" + token;
//		String message = "¡Registro completado! Por favor, finaliza la verificación accediendo al siguiente enlace:\n";
//
//		SimpleMailMessage email = new SimpleMailMessage();
//		email.setTo(recipientAddress);
//		email.setSubject(subject);
//		email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
//		mailSender.send(email);
//	}



	public String sendEmail(EventoVerificacion event) {
		
		
		Usuario usuario = event.getUsuario();
		String token = UUID.randomUUID().toString();
		VerificationToken nuevoToken = new VerificationToken(token, usuario);
		verificationTokenRepository.save(nuevoToken);
		

		String recipientAddress = usuario.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/registroConfirmado?token=" + token;
		String message = "¡Registro completado! Por favor, finaliza la verificación accediendo al siguiente enlace:\n";
		
		
		
		Email from = new Email();
		from.setEmail("waveit.notification@gmail.com");
		Email to = new Email(recipientAddress);

		Content content = new Content("text/html", "I'm replacing the <strong>body tag</strong><p style='color:red'></p>" + message+"<a href='"+confirmationUrl+"'>pulsa aqui </a");

		Mail mail = new Mail(from, subject, to, content);

		mail.setReplyTo(new Email("waveit.notification@gmail.com"));


		try {
			//Mail mail = prepareMail(email);
			

			Request request = new Request();

			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");

			request.setBody(mail.build());

			Response response = sendGrid.api(request);

			if (response != null) {

				System.out.println("response code from sendgrid" + response.getHeaders());

			}

		} catch (IOException e) {

			e.printStackTrace();
			return "error in sent mail!";
		}

		return "mail has been sent check your inbox!";

	}

	//public Mail prepareMail(String email) {

//		Mail mail = new Mail();
//		
//		Email fromEmail = new Email();
//		
//		fromEmail.setEmail("waveit.notification@gmail.com");
//		
//		mail.setFrom(fromEmail);
//		Email to = new Email();
//		to.setEmail(email);
//		
//		Personalization personalization = new Personalization();
//		
//		personalization.addTo(to);
//		mail.addPersonalization(personalization);
//		
//		mail.setTemplateId(templateId);
		
		// mail.personalization.get(0).addSubstitution("-username-", "Some blog user");
		// mail.setTemplateId(templateId);

//		Email from = new Email();
//		from.setEmail("waveit.notification@gmail.com");
//		String subject = "Hello World!";
//		Email to = new Email("waveit.notification@gmail.com");
//		String message = "jajajajaj";
//
//		Content content = new Content("text/html", "I'm replacing the <strong>body tag</strong><p style='color:red'></p>" + message);
//
//		Mail mail = new Mail(from, subject, to, content);
//
//		mail.setReplyTo(new Email("waveit.notification@gmail.com"));
//		
//
//		return mail;
//	}

}
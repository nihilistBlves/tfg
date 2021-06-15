package org.tfg.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tfg.domain.Usuario;
import org.tfg.domain.VerificationToken;
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
public class EmailService {

	@Autowired
	private SendGrid sendGrid;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	public String sendEmail(Usuario usuario, String appUrl) {
		String token = UUID.randomUUID().toString();
		VerificationToken nuevoToken = new VerificationToken(token, usuario);
		verificationTokenRepository.save(nuevoToken);
		
		Email fromEmail = new Email();
		fromEmail.setEmail("waveit.notification@gmail.com");
		
		Email to = new Email();
		to.setEmail(usuario.getEmail());
		
		Personalization personalization = new Personalization();
		
		personalization.addTo(to);

		Content content = new Content("text/html","<a href='"+appUrl+"//registroConfirmado?token="+token+"'></a>");	
		

		Mail mail = new Mail(fromEmail,"Notificacion WaveIt",to,content);
		

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

			System.out.println(e);
			return "error in sent mail!";
		}

		return "mail has been sent check your inbox!";

	}


}
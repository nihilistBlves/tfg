package org.tfg.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	@Value("${app.sendgrid.templateId}")
	private String templateId;
	@Autowired
	SendGrid sendGrid;

	public String sendEmail(String email) {
		
		Email fromEmail = new Email();
		fromEmail.setEmail("waveit.notification@gmail.com");
		

		
		Email to = new Email();
		to.setEmail(email);
		
		Personalization personalization = new Personalization();
		
		personalization.addTo(to);

		Content content = new Content("text/html","<h1>Hola esto es un ejemplo</h1><h2>Este es el seguundo parametro que no se para que es</h2>");	
		

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

			e.printStackTrace();
			return "error in sent mail!";
		}

		return "mail has been sent check your inbox!";

	}


}
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

		try {
			Mail mail = prepareMail(email);

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

	public Mail prepareMail(String email) {

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

		Email from = new Email();
		from.setEmail("waveit.notification@gmail.com");
		String subject = "Hello World!";
		Email to = new Email("waveit.notification@gmail.com");
		String message = "jajajajaj";

		Content content = new Content("text/html", "I'm replacing the <strong>body tag</strong>" + message);

		Mail mail = new Mail(from, subject, to, content);

		mail.setReplyTo(new Email("waveit.notification@gmail.com"));
		// mail.personalization.get(0).addSubstitution("-username-", "Some blog user");
		// mail.setTemplateId(templateId);

		return mail;
	}

}
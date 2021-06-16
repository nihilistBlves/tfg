
package org.tfg.service;
//
//import java.io.IOException;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.tfg.domain.Usuario;
//import org.tfg.domain.VerificationToken;
//import org.tfg.repositories.VerificationTokenRepository;
//
//import com.sendgrid.Method;
//import com.sendgrid.Request;
//import com.sendgrid.Response;
//import com.sendgrid.SendGrid;
//import com.sendgrid.helpers.mail.Mail;
//import com.sendgrid.helpers.mail.objects.Content;
//import com.sendgrid.helpers.mail.objects.Email;
//
//@Service
//public class EmailService {
//
//	@Autowired
//	private SendGrid sendGrid;
//
//	@Autowired
//	private VerificationTokenRepository verificationTokenRepository;
//
//	public void sendEmail(Usuario usuario) {
//		String token = UUID.randomUUID().toString();
//		VerificationToken nuevoToken = new VerificationToken(token, usuario);
//		verificationTokenRepository.save(nuevoToken);
//
//		Email fromEmail = new Email();
//		fromEmail.setEmail("waveit.notification@gmail.com");
//
//		Email to = new Email();
//		to.setEmail(usuario.getEmail());
//
//		String contentString = "Por favor, haz click en este link para activar tu cuenta: <a href='https://waveit.herokuapp.com/registroConfirmado?token=" + token + "'>https://waveit.herokuapp.com/registroConfirmado?token=" + token + "</a>";
//
//		System.out.println(contentString);
//
//		Content content = new Content("text/html", contentString);
//
//		Mail mail = new Mail(fromEmail, "WAVEIT! Registro confirmado", to, content);
//
//		try {
//
//			Request request = new Request();
//
//			request.setMethod(Method.POST);
//			request.setEndpoint("mail/send");
//
//			request.setBody(mail.build());
//
//			Response response = sendGrid.api(request);
//
//			if (response != null) {
//
//				System.out.println("response code from sendgrid" + response.getHeaders());
//
//			}
//
//		} catch (IOException e) {
//			System.out.println(e);
//		}
//	}
//
//}
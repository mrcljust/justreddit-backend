package de.justitsolutions.justreddit.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import de.justitsolutions.justreddit.exception.JustRedditException;
import de.justitsolutions.justreddit.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j //Logger
public class MailService {
	
	private final JavaMailSender mailSender;
	private final MailContentBuilder mailContentBuilder;

	@Async
	public void sendMail(NotificationEmail notificationEmail) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("justreddit@just-it-solutions.de");
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());
			messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()), true);
		};
		try {
			mailSender.send(messagePreparator);
			log.info("Activation email successfully sent to " + notificationEmail.getRecipient());
		} catch (MailException e) {
			log.info("Exception occured when sending activation email to " + notificationEmail.getRecipient());
			throw new JustRedditException("Exception occured when sending activation email to " + notificationEmail.getRecipient());
		}
	}
}

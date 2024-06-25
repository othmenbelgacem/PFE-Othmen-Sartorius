package com.sartorius.tma.business.services.email;

import static com.sartorius.tma.utils.Constants.BACKEND_BASE_URL;
import static com.sartorius.tma.utils.Constants.FRONT_BASE_URL;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.sartorius.tma.dtos.EmailDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
@Slf4j
public class EmailService {

	@Value("${tma.front.base.url}")
	private String frontBaseUrl;

	@Value("${tma.front.admin.base.url}")
	private String frontAdminBaseUrl;

	@Value("${tma.backend.base.url}")
	private String backendBaseUrl;

	@Value("${spring.mail.username}")
	private String smtpMailUsername;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${tma.app.base-url}")
	private String baseUrl;

	@Autowired
	private SpringTemplateEngine thymeleafTemplateEngine;

	public EmailService() {
	}

	/**
	 * send email
	 *
	 * @param emailDto: email content and config
	 */
	@Async
	public void sendMail(EmailDto emailDto, List<String> destinations) {
		destinations.stream().forEach(destination -> {

			emailDto.getMaps().put(FRONT_BASE_URL, frontBaseUrl);
			emailDto.getMaps().put(BACKEND_BASE_URL, backendBaseUrl);
			try {

				Context thymeleafContext = new Context();
				thymeleafContext.setVariables(emailDto.getMaps());
				String htmlBody = thymeleafTemplateEngine.process(emailDto.getTemplateName(), thymeleafContext);

				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				// helper.setTo(emailDto.getTo());
				helper.setTo(destination);
				helper.setFrom(smtpMailUsername);
				helper.setSubject(emailDto.getSubject());
				helper.setText(htmlBody, true);

				// Add exist attachments to email
				emailDto.getAttachments().entrySet().forEach(attachment -> {
					try {
						helper.addAttachment(attachment.getKey(), new ByteArrayResource(attachment.getValue()));
					} catch (MessagingException e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				});

				mailSender.send(message);

			} catch (Exception e) {
				e.printStackTrace();
				log.error("Exception when send Email: {}", e.getMessage());
			}
		});

	}
	public void sendEmail(String to, String subject, String body) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);


		Resource resource = new ClassPathResource("templates/logo.png");
		InputStreamSource logo = resource::getInputStream;
		helper.addInline("logo", logo, "image/png");

		mailSender.send(mimeMessage);
	}


}

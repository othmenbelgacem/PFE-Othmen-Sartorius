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


	@Autowired
	private JavaMailSender mailSender;



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

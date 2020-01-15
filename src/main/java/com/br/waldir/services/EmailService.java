package com.br.waldir.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.br.waldir.domain.User;

@Service
public interface EmailService {
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(User user, String newPass);
}
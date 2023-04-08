package com.choongang.scheduleproject.mail.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {
	public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException; // 메일 생성
	public String createKey(); // 인증키 생성
	public String sendSimpleMessage(String to, String joinResetFind) throws Exception; // 메일 전송
}
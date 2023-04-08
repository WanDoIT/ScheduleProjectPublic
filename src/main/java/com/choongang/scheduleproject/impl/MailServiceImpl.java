package com.choongang.scheduleproject.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.choongang.scheduleproject.command.EmailVO;
import com.choongang.scheduleproject.mail.service.MailService;
import com.choongang.scheduleproject.user.service.UserMapper;

import lombok.RequiredArgsConstructor;

@PropertySource("classpath:application.properties")
@Service("mailService")
@RequiredArgsConstructor
//@Slf4j log.info를 주석처리하여 현재는 사용하지 않음
public class MailServiceImpl implements MailService {
	private final JavaMailSender javaMailSender;
	private final UserMapper userMapper;
	
	//인증코드 - 생성은 메일을 보낼 때 createKey()를 이용해 ePw에 담아줄 것
    private String ePw;
    
    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to)throws MessagingException, UnsupportedEncodingException {
        //log.info("보내는 대상 : "+ to);
        //log.info("인증 번호 : " + ePw);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("Project Management 인증 코드"); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 인증코드</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 인증코드를 3분 안에 입력 후 인증하기 버튼을 눌러주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id,"Project_Management_Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름
        return message;
    }

    // 인증코드 만들기
    public String createKey() {
    	//랜덤 인증코드 생성
        Random random = new Random(); 
        int length = 6;
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(3);
            switch(choice) {
                case 0:
                    key.append((char)((int)random.nextInt(25)+97));
                    break;
                case 1:
                    key.append((char)((int)random.nextInt(25)+65));
                    break;
                case 2:
                    key.append((char)((int)random.nextInt(10)+48));
                    break;
                default:
                    break;
            }
        }
        return key.toString();
    }
    
    /*
    메일 발송
    sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소
    MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
    bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
    */
    public String sendSimpleMessage(String to, String joinResetFind) throws Exception { // 이메일 보내기
    	ePw = createKey(); //메일을 보낼 때 랜덤문자열을 생성하여 보낼 때마다 다른 값을 넘기도록 설정
        MimeMessage message = createMessage(to);
        try {
            EmailVO vo = new EmailVO();
            LocalDateTime expireTime = LocalDateTime.now().plusMinutes(3); //시간제한 3분 
            vo.setEmail(to);
            vo.setCode(ePw);
            vo.setExpireTime(expireTime);
            vo.setJoinResetFind(joinResetFind); // 회원가입인지 비밀번호 초기화인지 구별하기 위함
            int result = userMapper.sendVerifyCode(vo); //DB에 저장
            if(result == 0) { //db에 저장 실패
    			//RedirectAttributes를 여기서 사용할 수 없어서 userEmailError로 이동 후 처리
    			return "redirect:/user/user-email-error";
            }
            javaMailSender.send(message); // 메일 발송
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }
}

package com.example.smartmyhome.service;
import com.example.smartmyhome.dto.MailDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class SendMailService {

    private JavaMailSender emailSender;

    public void sendSimpleMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("119vkfks@naver.com");
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        emailSender.send(message);
    }

    // 메일전송 메소드
    public void mailsend(){
        try {   // 이메일 전송
            MimeMessage message = emailSender.createMimeMessage();
            // 0. Mime 설정
            MimeMessageHelper mimeMessageHelper
                    = new MimeMessageHelper( message , true, "utf-8"); // 예외처리 발생
            // 1. 보내는사람
            mimeMessageHelper.setFrom("119vkfks@naver.com" , "ㅎㅇㅇ");
            // 2. 받는 사람
            mimeMessageHelper.setTo("119vkfks@gmail.com");
            // 3. 메일 제목
            mimeMessageHelper.setSubject("안녕하세요");
            // 4. 메일 내용
            mimeMessageHelper.setText( "안녕하세요안녕하세요" , true);
            // 5. 메일 전송
            emailSender.send(  message );

        }catch( Exception e  ){
            System.out.println("메일전송 실패 : "+ e );
        }
    }

}

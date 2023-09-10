package likelion.running.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;
@Slf4j
@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender emailSender;

    public static final String ePw = generateRandomString(10);

    private MimeMessage generateMessage(String to)throws Exception{
        log.info("보내는 대상 :"+ to);
        log.info("인증 번호 :"+ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("이메일 인증");

        String text="";
        text +="<div style='margin:20px;'>" +
                "<h1> 뛰러 왔는데 비밀 번호를 잊었슈? <h1>" +
                "<br>" +
                "<p>아래 코드를 복사해 입력 해주세요<p>" +
                "<br>" +
                "<div align='center' style='border:1px solid black: font-family:verdana';>" +
                "<h3 style='color:blue;'>비밀번호 재설정 인증 코드입니다.</h3>" +
                "<div style='font-size:130%'>" +
                "CODE : <strong>" +
                ePw+"<strong><div><br/> " +
                "</div>";
        message.setText(text,"utf-8","html");
        message.setFrom(new InternetAddress("parkjkjk010@gamil.com","runningLion"));//보내는 사람

        return message;
    }


    public static String generateRandomString(int length) {
        // 무작위 문자열을 생성할 문자열 세트
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // 문자열 생성기를 위한 랜덤 객체 생성
        Random random = new Random();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 문자열 세트에서 무작위 문자 선택
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);

            // 선택한 문자를 문자열에 추가
            sb.append(randomChar);
        }

        return sb.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {
        MimeMessage message = generateMessage(to);
        try {
            emailSender.send(message);
        }catch (MailException ex){
            log.info(ex.getMessage());
            throw new IllegalArgumentException();
        }
        return ePw;
    }
}

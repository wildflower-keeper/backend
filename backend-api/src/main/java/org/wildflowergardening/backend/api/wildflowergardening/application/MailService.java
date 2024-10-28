package org.wildflowergardening.backend.api.wildflowergardening.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.wildflowergardening.backend.api.wildflowergardening.util.RandomCodeGenerator;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Transactional
    public void sendMail(String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String code = RandomCodeGenerator.generate();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[들꽃지기] 로그인 인증번호 안내");

            Context context = new Context();
            context.setVariable("code", code);

            String htmlContent = templateEngine.process("mailFormat", context);
            mimeMessageHelper.setText(htmlContent);

            javaMailSender.send(message);

            //TODO : 데이터베이스에 저장

        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패");
        }
    }

    //TODO : 인증 코드 확인(boolean)
}

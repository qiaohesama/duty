package com.mnnu.duty;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.annotation.Resource;

@SpringBootTest
class DutyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    MailSender mailSender;

    @Test
    void sendEmail() {
        // 2.准备发送邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 发送者和收件者
        mailMessage.setFrom("qiaohesama@163.com");
        mailMessage.setTo("2318928417@qq.com");
        String title = "505机房的值日信息";
        mailMessage.setSubject(title);
        mailMessage.setText("今天轮到你和" + "lzp" + "一起值日，请记得倒垃圾和扫地噢");
        try {
            // 3.发送
            mailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}

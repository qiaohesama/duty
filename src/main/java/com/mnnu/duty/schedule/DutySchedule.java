package com.mnnu.duty.schedule;

import org.springframework.core.io.ClassPathResource;

import com.mnnu.duty.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author qiaoh
 */
@Component
public class DutySchedule {

    @Resource
    MailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static List<Member> members = new ArrayList<>();

    static {

        ClassPathResource resource = new ClassPathResource("./info.properties");
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        entries.forEach(e -> {
            Member member = new Member();
            member.setName(String.valueOf(e.getKey()));
            member.setEmail(String.valueOf(e.getValue()));
            members.add(member);
        });

        System.out.println(members);
    }


    /**
     * 每周四和每周日打扫卫生
     * 摇两个同学
     */
    @Scheduled(cron = "0 0 10 * * 1,5")
    void sendDuty() {
        // 1.准备摇号
        Collections.shuffle(members);
        Member member = members.get(0);
        Member member1 = members.get(members.size() - 1);

        sendEmail(member1.getName(), member.getEmail());
        sendEmail(member.getName(), member1.getEmail());

    }

    void sendEmail(String m1, String mEmail) {
        // 2.准备发送邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 发送者和收件者
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(mEmail);
        String title = "505机房的值日信息";
        mailMessage.setSubject(title);
        mailMessage.setText("今天轮到你和" + m1 + "一起值日，请记得倒垃圾和扫地噢");
        try {
            // 3.发送
            mailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}

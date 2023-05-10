package cn.xueden.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author:梁志杰
 * @date:2023/3/31
 * @description:cn.xueden.email
 * @version:1.0
 */
@Component
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     *
     * @param form
     *        邮件发送者
     * @param to
     *        收件人
     * @param cc
     *        抄送人
     * @param subject
     *        邮件主题
     * @param content
     *        邮件内容
     */
    public void sendSimpleMail(String form,String to,String cc,String subject,String content){
        //  简单有效直接构建
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(form);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setCc(cc);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);

    }
}

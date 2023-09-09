package cn.xueden.email;

import cn.xueden.edu.domain.EduEmail;
import jakarta.mail.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author:梁志杰
 * @date:2023/3/31
 * @description:cn.xueden.email
 * @version:1.0
 */
@Component
public class MailService {

    private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    /**
     *
     * @param mailConfig
     *        邮件发送配置信息
     * @param to
     *        收件人
     * @param cc
     *        抄送人
     * @param subject
     *        邮件主题
     * @param content
     *        邮件内容
     */
    public void sendSimpleMail(EduEmail mailConfig, String to, String cc, String subject, String content){
        // 使用配置信息设置邮件发送参数
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(Integer.parseInt(mailConfig.getPort()));
        mailSender.setUsername(mailConfig.getUserName());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = new Properties();
        // 开启SMTP服务器认证
        props.setProperty("mail.smtp.auth", "true");
        // 启用SSL加密
        props.setProperty("mail.smtp.ssl.enable", "true");
        // 使用SSL加密连接
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSender.setJavaMailProperties(props);


        //  简单有效直接构建
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailConfig.getUserName());
        simpleMailMessage.setTo(to);
        simpleMailMessage.setCc(cc);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        mailSender.send(simpleMailMessage);

    }
}

package pro.inmost.vacancydiary.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import pro.inmost.vacancydiary.config.EmailConfiguration;

@Component
public class EmailFactory {
    private final EmailConfiguration emailConfig;

    @Autowired
    public EmailFactory(EmailConfiguration emailConfig) {
        this.emailConfig = emailConfig;
    }

    public JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        return mailSender;
    }

    public SimpleMailMessage createMessage(String mailFrom, String mailTo, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(mailTo);
        mailMessage.setText(message);

        return mailMessage;
    }
}

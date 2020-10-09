package ekol.currency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {

    private static final String NAMESPACE_PREFIX = "ekolMailSender";

    @Value("${" + NAMESPACE_PREFIX + ".host}")
    private String host;

    @Value("${" + NAMESPACE_PREFIX + ".port}")
    private int port;

    @Value("${" + NAMESPACE_PREFIX + ".username}")
    private String username;

    @Value("${" + NAMESPACE_PREFIX + ".password}")
    private String password;

    @Value("${" + NAMESPACE_PREFIX + ".properties.mail.transport.protocol}")
    private String mail_transport_protocol;

    @Value("${" + NAMESPACE_PREFIX + ".properties.mail.smtp.auth}")
    private String mail_smtp_auth;

    @Value("${" + NAMESPACE_PREFIX + ".properties.mail.smtp.starttls.enable}")
    private String mail_smtp_starttls_enable;

    @Value("${" + NAMESPACE_PREFIX + ".properties.mail.debug}")
    private String mail_debug;

    @Bean
    public JavaMailSender defaultMailSender() {
        return ekolMailSender();
    }

    public JavaMailSender ekolMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mail_transport_protocol);
        props.put("mail.smtp.auth", mail_smtp_auth);
        props.put("mail.smtp.starttls.enable", mail_smtp_starttls_enable);
        props.put("mail.debug", mail_debug);

        return mailSender;
    }

    public String getUsername() {
        return username;
    }
}

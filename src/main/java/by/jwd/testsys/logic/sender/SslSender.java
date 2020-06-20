package by.jwd.testsys.logic.sender;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SslSender {

    private String username;
    private String password;
    private Properties props;

    private static final SslSender instance = new SslSender();

    public static SslSender getInstance() {
        return instance;
    }

    public void inializeSalSender(Properties props) {
        this.props = props;
        this.username = props.getProperty("mail.username");
        this.password = props.getProperty("mail.password");
    }

    private SslSender() {}

    public void send(String subject, String text, String toEmail) {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(subject);
            //текст
            message.setText(text);

            //отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}

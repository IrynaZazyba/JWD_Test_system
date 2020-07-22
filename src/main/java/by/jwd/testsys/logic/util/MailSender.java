package by.jwd.testsys.logic.util;

import by.jwd.testsys.logic.exception.FaildSendMailException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private String username;
    private String password;
    private Properties props;

    private static final MailSender instance = new MailSender();

    public static MailSender getInstance() {
        return instance;
    }

    public void initializeSalSender(Properties props) {
        this.props = props;
        this.username = props.getProperty("mail.username");
        this.password = props.getProperty("mail.password");
    }

    private MailSender() {}

    public void send(String subject, String text, String toEmail) throws FaildSendMailException {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new FaildSendMailException("Exception on attempt to send mail",e);
        }
    }

}

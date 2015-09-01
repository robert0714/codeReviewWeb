package com.iisigroup.java.tech.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class GmailUtils.
 */
public class GmailUtils {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(GmailUtils.class);

    /** The Constant username. */
    private static final String username = "iisi.sonar@gmail.com";

    /** The Constant password. */
    private static final String password = "0920301309";

    /**
     * Instantiates a new gmail utils.
     */
    private GmailUtils() {
    }

    /**
     * Creates the session.
     * 
     * @return the session
     */
    public static Session createSession() {
        final Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        final Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }

    /**
     * Creates the message.
     * 
     * @param recipient
     *            the recipient
     * @param subject
     *            the subject
     * @param content
     *            the content
     * @param session
     *            the session
     * @return the message
     * @throws AddressException
     *             the address exception
     * @throws MessagingException
     *             the messaging exception
     */
    public static Message createMessage(final String recipient,
            final String subject, final String content, final Session session)
            throws AddressException, MessagingException {
        LOGGER.info("preparation for createMessage to {}....." , recipient);
        final Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("iisi.sonar@gmail.com"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
}

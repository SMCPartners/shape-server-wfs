package com.smcpartners.shape.shapeserver.crosscutting.email;

import com.smcpartners.shape.shapeserver.shared.dto.common.MailDTO;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. EJB to send out emails. Uses the Apache commons email library</br>
 * <p>
 * Created by johndestefano on 10/6/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class SendMailService {

    @Inject
    private Logger log;

    @Resource(mappedName = "java:jboss/mail/ShapeMail")
    private Session session;

    /**
     * Mail from address
     */
    @Inject
    @ConfigurationValue("${com.smc.server-core.MAIL_FROM_ADDRESS}")
    private String fromAddress;

    /**
     * Default constructor
     */
    public SendMailService() {
    }

    /**
     * Send an mail message
     *
     * @param mailDTO
     * @throws Exception
     */
    public void sendEmailMsg(MailDTO mailDTO) throws Exception {

        Message message = new MimeMessage(session);
        message.setFrom();
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDTO.getToEmail(), false));
        message.setSubject(mailDTO.getSubject());
        message.setSentDate(new Date());
        message.setContent(mailDTO.getMessage(), "text/html; charset=UTF-8");
        Transport.send(message);
    }

}

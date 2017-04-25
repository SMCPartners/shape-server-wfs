package com.smcpartners.shape.shapeserver.crosscutting.activitylogging;

import com.smcpartners.shape.shapeserver.crosscutting.activitylogging.dto.ClickLogDTO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.ClickLogDAO;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Process messages on the JumpActivityQueue</br>
 * <p>
 * Created by johndestefano on 10/3/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@MessageDriven(name = "ShapeActivityQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/queue/ShapeActivityQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class ShapeActivityQueueMDB implements MessageListener {

    @Inject
    private Logger log;

    @EJB
    private ClickLogDAO clickLogDAO;

    /**
     * Message processing method
     *
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) message;
                ClickLogDTO dto = (ClickLogDTO) om.getObject();
                clickLogDAO.create(dto);
            } else {
                throw new JMSException("Wrong message type");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "onMessage", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

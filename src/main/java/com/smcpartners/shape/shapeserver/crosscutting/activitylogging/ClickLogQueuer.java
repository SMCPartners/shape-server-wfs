package com.smcpartners.shape.shapeserver.crosscutting.activitylogging;


import com.smcpartners.shape.shapeserver.crosscutting.activitylogging.dto.ClickLogDTO;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;

/**
 * Responsible:</br>
 * 1. Puts ClickLogDTO messages on the ShapeActivityQueue</br>
 * <p>
 * Created by johndestefano on 10/3/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class ClickLogQueuer {

    @Resource(mappedName = "java:/jms/queue/ShapeActivityQueue")
    private Destination queue;

    @Inject
    private JMSContext context;

    /**
     * Constructor
     */
    public ClickLogQueuer() {
    }

    /**
     * Queue message up
     *
     * @param dto
     * @throws Exception
     */
    public void sendMessage(ClickLogDTO dto) throws Exception {
        context.createProducer().send(queue, dto);
    }
}

package com.smcpartners.shape.shapeserver;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.mail.MailFraction;

/**
 * Responsibility: </br>
 * 1. Build, start, and deploy the swarm.</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/5/17</br>
 * <p>
 *     Changes:</br>
 *     1. Moved fraction configuration to config file - 6/25/17 - johndestefano</br>
 * </p>
 */
public class Main {

    /**
     * Entry point.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Running " + Main.class.getCanonicalName() + ".main");

        // Create the Swarm
        Swarm swarm = new Swarm();

        // Mail
        // Need this until setting up mail with config file is clear
        swarm.fraction(new MailFraction().smtpServer("ShapeMail",
                s -> s.host("smtp.gmail.com")
                        .port(587)
                        .tls(true)
                        .password("q1w2e3r4!")
                        .username("smctestemails@gmail.com")
        ));

        // Start and deploy WAR
        swarm.start().deploy();
    }
}

package com.smcpartners.shape.shapeserver;

import org.wildfly.swarm.Swarm;

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

        // Start and deploy WAR
        swarm.start().deploy();
    }
}

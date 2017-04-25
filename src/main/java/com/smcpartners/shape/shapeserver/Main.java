package com.smcpartners.shape.shapeserver;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.mail.MailFraction;
import org.wildfly.swarm.messaging.MessagingFraction;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/5/17
 */
public class Main {

    public Main() {
    }

    protected void build() throws Exception {
        Swarm swarm = new Swarm();

        // Configure the swarm subsystem with a driver
        // and datasources for shape and embedded keycloak
        swarm.fraction(new DatasourcesFraction()
                .jdbcDriver("com.mysql", (d) -> {
                    d.driverClassName("com.mysql.jdbc.Driver");
                    d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
                    d.driverModuleName("com.mysql");
                })
                .dataSource("shapeDS", (ds) -> {
                    ds.driverName("com.mysql");
                    ds.connectionUrl("jdbc:mysql://localhost:3306/shape");
                    ds.userName("shape");
                    ds.password("shape");
                }));

        // Configure JMS
        swarm.fraction(MessagingFraction.createDefaultFraction()
                .defaultServer((s) ->
                        s.jmsQueue("ShapeActivityQueue")));

        // Mail
        swarm.fraction(new MailFraction().smtpServer("ShapeMail",
                s -> s.host("localhost")
                        .port(24)
                        .tls(true)
                        .password("somepassword")
                        .username("someusername")
        ));

        // Start and deploy WAR
        swarm.start().deploy();
    }

    /**
     * Entry point.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Running " + Main.class.getCanonicalName() + ".main");

        // Create Main class and call build. This way
        // we can take advantage of injection.
        new Main().build();
    }
}

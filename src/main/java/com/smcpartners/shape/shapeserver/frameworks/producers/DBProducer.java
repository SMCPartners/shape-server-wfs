package com.smcpartners.shape.shapeserver.frameworks.producers;

import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Responsible:<br/>
 * 1. Producer mapping for data dao<br/>
 * <p>
 * Created by johndestefano on 9/10/15.
 * <p>
 * Changes:<b/>
 */
@ApplicationScoped
public class DBProducer {

    @Produces
    @PersistenceContext(unitName = "shape")
    @ShapeDatabase
    EntityManager shape;

    //@Produces
    //@PersistenceContext(unitName = "aerogears")
    //@AeroGearsDatabase
    //private EntityManager aerogears;

    /*
     @PersistenceUnit(unitName = "jump")
     private EntityManagerFactory emf;

     @Produces
     @RequestScoped
     @JumpDatabase protected EntityManager createEntityManager() {
     return this.emf.createEntityManager();
     }

     protected void closeEntityManager(@Disposes @JumpDatabase EntityManager em) {
     if (em.isOpen()) {
     em.close();
     }
     }

     @PersistenceUnit(unitName = "aerogears")
     private EntityManagerFactory emf2;

     @Produces
     @RequestScoped
     @AeroGearsDatabase protected EntityManager createEntityManager2() {
     return this.emf2.createEntityManager();
     }

     protected void closeEntityManager2(@Disposes @AeroGearsDatabase EntityManager em) {
     if (em.isOpen()) {
     em.close();
     }
     }
     */
}

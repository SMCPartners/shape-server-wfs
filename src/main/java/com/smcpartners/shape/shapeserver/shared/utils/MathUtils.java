package com.smcpartners.shape.shapeserver.shared.utils;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;

/**
 * Responsible: Some random math utils<br/>
 * 1. <br/>
 * <p>
 * Created by johndestefano on 4/5/16.
 * </p>
 * <p>
 * Changes:<br/>
 * 1. <br/>
 * </p>
 */
@ApplicationScoped
public class MathUtils {

    public int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).findFirst().getAsInt();
    }
}

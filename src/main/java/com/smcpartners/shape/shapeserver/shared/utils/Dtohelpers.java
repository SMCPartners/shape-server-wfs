package com.smcpartners.shape.shapeserver.shared.utils;

import java.util.Map;

/**
 * Responsible:<br/>
 * 1. Helper class to support DTO usage
 * <p>
 * Created by johndestefano on 10/7/15.
 * <p>
 * <p>
 * Changes:<b/>
 * </p>
 */
public class Dtohelpers {

    public static <T> T getValue(Map<String, Object> map, Class<T> clazz, String key) throws Exception {
        // Get the value
        Object value = map.get(key);

        // Is it valid
        if (value == null) {
            throw new Exception("Value not in map.");
        }

        // Type cast it
        return clazz.cast(value);
    }
}

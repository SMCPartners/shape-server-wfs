package com.smcpartners.shape.shapeserver.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsibility: Hold encoded tokens used in the Double Cookie Security mechanism</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/23/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubleCookieTokenPair {
    private String xToken;
    private String jwtToken;
}

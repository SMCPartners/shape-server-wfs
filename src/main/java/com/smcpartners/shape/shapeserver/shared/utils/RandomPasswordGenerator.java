package com.smcpartners.shape.shapeserver.shared.utils;

import java.util.Random;

/**
 * Responsible: Generate randon passwords that meet the systems passowrd requirements<br/>
 * 1. Generate a random password
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
public class RandomPasswordGenerator {
    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM = "0123456789";
    private static final String SPL_CHARS = "!@#$%^&*_=+-/";

    /**
     * Constructor
     *
     */
    public RandomPasswordGenerator() {
    }

    /**
     * Default for app (1 cap, 8 to 10 in length, 1 number)
     *
     * @return
     * @throws Exception
     */
    public static String generateApplicationDefaultPwd() throws Exception {
        return new String(generatePasswd(8, 10, 1, 1, 0));
    }

    /**
     * Generic generator
     *
     * @param minLen
     * @param maxLen
     * @param noOfCAPSAlpha
     * @param noOfDigits
     * @param noOfSplChars
     * @return
     * @throws Exception
     */
    private static char[] generatePasswd(int minLen, int maxLen, int noOfCAPSAlpha,
                               int noOfDigits, int noOfSplChars) throws Exception {
        if (minLen > maxLen) {
            throw new Exception("Min. Length > Max. Length!");
        }

        if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen) {
            throw new Exception(
                    "Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
        }

        Random rnd = new Random();
        int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
        char[] pswd = new char[len];
        int index = 0;

        for (int i = 0; i < noOfCAPSAlpha; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
        }

        for (int i = 0; i < noOfDigits; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
        }

        for (int i = 0; i < noOfSplChars; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
        }

        for (int i = 0; i < len; i++) {
            if (pswd[i] == 0) {
                pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
            }
        }
        return pswd;
    }

    /**
     * Used by generator (generatePswd)
     *
     * @param rnd
     * @param len
     * @param pswd
     * @return
     */
    private static int getNextIndex(Random rnd, int len, char[] pswd) {
        int index = rnd.nextInt(len);
        while (pswd[index = rnd.nextInt(len)] != 0)
            ;
        return index;
    }
}

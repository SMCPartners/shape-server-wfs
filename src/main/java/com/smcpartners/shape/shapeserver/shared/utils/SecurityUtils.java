/*******************************************************************************
 * Copyright (c) 2013 Pronoia Health LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Pronoia Health LLC - initial API and implementation
 *******************************************************************************/
package com.smcpartners.shape.shapeserver.shared.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Responsible: Utilities used in application security<br/>
 *
 * <p>
 * Changes:<br/>
 * 1. Capitalized REGEXP - jjdestef3 - 3/15/16<br/>
 * </p>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 */
public class SecurityUtils {

    private static int ITERATIONS = 5;

    /**
     * Password regex</br>
     * One number, one lower case letter, one uppercase letter, one special character (?, !)
     *
     */
    private static String REGEXP = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).{6,10}";

    /**
     * From a base 64 representation, returns the corresponding byte[]
     *
     * @param data String The base64 representation
     * @return byte[]
     * @throws IOException
     */
    public static byte[] base64ToByte(String data) throws IOException {
        return Base64.getDecoder().decode(data);
    }

    /**
     * From a byte[] returns a base 64 representation
     *
     * @param data byte[]
     * @return String
     * @throws IOException
     */
    public static String byteToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * From a password, a number of iterations and a salt, returns the
     * corresponding digest
     *
     * @param password String The password to encrypt
     * @param salt     byte[] The salt
     * @return byte[] The digested password
     * @throws NoSuchAlgorithmException If the algorithm doesn't exist
     */
    public static byte[] getHash(String password, byte[] salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < ITERATIONS; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }

    /**
     * Create salted password and salt for database storage
     *
     * @param password
     * @return
     * @throws Exception
     */
    public static SaltedPassword genSaltedPasswordAndSalt(String password)
            throws Exception {
        if (password.length() > 0 && password.length() <= 32) {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            // Salt generation 64 bits long
            byte[] bSalt = new byte[8];
            random.nextBytes(bSalt);

            // Digest computation
            byte[] bDigest = getHash(password, bSalt);
            String sDigest = byteToBase64(bDigest);
            String sSalt = byteToBase64(bSalt);
            return new SaltedPassword(sDigest, sSalt);
        } else {
            throw new Exception("Password must be between 1 and 32 characters.");
        }
    }

    /**
     * Validate the password against the digest. If computeOnly then we are just
     * doing a computation on a password and digest we know are incorrect. This
     * is to confuse attackers
     *
     * @param pwd
     * @param digest
     * @param salt
     * @param computeOnly
     * @return
     * @throws Exception
     */
    public static boolean validatePassword(String pwd, String digest,
                                           String salt, boolean computeOnly) throws Exception {
        // If compute only or any of the inputs are null then just do
        // the computation. This confuses attackers
        if (computeOnly == true || pwd == null || salt == null
                || digest == null) {
            digest = "000000000000000000000000000=";
            salt = "00000000000=";
        }

        // decode base64
        byte[] bDigest = base64ToByte(digest);
        byte[] bSalt = base64ToByte(salt);

        // Compute the new DIGEST
        byte[] proposedDigest = getHash(pwd, bSalt);

        // Check digested password against what is in the digests
        return Arrays.equals(proposedDigest, bDigest) && !computeOnly;
    }

    /**
     * Validate the password against the above requirements
     *
     * @param pwd
     * @return
     * @throws Exception
     */
    public static boolean checkPasswordCompliance(String pwd) throws Exception {
        boolean valid = pwd.matches(REGEXP);
        return valid;
    }

    /**
     * Test
     *
     * @param args
     */
    public static void main(String[] args) {
        String pwd = "Q1w2e3r4!";
        try {
            // Generate digest
            SaltedPassword sPwd = genSaltedPasswordAndSalt(pwd);
            System.out.println("Password Digest: " + sPwd.getPwdDigest() + "\n");
            System.out.println("Salt: " + sPwd.getSalt() + "\n");

            // Test that it works
            boolean valid = validatePassword(pwd, sPwd.getPwdDigest(), sPwd.getSalt(), false);
            System.out.println("Password check Ok: " + valid);

            // Test pwd
            String password = "Q1w2e3r4!";
            System.out.println(checkPasswordCompliance(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

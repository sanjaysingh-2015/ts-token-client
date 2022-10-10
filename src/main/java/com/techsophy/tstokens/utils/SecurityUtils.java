package com.techsophy.tstokens.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class SecurityUtils {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateAuthCode() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String generateCode(String organizationName, Integer length) {
        Long orgNo = (long)(Math.floor(Math.random() * (9*Math.pow(10,length-1))) + Math.pow(10,(length-1)));
        return shuffleString(organizationName.toUpperCase().replace(" ","")) + orgNo.toString();
    }

    private static String shuffleString(String organizationName) {
        List<String> letters = Arrays.asList(organizationName.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled.substring(0,3);
    }
}

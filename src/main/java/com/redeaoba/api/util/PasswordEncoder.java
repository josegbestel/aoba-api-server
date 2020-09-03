package com.redeaoba.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    public static String encode(String pass){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passEncrypt = passwordEncoder.encode(pass);
        return passEncrypt;
    }
}

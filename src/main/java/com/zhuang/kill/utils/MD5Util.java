package com.zhuang.kill.utils;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class MD5Util {
    public static String md5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        Base64.Encoder encoder = Base64.getEncoder();
        String result = encoder.encodeToString(md5.digest(str.getBytes("utf-8")));
        return result;
    }
}

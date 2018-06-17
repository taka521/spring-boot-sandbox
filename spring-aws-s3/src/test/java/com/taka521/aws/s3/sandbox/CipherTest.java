package com.taka521.aws.s3.sandbox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.crypto.Cipher;

@RunWith(JUnit4.class)
public class CipherTest {

    @Test
    public void check() throws Exception {
        // 最大キー長を確認
        System.out.println("AES : " + Cipher.getMaxAllowedKeyLength("AES"));
    }

}

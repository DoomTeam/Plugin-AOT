package com.uama.asm;


import com.uama.asm.entity.Statistics;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MethodWaitUpdate {
    public void needUpdate(String a,String b) throws  InterruptedException {
        Thread.sleep(1000);
        String name = "11";
        String methodName = "22";
        String desc = "33";
        Object[] objects = new Object[2];
        objects[0] = "1";
        objects[1] = 2;
        Statistics statistics = new Statistics(name,methodName,desc,1000,100,100,objects);
        String key = StatisticsUtils.getKey(name,methodName,desc);
        StatisticsUtils.insertData(statistics,key);
    }

    /**
     * aes 加密
     *
     * @param sSrc 原文
     * @param sKey key
     * @return 密文
     */
    private String encrypt(String sSrc, String sKey) {
        if (sKey == null) {
            return "";
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return "";
        }
        try {
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
            return "11";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
}

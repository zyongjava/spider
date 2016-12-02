package cn.pomelo.biz.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhengyong on 16/12/2.
 */
public class MD5Util {

    public static String md5(String src) {
        if (StringUtils.isNotBlank(src)) {
            try {
                MessageDigest m = MessageDigest.getInstance("MD5");
                m.reset();
                m.update(src.getBytes(Charset.forName("utf-8")));
                byte[] digest = m.digest();
                BigInteger bigInt = new BigInteger(1, digest);
                String hashText = bigInt.toString(16);
                while (hashText.length() < 32) {
                    hashText = "0" + hashText;
                }
                return hashText;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

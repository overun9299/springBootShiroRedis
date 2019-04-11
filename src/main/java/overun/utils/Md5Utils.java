package overun.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by ZhangPY on 2019/4/6
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 */
public class Md5Utils {


    /**
     * 获取加密后密码
     * @param salt
     * @param password
     */
    public static String getMd5(String salt,String password) {
        // 加密方式
        String hashAlgorithmName = "MD5";

        //加密次数
        int hashIterations = 1024;

        Object simpleHash = new SimpleHash(hashAlgorithmName, password, salt, hashIterations);

        return ((SimpleHash) simpleHash).toHex();

    }

}

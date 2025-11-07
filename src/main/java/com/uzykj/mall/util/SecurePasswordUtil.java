package com.uzykj.mall.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 安全密码加密工具类，融合BCrypt双重加盐与国密SM4二次加密
 */
public class SecurePasswordUtil {
    // SM4加密密钥（16字节）
    private static final byte[] SM4_KEY = "uzykj_mall_sm4_key".getBytes();
    // SM4加密IV（16字节）
    private static final byte[] SM4_IV = "uzykj_mall_sm4_iv".getBytes();

    /**
     * 生成安全密码：BCrypt双重加盐 + SM4二次加密
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String generateSecurePassword(String password) {
        // 第一次BCrypt加密
        String bcryptHash1 = BCrypt.hashpw(password, BCrypt.gensalt(12));
        // 第二次BCrypt加密
        String bcryptHash2 = BCrypt.hashpw(bcryptHash1, BCrypt.gensalt(12));
        // SM4二次加密
        return sm4Encrypt(bcryptHash2);
    }

    /**
     * 验证安全密码
     * @param password 原始密码
     * @param securePassword 加密后的密码
     * @return 是否验证通过
     */
    public static boolean verifySecurePassword(String password, String securePassword) {
        // 先解密SM4
        String bcryptHash2 = sm4Decrypt(securePassword);
        // 提取第二次BCrypt的salt
        String salt2 = bcryptHash2.substring(0, 29);
        // 验证第二次BCrypt
        String bcryptHash1 = BCrypt.hashpw(password, BCrypt.gensalt(12));
        if (!BCrypt.checkpw(bcryptHash1, bcryptHash2)) {
            return false;
        }
        // 提取第一次BCrypt的salt
        String bcryptHash1FromDb = BCrypt.hashpw(password, salt2);
        String salt1 = bcryptHash1FromDb.substring(0, 29);
        // 验证第一次BCrypt
        return BCrypt.checkpw(password, bcryptHash1FromDb);
    }

    /**
     * SM4加密
     * @param data 待加密数据
     * @return 加密后的Base64字符串
     */
    private static String sm4Encrypt(String data) {
        try {
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()));
            KeyParameter keyParam = new KeyParameter(SM4_KEY);
            ParametersWithIV ivParam = new ParametersWithIV(keyParam, SM4_IV);
            cipher.init(true, ivParam);

            byte[] input = data.getBytes("UTF-8");
            byte[] output = new byte[cipher.getOutputSize(input.length)];
            int length = cipher.processBytes(input, 0, input.length, output, 0);
            cipher.doFinal(output, length);

            return Base64.getEncoder().encodeToString(output);
        } catch (Exception e) {
            throw new RuntimeException("SM4 encryption failed", e);
        }
    }

    /**
     * SM4解密
     * @param encryptedData 加密后的Base64字符串
     * @return 解密后的数据
     */
    private static String sm4Decrypt(String encryptedData) {
        try {
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()));
            KeyParameter keyParam = new KeyParameter(SM4_KEY);
            ParametersWithIV ivParam = new ParametersWithIV(keyParam, SM4_IV);
            cipher.init(false, ivParam);

            byte[] input = Base64.getDecoder().decode(encryptedData);
            byte[] output = new byte[cipher.getOutputSize(input.length)];
            int length = cipher.processBytes(input, 0, input.length, output, 0);
            length += cipher.doFinal(output, length);

            return new String(output, 0, length, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("SM4 decryption failed", e);
        }
    }

    /**
     * 生成随机密钥（用于密钥轮换）
     * @return 随机密钥
     */
    public static String generateRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Hex.toHexString(key);
    }
}

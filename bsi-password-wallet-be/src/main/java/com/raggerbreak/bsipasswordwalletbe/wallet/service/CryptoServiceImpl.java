package com.raggerbreak.bsipasswordwalletbe.wallet.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final String ALGO = "AES";

    @Override
    public String encrypt(String password, String walletPassword) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, generateKey(walletPassword));
        byte[] encVal = c.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    @Override
    public String decrypt(String password, String walletPassword) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, generateKey(walletPassword));
        byte[] decodedValue = Base64.getDecoder().decode(password);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private byte[] calculateMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private Key generateKey(String password) {
        return new SecretKeySpec(calculateMD5(password), ALGO);
    }


}

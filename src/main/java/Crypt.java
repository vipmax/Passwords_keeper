/**
 * Created by admin on 10.05.14.
 */

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Crypt {
    private final String KEY;
    Cipher ecipher;
    Cipher dcipher;

    public Crypt() {
        KEY = "paroli14";
        byte[] keyBytes = KEY.getBytes();
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
        try {
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция шифровния
     */

    public String encrypt(String str) {
        byte[] utf8 = new byte[0];
        try {
            utf8 = str.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] enc = new byte[0];
        try {
            enc = ecipher.doFinal(utf8);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new sun.misc.BASE64Encoder().encode(enc);
    }

    /**
     * Функция расшифрования
     */
    public String decrypt(String str) {
        byte[] dec = new byte[0];
        try {
            dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] utf8 = new byte[0];
        try {
            utf8 = dcipher.doFinal(dec);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String s = null;
        try {
            s = new String(utf8, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
package ekol.utils;

import ekol.exceptions.ApplicationException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * Created by kilimci on 25/10/16.
 */
public class AESEncryption {

    public static String encrypt(String salt, String clearText) {
        try {
            Key key = new SecretKeySpec(salt.getBytes(), 0, 16, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(clearText.getBytes()));
        } catch (Exception e) {
          throw new ApplicationException("Error encrypting", e);
        }
    }
    public static String decrypt(String salt, String encrypted) {
        try {
            Key k = new SecretKeySpec(salt.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, k);
            byte[] decodedValue = Base64.getDecoder().decode(encrypted);
            byte[] decValue = c.doFinal(decodedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new ApplicationException("Error decrypting", e);
        }
    }
}

package ekol.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ekol.exceptions.ApplicationException;

/**
 * Created by kilimci on 25/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class AESEncryptionUnitTest {

    @Test
    public void shouldDoEncryptionAndDecryption(){
        String encrypted = AESEncryption.encrypt("1234567890123456", "cleartext");
        String decrypted = AESEncryption.decrypt("1234567890123456", encrypted);
        assertEquals(decrypted, "cleartext");
    }

    @Test(expected = ApplicationException.class)
    public void shouldFailWhenSaltChanges(){
        String encrypted = AESEncryption.encrypt("1234567890123456", "cleartext");
        AESEncryption.decrypt("0123456789012345", encrypted);
    }
}

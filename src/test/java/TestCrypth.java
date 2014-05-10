import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by admin on 10.05.14.
 */

public class TestCrypth {
    Crypt crypt;

    @Before
    public void setUp() throws Exception {
        crypt = new CryptImpl();
    }

    @Test
    public void testEncodeAndDecode() throws Exception {
        String enc = crypt.encrypt("test");
        System.out.println("enc = " + enc);
        assertEquals("test", crypt.decrypt(enc));
    }
}

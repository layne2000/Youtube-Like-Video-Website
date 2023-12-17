import org.example.util.SHA256Util;
import org.junit.Test;

public class SHA256UtilTest {
    @Test
    public void testWholeFunc(){
        String rawStr = "123456789";
        String saltStr = SHA256Util.createSalt();
        String hashedStr = SHA256Util.generateHashedStr(rawStr, saltStr);
        System.out.println(hashedStr);
        assert(SHA256Util.verify(rawStr, hashedStr, saltStr));
    }

    
}

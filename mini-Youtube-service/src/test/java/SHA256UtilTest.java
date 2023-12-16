import org.example.util.SHA256Util;
import org.junit.Test;

public class SHA256UtilTest {
    @Test
    public void testWholeFunc(){
        String rawStr = "this is a test pwd";
        String saltStr = SHA256Util.createSalt();
        String hashedStr = SHA256Util.generateHashedStr(rawStr, saltStr);
        assert(SHA256Util.verify(rawStr, hashedStr, saltStr));
    }
}

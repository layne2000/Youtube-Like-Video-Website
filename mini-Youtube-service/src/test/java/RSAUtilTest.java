import org.example.util.RSAUtil;
import org.junit.Test;

public class RSAUtilTest {
    @Test
    public void decryptPwd() throws Exception {
        String encryptedPwd = "PNCGSXWhfxF0cSgyiD0qpuNbp5pBmcV9V8ghWKA/idBns7LCKSgUfRMde0mDiP8F9VBQbK4nJaROTazYl0tcoadPRbHHH1mHz/UqvYozfdFhZlNz30w6e58EZkZ6c53qw1MOJwfpNBdtyrBrJJUDzBQlP27onPtS88Ag/+C8b1Y=";
        String decryptedPwd = RSAUtil.decrypt(encryptedPwd);
        String realPwd = "123456789";
        assert(decryptedPwd.equals(realPwd));
    }
}

import org.example.util.JsonResponse;
import org.junit.Test;

public class JsonResponseTest {
    @Test
    public void test(){
        System.out.println(JsonResponse.success("we don't need generic type to call static method"));
    }
}

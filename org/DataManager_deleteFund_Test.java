import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_deleteFund_Test {
    @Test
    public void test() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"fundId\":\"123456\"}";

            }
        });
        String result = dm.deleteFund("123456");
        assertEquals(result, "success");
    }

    @Test
    public void testEmptyInput() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"fundId\":\"123456\"}";

            }
        });
        String result = dm.deleteFund("");
        assertEquals(result, "Invalid Fund ID.");
    }
    @Test
    public void NullInput() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"fundId\":\"123456\"}";

            }
        });
        String result = dm.deleteFund(null);
        assertEquals(result, "Invalid Fund ID.");
    }
    @Test
    public void ReturnFailed() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failed\",\"fundId\":\"123456\"}";

            }
        });
        String result = dm.deleteFund("123456");
        assertEquals(result, "error");
    }

    @Test
    public void testExceptionInDeleteFund() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new IllegalStateException("Error occur");
            }
        });

        String result = dm.deleteFund("123");
        assertEquals("Error: exception occurred.", result);
    }
}

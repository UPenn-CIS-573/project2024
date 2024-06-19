import static org.junit.Assert.*;

import java.util.Map;
import org.junit.Test;

public class DataManager_deleteFund_Test {

    // test get contributor name
    @Test
    public void testDeleteFund() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }

        });

        boolean success = dm.deleteFund("123");
        assertTrue(success);
    }

    // test get contributor name fail
    @Test
    public void testDeleteFundFail() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"fail\",\"message\":\"error\"}";
            }

        });

        boolean success = dm.deleteFund("123");
        assertFalse(success);
    }

    // test get contributor name exception
    @Test
    public void testDeleteFundException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("error");
            }

        });

        boolean success = dm.deleteFund("123");
        assertFalse(success);
    }


}

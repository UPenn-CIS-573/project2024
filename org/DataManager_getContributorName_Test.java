import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
public class DataManager_getContributorName_Test {
    @Test
    public void test() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"test\"}";

            }
        });
        String contributorName = dm.getContributorName("12345");
        assertNotNull(contributorName);
        assertEquals("test", contributorName);
    }
    @Test
    public void testUnsuccessfulInGetContributorName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\",\"data\":\"test\"}";

            }
        });
        String contributorName = dm.getContributorName("12345");
        assertNull(contributorName);
    }

    @Test
    public void testExceptionInGetContributorName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new IllegalArgumentException("Some errors occurred.");
            }
        });

        String contributorName = dm.getContributorName("123456");
        assertNull(contributorName);
    }

    @Test
    public void testNullInputParam() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"test\"}";
            }
        });

        String contributorName = dm.getContributorName(null);
        assertNull(contributorName);
    }

    @Test
    public void testEmptyInputParam() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"test\"}";
            }
        });

        String contributorName = dm.getContributorName("");
        assertNull(contributorName);
    }
}

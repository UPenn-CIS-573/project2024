import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_attemptLogin_Test {
    @Test
    public void test() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{" +
                        "\"_id\":\"12345\"," +
                        "\"name\":\"test\"," +
                        "\"description\":\"test description\", " +
                        "\"funds\":" +
                        "[{\"_id\":\"12345\",\"name\":\"test2\",\"description\":\"test2\",\"target\":21203," +
                        "\"donations\":" +
                        "[{\"contributor\":\"232\",\"amount\":68000,\"date\":\"2024-06-09\"}]}]}}";

            }

        });
        Organization o = dm.attemptLogin("123456", "123456");
        Fund f = o.getFunds().get(0);
        Donation d = f.getDonations().get(0);
        assertNotNull(o);
       assertEquals("12345", o.getId());
       assertEquals("test", o.getName());
        assertEquals("test description", o.getDescription());
        assertEquals("12345", f.getId());
       assertEquals("test2", f.getName());
       assertEquals("test2", f.getDescription());
       assertEquals(21203, f.getTarget());
       assertEquals(68000, d.getAmount());
       assertEquals("2024-06-09", d.getDate());
    }

    @Test
    public void testUnsuccessfulAttemptLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });

        Organization o = dm.attemptLogin("123456", "123456");
        assertNull(o);
    }

    @Test
    public void testExceptionInAttemptLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new IllegalArgumentException("Some errors occurred.");
            }
        });

        Organization o = dm.attemptLogin("123456", "123456");
        assertNull(o);
    }

    @Test
    public void testNullInputParam() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{" +
                        "\"_id\":\"12345\"," +
                        "\"name\":\"test\"," +
                        "\"description\":\"This is a description\", " +
                        "\"funds\":" +
                        "[{\"_id\":\"12345\",\"name\":\"test2\",\"description\":\"test2\",\"target\":21203," +
                        "\"donations\":" +
                        "[{\"contributor\":\"232\",\"amount\":68000,\"date\":\"2024-06-09\"}]}]}}";

            }
        });

        Organization o = dm.attemptLogin(null, "123456");
        assertNull(o);
    }

    @Test
    public void testEmptyInputParam() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{" +
                        "\"_id\":\"12345\"," +
                        "\"name\":\"test\"," +
                        "\"description\":\"This is a description\", " +
                        "\"funds\":" +
                        "[{\"_id\":\"12345\",\"name\":\"test2\",\"description\":\"test2\",\"target\":21203," +
                        "\"donations\":" +
                        "[{\"contributor\":\"232\",\"amount\":68000,\"date\":\"2024-06-09\"}]}]}}";

            }
        });

        Organization o = dm.attemptLogin("123456", "");
        assertNull(o);
    }
}

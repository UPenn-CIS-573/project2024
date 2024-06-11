import static org.junit.Assert.*;

import java.util.Map;
import org.junit.Test;

public class DataManager_attemptLogin_Test {

    // test success Login
	@Test
	public void testSuccessfulLogin() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\"," +
						"\"data\":{" +
							"\"_id\":\"123\",\"name\":\"new fund\",\"description\":\"this is the new fund\"," +
							"\"funds\":[{\"_id\":\"12345\",\"name\":\"first fund\",\"description\":\"this is the first fund\",\"target\":10000,\"donations\":[{\"contributor\":\"Mike\", \"amount\":10000, \"date\":\"06-24-2024\"}]}" +
							",{\"_id\":\"232\",\"name\":\"second fund\",\"description\":\"this is the second fund\",\"target\":20000,\"donations\":[]}"+
						"]}}";
			}
		});

		Organization org = dm.attemptLogin("login", "password");
		assertNotNull(org);
		assertEquals("123", org.getId());
		assertEquals("new fund", org.getName());
		assertEquals("this is the new fund", org.getDescription());

		assertEquals(2, org.getFunds().size());
		assertEquals("12345", org.getFunds().get(0).getId());
		assertEquals("first fund", org.getFunds().get(0).getName());
		assertEquals(10000, org.getFunds().get(0).getTarget());

		assertEquals("232", org.getFunds().get(1).getId());
		assertEquals("second fund", org.getFunds().get(1).getName());
		assertEquals(20000, org.getFunds().get(1).getTarget());
	}

    // test login fail
	@Test
	public void testFailedLogin() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"fail\",\"message\":\"error\"}";

			}

		});

		Organization org = dm.attemptLogin("login", "password");
		assertNull(org);
	}

	// test login exception
	@Test
	public void testLoginException() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				throw new RuntimeException("error");
			}

		});

		Organization org = dm.attemptLogin("login", "password");
		assertNull(org);
	}
}
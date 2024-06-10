import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_createFund_Test {
	
	/*
	 * This is a test class for the DataManager.createFund method.
	 * Add more tests here for this method as needed.
	 * 
	 * When writing tests for other methods, be sure to put them into separate
	 * JUnit test classes.
	 */

	@Test
	public void testSuccessfulCreation() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";

			}
			
		});
		
		
		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);
		
		assertNotNull(f);
		assertEquals("this is the new fund", f.getDescription());
		assertEquals("12345", f.getId());
		assertEquals("new fund", f.getName());
		assertEquals(10000, f.getTarget());
		
	}

	// test fail creation
	@Test
	public void testFailedCreation() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"fail\",\"message\":\"error\"}";

			}

		});

		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);
		assertNull(f);
	}

	// test exception creation
	@Test
	public void testExceptionCreation() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				throw new RuntimeException("error");
			}

		});

		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);
		assertNull(f);
	}

	// test success Login
	@Test
	public void testSuccessfulLogin() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\"," +
						"\"data\":{" +
							"\"_id\":\"123\",\"name\":\"new fund\",\"description\":\"this is the new fund\"," +
							"\"funds\":[{\"_id\":\"12345\",\"name\":\"first fund\",\"description\":\"this is the first fund\",\"target\":10000,\"donations\":[{\"contributor\":\"Mike\", \"amount\":10000, \"date\":\"06\\24\\2024\"}]}" +
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

	// test get contributor name
	@Test
	public void testGetContributorName() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":\"Mike\"}";
			}

		});

		String name = dm.getContributorName("123");
		assertEquals("Mike", name);
	}

	// test get contributor name fail
	@Test
	public void testGetContributorNameFail() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"fail\",\"message\":\"error\"}";
			}

		});

		String name = dm.getContributorName("123");
		assertNull(name);
	}

	// test get contributor name exception
	@Test
	public void testGetContributorNameException() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				throw new RuntimeException("error");
			}

		});

		String name = dm.getContributorName("123");
		assertNull(name);
	}

}

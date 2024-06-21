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

	@Test
	public void testUnsuccessfulCreation() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"failure\"}";
			}
		});

		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 200);
		assertNull(f);
	}
	@Test(expected = IllegalStateException.class)
	public void testExceptionInCreation() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				throw new IllegalArgumentException("Some errors occurred.");
			}
		});

		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 12380);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testNullParam() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";
			}
		});

		Fund f = dm.createFund(null, null, "this is the new fund", 12380);
		assertNull(f);
	}
	@Test
	public void testEmptyParam() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";
			}
		});

		Fund f = dm.createFund("12345", "", "", 12380);
		assertNull(f);
	}
	@Test
	public void testIllegalTargetParam() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";
			}
		});

		Fund f = dm.createFund("12345", "new fund", "this is the new fund", -210);
		assertNull(f);
	}

}
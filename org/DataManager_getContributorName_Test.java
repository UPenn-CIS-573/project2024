import static org.junit.Assert.*;

import java.util.Map;
import org.junit.Test;

public class DataManager_getContributorName_Test {

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
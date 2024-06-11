package edu.upenn.cis573.project;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class DataManager_getFundName_Test {

    @Test
    public void testSuccess() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Snoopy\"}";
            }
        });

        String name = dm.getFundName("12345");
        assertNotNull(name);
        assertEquals("Snoopy", name);
    }

    @Test
    public void testGetFundNameUnknownFund() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"fail\",\"data\":\"Snoopy\"}";
            }
        });

        String name = dm.getFundName("12345");
        assertNotNull(name);
        assertEquals("Unknown Fund", name);
    }

    @Test
    public void testGetFundNameException() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Simulated exception");
            }
        });

        // Assert that an exception causes the method to return null
        String name = dm.getFundName("12345");
        assertNull(name);
    }

}

package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DataManager_getAllOrganizations_Test {

    DataManager dmSuccess, dmFailure, dmException;

    @Before
    public void setUp() {
        dmSuccess = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{ \"status\": \"success\", " +
                        "\"data\": [" +
                            "{ \"_id\": \"org1\", " +
                            "\"name\": \"Org One\", " +
                            "\"funds\": [" +
                                "{ \"_id\": \"fund1\", " +
                                "\"name\": \"Fund One\", " +
                                "\"target\": 10000, " +
                                "\"totalDonations\": 5000 }" +
                                "] " +
                            "}," +
                            "{ \"_id\": \"org2\", " +
                            "\"name\": \"Org Two\", " +
                            "\"funds\": [" +
                                "{ \"_id\": \"fund2\", " +
                                "\"name\": \"Fund Two\", " +
                                "\"target\": 20000, " +
                                "\"totalDonations\": 15000 }" +
                                "] " +
                            "}" +
                            "] " +
                        "}";
            }
        });

        dmFailure = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{ \"status\": \"failure\" }";
            }
        });

        dmException = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Simulated exception");
            }
        });

    }

    @Test
    public void testGetAllOrganizationsSuccess() {
        List<Organization> organizations = dmSuccess.getAllOrganizations();
        assertNotNull(organizations);
        assertEquals(2, organizations.size());

        // Test first organization
        Organization org1 = organizations.get(0);
        assertEquals("org1", org1.getId());
        assertEquals("Org One", org1.getName());

        List<Fund> funds1 = org1.getFunds();
        assertNotNull(funds1);
        assertEquals(1, funds1.size());

        Fund fund1 = funds1.get(0);
        assertEquals("fund1", fund1.getId());
        assertEquals("Fund One", fund1.getName());
        assertEquals(10000, fund1.getTarget());
        assertEquals(5000, fund1.getTotalDonations());

        // Test second organization
        Organization org2 = organizations.get(1);
        assertEquals("org2", org2.getId());
        assertEquals("Org Two", org2.getName());

        List<Fund> funds2 = org2.getFunds();
        assertNotNull(funds2);
        assertEquals(1, funds2.size());

        Fund fund2 = funds2.get(0);
        assertEquals("fund2", fund2.getId());
        assertEquals("Fund Two", fund2.getName());
        assertEquals(20000, fund2.getTarget());
        assertEquals(15000, fund2.getTotalDonations());
    }

    @Test
    public void testGetAllOrganizationsFailure() {
        List<Organization> organizations = dmFailure.getAllOrganizations();
        assertNull(organizations);
    }

    @Test
    public void testGetAllOrganizationsException() {
        List<Organization> organizations = dmException.getAllOrganizations();
        assertNull(organizations);
    }

}

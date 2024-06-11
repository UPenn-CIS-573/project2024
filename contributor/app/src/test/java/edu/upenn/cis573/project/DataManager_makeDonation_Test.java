package edu.upenn.cis573.project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class DataManager_makeDonation_Test {
    DataManager dmSuccess, dmFailure, dmException;

    @Before
    public void setUp() {
        dmSuccess = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{ \"status\": \"success\" }";
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
    public void testMakeDonationSuccess() {
        assertTrue(dmSuccess.makeDonation(
                "contributorId",
                "fundId",
                "100")
        );
    }

    @Test
    public void testMakeDonationFailure() {
        assertFalse(dmFailure.makeDonation(
                "contributorId",
                "fundId",
                "100")
        );
    }

    @Test
    public void testMakeDonationException() {
        assertFalse(dmException.makeDonation(
                "contributorId",
                "fundId",
                "100")
        );
    }
}

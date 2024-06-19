package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DataManager_attemptLogin_Test {
    DataManager dmValid, dmFailure, dmException;
    Contributor contributor;
    @Before
    public void setUp() {
        dmValid = new DataManager(new WebClient("1", 3000) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{ \"status\": \"success\", " +
                        "\"data\": { " +
                        "\"_id\": \"000\", " +
                        "\"name\": \"John Smith\", " +
                        "\"email\": \"john@example.com\", " +
                        "\"creditCardNumber\": \"0000000000000000\", " +
                        "\"creditCardCVV\": \"000\", " +
                        "\"creditCardExpiryMonth\": 1, " +
                        "\"creditCardExpiryYear\": 2026, " +
                        "\"creditCardPostCode\": \"12345\", " +
                        "\"donations\": [ { " +
                        "\"fund\": \"fund1\", " +
                        "\"date\": \"2023-06-01\", " +
                        "\"amount\": 100 } ] " +
                        "} " +
                        "}";
            }
        }) {
            @Override
            public String getFundName(String id) {
                if (id.equals("fund1")) {
                    return "f1";
                }
                return null;
            }
        };

        dmFailure = new DataManager(new WebClient("1", 3000) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{ \"status\": \"failure\" }";
            }
        });
        dmException = new DataManager(new WebClient("1", 3000) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException("Simulated exception");
            }
        });
    }

    @Test
    public void testAttemptLoginSuccess() {
        contributor = dmValid.attemptLogin("login", "password");
        assertNotNull(contributor);
        assertEquals("000", contributor.getId());
        assertEquals("John Smith", contributor.getName());
        assertEquals("john@example.com", contributor.getEmail());
        assertEquals("0000000000000000", contributor.getCreditCardNumber());
        assertEquals("000", contributor.getCreditCardCVV());
        assertEquals("1", contributor.getCreditCardExpiryMonth());
        assertEquals("2026", contributor.getCreditCardExpiryYear());
        assertEquals("12345", contributor.getCreditCardPostCode());

        List<Donation> donations = contributor.getDonations();
        assertNotNull(donations);
        assertEquals(1, donations.size());
        Donation donation = donations.get(0);
        assertEquals("f1", donation.getFundName());
        assertEquals("John Smith", donation.getContributorName());
        assertEquals(100, donation.getAmount());
        assertEquals("2023-06-01", donation.getDate());
    }

    @Test
    public void testAttemptLoginFailure() {
        contributor = dmFailure.attemptLogin("login", "password");
        assertNull(contributor);
    }

    @Test
    public void testAttemptLoginException() {
        contributor = dmException.attemptLogin("login", "password");
        assertNull(contributor);
    }
}

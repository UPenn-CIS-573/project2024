package edu.upenn.cis573.project;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;

public class DataManager {

    private WebClient client;

    public DataManager(WebClient client) {

        this.client = client;
    }


    /**
     * Attempt to log in to the Contributor account using the specified login and password.
     * This method uses the /findContributorByLoginAndPassword endpoint in the API
     * @return the Contributor object if successfully logged in, null otherwise
     */
    public Contributor attemptLogin(String login, String password) {
        if (client == null) {
            throw new IllegalStateException("WebClient is null");
        }
        if (login == null || password == null) {
            throw new IllegalArgumentException("Login or password is null");
        }

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            map.put("password", password);
            String response = client.makeRequest("/findContributorByLoginAndPassword", map);

            if (response == null) {
                throw new IllegalStateException("WebClient returned null");
            }

            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            // create cache
            final Map<String, String> fundNameCache = new HashMap<>();

            if(status.equals("error")) {
                throw new IllegalStateException("Error in response: " + json.getString("error"));
            }

            if (status.equals("success")) {
                JSONObject data = (JSONObject)json.get("data");
                String id = (String)data.get("_id");
                String name = (String)data.get("name");
                String email = (String)data.get("email");
                String creditCardNumber = (String)data.get("creditCardNumber");
                String creditCardCVV = (String)data.get("creditCardCVV");
                String creditCardExpiryMonth = ((Integer)data.get("creditCardExpiryMonth")).toString();
                String creditCardExpiryYear = ((Integer)data.get("creditCardExpiryYear")).toString();
                String creditCardPostCode = (String)data.get("creditCardPostCode");

                Contributor contributor = new Contributor(id, name, email, creditCardNumber, creditCardCVV, creditCardExpiryYear, creditCardExpiryMonth, creditCardPostCode);

                List<Donation> donationList = new LinkedList<>();

                JSONArray donations = (JSONArray)data.get("donations");

                for (int i = 0; i < donations.length(); i++) {

                    JSONObject jsonDonation = donations.getJSONObject(i);
                    String fundId = (String) jsonDonation.get("fund");

                    // query cache
                    String fund = fundNameCache.get(fundId);
                    // cache miss
                    if (fund == null) {
                        fund = getFundName(fundId);
                        // update cache
                        if (fund != null) {
                            fundNameCache.put(fundId, fund);
                        }
                    }

                    String date = (String)jsonDonation.get("date");
                    long amount = (Integer)jsonDonation.get("amount");

                    Donation donation = new Donation(fund, name, amount, date);
                    donationList.add(donation);

                }

                contributor.setDonations(donationList);

                return contributor;
            }
            return null;
        }
        catch (Exception e) {
           throw new IllegalStateException("Exception during login", e);
        }
    }

    /**
     * Get the name of the fund with the specified ID using the /findFundNameById endpoint
     * @return the name of the fund if found, "Unknown fund" if not found, null if an error occurs
     */
    public String getFundName(String id) {
        if (client == null) {
            throw new IllegalStateException("WebClient is null");
        }
        if (id == null) {
            throw new IllegalArgumentException("Fund ID is null");
        }

        try {

            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            String response = client.makeRequest("/findFundNameById", map);

            if (response == null) {
                throw new IllegalStateException("WebClient returned null");
            }

            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");
            if(status.equals("error")) {
                throw new IllegalStateException("Error in response: " + json.getString("error"));
            }
            if (status.equals("success")) {
                String name = (String)json.get("data");
                return name;
            }
            else return "Unknown Fund";
        } catch (Exception e) {
            throw new IllegalStateException("Exception during getFundName", e);
        }
    }

    /**
     * Get information about all of the organizations and their funds.
     * This method uses the /allOrgs endpoint in the API
     * @return a List of Organization objects if successful, null otherwise
     */
    public List<Organization> getAllOrganizations() {
        if (client == null) {
            throw new IllegalStateException("WebClient is null");
        }

        try {
            Map<String, Object> map = new HashMap<>();
            String response = client.makeRequest("/allOrgs", map);

            if (response == null) {
                throw new IllegalStateException("WebClient returned null");
            }

            JSONObject json = new JSONObject(response);
            String status = (String)json.getString("status");

            if (status.equals("success")) {

                List<Organization> organizations = new LinkedList<>();

                JSONArray data = (JSONArray)json.get("data");

                for (int i = 0; i < data.length(); i++) {

                    JSONObject obj = data.getJSONObject(i);

                    String id = obj.getString("_id");
                    String name = obj.getString("name");

                    Organization org = new Organization(id, name);

                    List<Fund> fundList = new LinkedList<>();

                    JSONArray array = obj.getJSONArray("funds");

                    for (int j = 0; j < array.length(); j++) {

                        JSONObject fundObj = array.getJSONObject(j);

                        id = fundObj.getString("_id");
                        name = fundObj.getString("name");
                        long target = fundObj.getInt("target");
                        long totalDonations = fundObj.getInt("totalDonations");

                        Fund fund = new Fund(id, name, target, totalDonations);

                        fundList.add(fund);

                    }

                    org.setFunds(fundList);

                    organizations.add(org);

                }

                return organizations;

            } else {
                throw new IllegalStateException("Error in response: " + json.getString("error"));
            }

        }
        catch (Exception e) {
            throw new IllegalStateException("Exception during getAllOrganizations", e);
        }
    }

    /**
     * Make a donation to the specified fund for the specified amount.
     * This method uses the /makeDonation endpoint in the API
     * @return true if successful, false otherwise
     */
    public boolean makeDonation(String contributorId, String fundId, String amount) {
        if (client == null) {
            throw new IllegalStateException("WebClient is null");
        }
        if (contributorId == null || fundId == null || amount == null) {
            throw new IllegalArgumentException("Contributor ID, Fund ID, or amount is null");
        }

        try {
            Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount is non-numeric", e);
        }

        try {

            Map<String, Object> map = new HashMap<>();
            map.put("contributor", contributorId);
            map.put("fund", fundId);
            map.put("amount", amount);
            String response = client.makeRequest("/makeDonation", map);

            if (response == null) {
                throw new IllegalStateException("WebClient returned null");
            }
            
            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (!status.equals("success")) {
                throw new IllegalStateException("Error in reponse: " + json.getString("error"));
            }
            return true;

        }
        catch (Exception e) {
            throw new IllegalStateException("Exception during makeDonation");
        }
    }
}

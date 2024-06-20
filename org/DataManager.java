
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataManager {

	private final WebClient client;

	public DataManager(WebClient client) {
		this.client = client;
	}

	/**
	 * Attempt to log the user into an Organization account using the login and password.
	 * This method uses the /findOrgByLoginAndPassword endpoint in the API
	 * @return an Organization object if successful; null if unsuccessful
	 */
	public Organization attemptLogin(String login, String password) {
		if (login == null || password == null) {
			return null;
		}
		if (login.isEmpty() || password.isEmpty()) {
			return null;
		}

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("login", login);
			map.put("password", password);
			String response = client.makeRequest("/findOrgByLoginAndPassword", map);

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			// create cache
			final Map<String, String> contributorNameCache = new HashMap<>();

			if (status.equals("success")) {
				JSONObject data = (JSONObject)json.get("data");
				String fundId = (String)data.get("_id");
				String name = (String)data.get("name");
				String description = (String)data.get("description");
				Organization org = new Organization(fundId, name, description);

				JSONArray funds = (JSONArray)data.get("funds");
				Iterator it = funds.iterator();
				while(it.hasNext()){
					JSONObject fund = (JSONObject) it.next(); 
					fundId = (String)fund.get("_id");
					name = (String)fund.get("name");
					description = (String)fund.get("description");
					long target = (Long)fund.get("target");

					Fund newFund = new Fund(fundId, name, description, target);

					JSONArray donations = (JSONArray)fund.get("donations");
					List<Donation> donationList = new LinkedList<>();
					Iterator it2 = donations.iterator();
					while(it2.hasNext()){
						JSONObject donation = (JSONObject) it2.next();
						String contributorId = (String)donation.get("contributor");

						String contributorName = null;
						if (contributorId != null) {
							// query cache
							contributorName = contributorNameCache.get(contributorId);
							// cache miss
							if (contributorName == null) {
								contributorName = this.getContributorName(contributorId);
								// update cache
								if (contributorName != null) {
									contributorNameCache.put(contributorId, contributorName);
								}
							}
						}

						long amount = (Long)donation.get("amount");
						String date = parseDateFormat((String)donation.get("date"));
						donationList.add(new Donation(fundId, contributorName, amount, date));
					}

					newFund.setDonations(donationList);

					org.addFund(newFund);

				}

				return org;
			}
			else return null;
		}
		catch (Exception e) {
			throw new IllegalStateException("Error in communicating with server", e);
		}
	}

	/**
	 * Look up the name of the contributor with the specified ID.
	 * This method uses the /findContributorNameById endpoint in the API.
	 * @return the name of the contributor on success; null if no contributor is found
	 */
	public String getContributorName(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}

		try {

			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			String response = client.makeRequest("/findContributorNameById", map);

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("success")) {
				String name = (String)json.get("data");
				return name;
			}
			else return null;


		}
		catch (Exception e) {
			return null;
		}	
	}

	/**
	 * This method creates a new fund in the database using the /createFund endpoint in the API
	 * @return a new Fund object if successful; null if unsuccessful
	 */
	public Fund createFund(String orgId, String name, String description, long target) {
		if (orgId == null || name == null || description == null) {
			return null;
		}
		if (orgId.isEmpty() || name.isEmpty() || description.isEmpty()) {
			return null;
		}
		if (target < 0) {
			return null;
		}
		try {

			Map<String, Object> map = new HashMap<>();
			map.put("orgId", orgId);
			map.put("name", name);
			map.put("description", description);
			map.put("target", target);
			String response = client.makeRequest("/createFund", map);

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("success")) {
				JSONObject fund = (JSONObject)json.get("data");
				String fundId = (String)fund.get("_id");
				return new Fund(fundId, name, description, target);
			}
			else return null;

		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String deleteFund(String fundId) {
		if (fundId == null || fundId.isEmpty()) {
			return "Invalid Fund ID.";
		}
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("id", fundId);
			String response = client.makeRequest("/deleteFund", map);
			if (response == null || response.isEmpty()) {
				return "Error: Empty response from server.";
			}

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String) json.get("status");

			return status.equals("success") ? "success" : "error";
		} catch (Exception e) {
            e.printStackTrace();
			return "Error: exception occurred.";
        }
    }

	public String parseDateFormat(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
		return sdf.format(d);
	}

}

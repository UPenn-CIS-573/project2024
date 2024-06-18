
import java.io.Console;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class DataManager {

	private final WebClient client;

	private Map<String, String> contributorCache = new HashMap<>();
	private PublicKey publicKey = null;

	public DataManager(WebClient client) {
		this.client = client;
	}

	public static String encrypt(String data, PublicKey publicKey) throws Exception {
		// RSA/ECB/OAEPWithSHA-256AndMGF1Padding RSA/ECB/PKCS1Padding
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static PublicKey loadPublicKey(String filename) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		StringBuilder publicKeyPem = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			publicKeyPem.append(line).append("\n");
		}
		br.close();

		String publicKeyContent = publicKeyPem.toString()
				.replaceAll("\\n", "")
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "");
		byte[] publicBytes = Base64.getDecoder().decode(publicKeyContent);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * Attempt to log the user into an Organization account using the login and password.
	 * This method uses the /findOrgByLoginAndPassword endpoint in the API
	 * @return an Organization object if successful; null if unsuccessful
	 */
	public Organization attemptLogin(String login, String password, String publicKeyFile) {

		try {
			Map<String, Object> map = new HashMap<>();

			// encrypt password
			publicKey = loadPublicKey(publicKeyFile);
			String encryptedData = encrypt(password, publicKey);

			map.put("login", login);
			map.put("password", encryptedData);
			String response = client.makeRequest("/findOrgByLoginAndPassword", map);

			// connection fails
			if (response == null) {
				throw new IllegalStateException("Response is null");
			}

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");


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
						String contributorName = this.getContributorName(contributorId);
						long amount = (Long)donation.get("amount");
						String date = (String)donation.get("date");

						String day_date = date.split("T")[0];
						String[] date_parts = day_date.split("-");
						// check seperator
						if (date_parts.length != 3) {
							continue;
						}

						String month = MonthLiteral(date_parts[1]);
						date = month + " " + date_parts[2] + ", " + date_parts[0];
						donationList.add(new Donation(fundId, contributorName, amount, date));
					}

					newFund.setDonations(donationList);
					org.addFund(newFund);
				}

				return org;
			}
			else return null;
		}
		catch (IllegalStateException e) {
			// rethrow the exception
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Look up the name of the contributor with the specified ID.
	 * This method uses the /findContributorNameById endpoint in the API.
	 * @return the name of the contributor on success; null if no contributor is found
	 */
	public String getContributorName(String id) {
		// ID is null
		if (id == null) {
			throw new IllegalArgumentException("[Invalid Input] Contributor ID cannot be empty.");
		}

		try {
			if (contributorCache.containsKey(id)) {
				return contributorCache.get(id);
			}

			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			String response = client.makeRequest("/findContributorNameById", map);

			if (response == null) {
				throw new IllegalStateException("[Error in communicating with server] fail to find contributor.");
			}

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("success")) {
				String name = (String)json.get("data");
				contributorCache.put(id, name);
				return name;
			}
			else return null;

		}
		catch (Exception e) {
			return null;
		}	
	}

	public static String MonthLiteral(String month_int){
		// convert month from number to literal
		String month = "";
		switch (month_int) {
			case "01":
				month = "January";
				break;
			case "02":
				month = "February";
				break;
			case "03":
				month = "March";
				break;
			case "04":
				month = "April";
				break;
			case "05":
				month = "May";
				break;
			case "06":
				month = "June";
				break;
			case "07":
				month = "July";
				break;
			case "08":
				month = "August";
				break;
			case "09":
				month = "September";
				break;
			case "10":
				month = "October";
				break;
			case "11":
				month = "November";
				break;
			case "12":
				month = "December";
				break;
		}

		return month;
	}

	/**
	 * This method creates a new fund in the database using the /createFund endpoint in the API
	 * @return a new Fund object if successful; null if unsuccessful
	 */
	public Fund createFund(String orgId, String name, String description, long target) {

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


}

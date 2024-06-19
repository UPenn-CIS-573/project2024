
import java.io.Console;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		if (login == null || login.isEmpty()) {
			throw new IllegalArgumentException("[Invalid Input] Login cannot be empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("[Invalid Input] Password cannot be empty.");
		}
		try {
			Map<String, Object> map = new HashMap<>();

			if (login.matches("\\w+")){
				map.put("login", login);
			}else {
				throw new IllegalArgumentException("[Invalid login ID] Login must be alphanumeric.");
			}

			//check illegal password value
			if (login.matches("\\w+")){
				map.put("password", password);}
			else {
				throw new IllegalArgumentException("[Invalid password] Password must be alphanumeric.");
			}

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
					Map<String, AggregateDonation> aggregateDonations = new HashMap<>();
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
						if (aggregateDonations.containsKey(contributorId)) {
							aggregateDonations.get(contributorId).increaseDonation(amount);
						} else {
							aggregateDonations.put(contributorId, new AggregateDonation(contributorId, contributorName, amount));
						}
					}

					newFund.setDonations(donationList);
					newFund.setAggregateDonations(aggregateDonations);
					org.addFund(newFund);
				}

				return org;
			}
			else {
				throw new IllegalStateException("[Error in communicating with server] fail to login.");
			}
		}
		catch (ParseException e) {
			throw new IllegalStateException("[Error in communicating with server] fail to login.");
		}
		catch (IllegalStateException e) {
			// rethrow the exception
			throw e;
		}
		catch (IllegalArgumentException e) {
			throw e;
		}
		catch (NullPointerException e) {
			throw new IllegalStateException("[Error in communicating with server] fail to login.");
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
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("[Invalid Input] Contributor ID cannot be empty.");
		}
		if (client == null) {
			throw new IllegalStateException("[Invalid State] WebClient cannot be null.");
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
			JSONObject json;
			try {
				json = (JSONObject) parser.parse(response);
			} catch (Exception e) {
				throw new IllegalStateException("[Error in communicating with server] fail to find contributor.");
			}
			String status = (String)json.get("status");

			if (status.equals("success")) {
				String name = (String)json.get("data");
				contributorCache.put(id, name);
				return name;
			}
			else{
				throw new IllegalStateException("[Error in communicating with server] fail to find contributor.");
			}

 		} catch (NullPointerException e) {
			throw new IllegalStateException("[Error in communicating with server] fail to find contributor.");
		}
		catch (IllegalStateException e) {
			// rethrow the exception
			throw e;
		}
		catch (IllegalArgumentException e) {
			throw e;
		}
		catch (Exception e) {
			System.out.println("Error in getContributorName: " + e.getMessage());
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
		if (client == null) {
			throw new IllegalStateException("[Invalid State] WebClient cannot be null.");
		}
		if (orgId == null || orgId.isEmpty()) {
			throw new IllegalArgumentException("[Invalid Input] Organization ID cannot be null or empty.");
		}
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("[Invalid Input] Fund name cannot be null or empty.");
		}
		if (description == null) {
			throw new IllegalArgumentException("[Invalid Input] Description cannot be null.");
		}
		if (target < 0) {
			throw new IllegalArgumentException("[Invalid Input] Target cannot be negative.");
		}
		try {

			Map<String, Object> map = new HashMap<>();
			map.put("orgId", orgId);
			map.put("name", name);
			map.put("description", description);
			map.put("target", target);
			String response = client.makeRequest("/createFund", map);

			if (response == null) {
				throw new IllegalStateException("[Error in communicating with server] fail to create fund.");
			}
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			try {
				json = (JSONObject) parser.parse(response);
			} catch (Exception e) {
				throw new IllegalStateException("[Error in communicating with server] fail to create fund.");
			}
			String status = (String)json.get("status");
			if ("error".equals(status)) {
				throw new IllegalStateException("[Error in communicating with server] fail to create fund.");
			}

			if (status.equals("success")) {
				JSONObject fund = (JSONObject)json.get("data");
				String fundId = (String)fund.get("_id");
				return new Fund(fundId, name, description, target);
			}
			else{
				throw new IllegalStateException("[Error in communicating with server] fail to create fund.");
			}

		}
		catch (NullPointerException e) {
			throw new IllegalStateException("[Error in communicating with server] fail to create fund.");
		}
		catch (IllegalStateException e) {
			// rethrow the exception
			throw e;
		}
		catch (IllegalArgumentException e) {
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}


}

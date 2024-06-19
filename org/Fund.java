import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Fund {
	public class CountDonations {
		public int count;
		public long sum;
		public CountDonations() {
			this.count = 0;
			this.sum = 0;
		}
		public void addDonation(long amount) {
			this.count += 1;
			this.sum += amount;
		}
	}
	private String id;
	private String name;
	private String description;
	private long target;
	private List<Donation> donations;
	private Map<String, CountDonations> aggregates;
	public Fund(String id, String name, String description, long target) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.target = target;
		donations = new LinkedList<>();
		aggregates = new HashMap<>();
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getTarget() {
		return target;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
		for (Donation d : donations) {
			CountDonations cd = aggregates.getOrDefault(d.getContributorName(), new CountDonations());
			cd.addDonation(d.getAmount());
			aggregates.put(d.getContributorName(), cd);
		}
	}
	
	public List<Donation> getDonations() {
		return donations;
	}
	
	public Map<String, CountDonations> getDonationAggregates() {
		return aggregates;
	}
	
	
}

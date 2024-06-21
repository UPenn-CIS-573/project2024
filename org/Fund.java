import java.util.*;

public class Fund {

	private String id;
	private String name;
	private String description;
	private long target;
	private List<Donation> donations;
	private List<Map.Entry<String, AggregateInfo>> AggregationList;
	
	public Fund(String id, String name, String description, long target) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.target = target;
		donations = new LinkedList<>();
		AggregationList = null;
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
		AggregationList = null;
	}
	
	public List<Donation> getDonations() {
		return donations;
	}

	private void aggregation() {
		Map<String, AggregateInfo> AggregationResult = new HashMap<>();
		for (Donation donation : donations) {
			String Contributor = donation.getContributorName();
			AggregateInfo AI = AggregationResult.getOrDefault(Contributor, new AggregateInfo());
			AI.addFund(donation.getAmount());
			AggregationResult.put(Contributor, AI);
		}
		List<Map.Entry<String, AggregateInfo>> AggregationResultList = new ArrayList<>(AggregationResult.entrySet());
		AggregationResultList.sort((entry1, entry2) -> Long.compare(entry2.getValue().totalAmount, entry1.getValue().totalAmount));
		AggregationList = AggregationResultList;
	}
	public List<Map.Entry<String, AggregateInfo>> getAggregatedDonations() {
		if (AggregationList == null) {
			aggregation();
		}
		return AggregationList;
	}
	
	static class AggregateInfo {
		long totalAmount = 0;
		int donationCount = 0;
		void addFund (long amount) {
			totalAmount += amount;
			donationCount ++;
		}
	}
}

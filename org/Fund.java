import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Fund {

	private String id;
	private String name;
	private String description;
	private long target;
	private List<Donation> donations;
	private Map<String, AggregateDonation> aggregateDonations;
	
	public Fund(String id, String name, String description, long target) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.target = target;
		donations = new LinkedList<>();
		aggregateDonations = new HashMap<>();
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
	}
	
	public List<Donation> getDonations() {
		return donations;
	}
	
	public void setAggregateDonations(Map<String, AggregateDonation> aggregateDonations) {
		this.aggregateDonations = aggregateDonations;
	}

	public List<AggregateDonation> getSortedAggregateDonations() {
		List<AggregateDonation> sorted = new LinkedList<>(aggregateDonations.values());
		sorted.sort((a, b) -> {
			if (a.getTotal() > b.getTotal()) {
				return -1;
			} else if (a.getTotal() < b.getTotal()) {
				return 1;
			} else {
				return 0;
			}
		});
		return sorted;
	}
	
}

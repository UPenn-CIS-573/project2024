import java.util.LinkedList;
import java.util.List;

public class AggregateDonation {
    private String contributorId;
    private String contributorName;
    private int times;
    private long total;

    public AggregateDonation(String contributorId, String contributorName) {
        this.contributorId = contributorId;
        this.contributorName = contributorName;
        this.times = 0;
        this.total = 0;
    }

    public AggregateDonation(String contributorId, String contributorName, long total) {
        this.times = 1;
        this.total = total;
        this.contributorName = contributorName;
        this.contributorId = contributorId;
    }

    public void increaseDonation(long amount) {
        times++;
        total += amount;
    }

    public long getTotal() {
        return total;
    }

    public String getContributorName() {
        return contributorName;
    }

}
package edu.upenn.cis573.project;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ViewDonationActivityByFund extends AppCompatActivity {
    private static final Map<String, String[]> cache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donations_byfund);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView messageField = findViewById(R.id.donationsListHeaderFieldByFund);

        Contributor contributor = MainActivity.contributor;
        String name = contributor.getId();

        Log.v("contributor", "number of donations when listing " + contributor.getDonations().size());

        messageField.setText("Here are " + contributor.getName() + "'s donations aggregated by fund:");

        String[] donationInfo;
        if (cache.containsKey(name)) {
            Log.v("cache", "using cache");
            donationInfo = cache.get(name);
        } else {
            Log.v("cache", "not using cache");
            Map<String, Double> donationsByFund = new HashMap<>();
            Map<String, Integer> donationsCountByFund = new HashMap<>();

            for (Donation d : contributor.getDonations()) {
                String fundName = d.getFundName();
                double amount = d.getAmount();
                donationsByFund.put(fundName, donationsByFund.getOrDefault(fundName, 0.0) + amount);
                donationsCountByFund.put(fundName, donationsCountByFund.getOrDefault(fundName, 0) + 1);
            }

            List<Map.Entry<String, Double>> entryList = new ArrayList<>(donationsByFund.entrySet());
            entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            Map<String, Double> sortedDonationsByFund = new LinkedHashMap<>();
            for (Map.Entry<String, Double> entry : entryList) {
                sortedDonationsByFund.put(entry.getKey(), entry.getValue());
            }

            donationInfo = new String[sortedDonationsByFund.size()];
            int index = 0;
            for (String fundName : sortedDonationsByFund.keySet()) {
                donationInfo[index] = fundName + ", " + donationsCountByFund.get(fundName) + " donations, $" + sortedDonationsByFund.get(fundName) + " total";
                index++;
            }

            cache.put(name, donationInfo);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.listview, donationInfo);
        ListView listView = findViewById(R.id.donationsListByFund);
        listView.setAdapter(adapter);
    }

}
package com.example.shelterconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.model.Donation;

import java.util.ArrayList;

public class DonationAdapter extends ArrayAdapter<Donation> {

    private ArrayList<Donation> donations;
    private Context adapterContext;

    public DonationAdapter(Context context, ArrayList<Donation> donations) {
        super(context, R.layout.activity_my_donations, donations);
        adapterContext = context;
        this.donations = donations;
    }

    @Override
    public View getView(int indexPosition, View convertView, ViewGroup parent) {
        View currentView = convertView;

        try {
            Donation currDonation = this.donations.get(indexPosition);

            if (currentView == null) {
                LayoutInflater vi = (LayoutInflater) this.adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                currentView = vi.inflate(R.layout.donation_list_item, null);
            }

            TextView amountDonated = (TextView) currentView.findViewById(R.id.amountDonated);
            String donationAmount = "$"+Double.toString(currDonation.getAmountDonated());
            amountDonated.setText(donationAmount);

            TextView donationDate = (TextView) currentView.findViewById(R.id.donationDate);
            donationDate.setText(currDonation.getDonationDate());

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return currentView;
    }
}

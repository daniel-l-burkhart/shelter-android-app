package com.example.shelterconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.MakeDonationActivity;
import com.example.shelterconnect.model.Request;

import java.util.ArrayList;

/**
 * Created by parth on 20-03-2018.
 */

public class RequestAdapterDonor extends ArrayAdapter<Request> {

    public static final String REQUEST_ID_EXTRA = "requestID";
    public static final String ITEM_NAME_EXTRA = "itemName";
    public static final String REQUEST_NEEDED_EXTRA = "requestRaised";
    public static final String REQUEST_RAISED_EXTRA = "requestNeeded";
    public static final String REQUEST_QUANTITY_EXTRA = "quantity";
    public static final String ITEM_ID_EXTRA = "itemId";
    private ArrayList<Request> requests;
    private Context adapterContext;

    public RequestAdapterDonor(Context context, ArrayList<Request> requests) {
        super(context, R.layout.activity_open_requests, requests);
        adapterContext = context;
        this.requests = requests;
    }

    @Override
    public View getView(final int indexPosition, View convertView, ViewGroup parent) {
        View currentView = convertView;

        try {
            final Request currRequest = this.requests.get(indexPosition);

            if (currentView == null) {
                LayoutInflater vi = (LayoutInflater) this.adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                currentView = vi.inflate(R.layout.request_list_donor, null);
            }

            final TextView itemName = currentView.findViewById(R.id.itemName);
            String name = currRequest.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            itemName.setText(name);

            final int requestIDLabel = (currRequest.getRequestID());

            final TextView requestNeeded = currentView.findViewById(R.id.required);
            requestNeeded.setText(Double.toString(currRequest.getAmountNeeded()));

            final TextView requestRaised = currentView.findViewById(R.id.achieved);
            requestRaised.setText(Double.toString(currRequest.getAmountRaised()));

            final TextView inStockQuantity = currentView.findViewById(R.id.inStock);
            inStockQuantity.setText(Integer.toString(currRequest.getQuantity()));


            final double raisedLabel = (currRequest.getAmountRaised());
            final double requiredLabel = (currRequest.getAmountNeeded());
            final String itemNameLabel = (currRequest.getName());
            final int itemIDLabel = (currRequest.getItemID());
            final int quantityLabel = (currRequest.getQuantity());

            TextView requestActive = currentView.findViewById(R.id.act);
            requestActive.setText(Boolean.toString(currRequest.isActive()));
            Button makeDonationBtn = currentView.findViewById(R.id.donateNowButton);

            makeDonationBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), MakeDonationActivity.class);
                    i.putExtra(REQUEST_ID_EXTRA, String.valueOf(requestIDLabel));
                    i.putExtra("requestObject", currRequest);
                    adapterContext.startActivity(i);
                    Log.d("requestID value", String.valueOf(requestIDLabel));
                }
            });

            Boolean x = (currRequest.isActive());

            if (x) {
                itemName.setTextColor(Color.parseColor("RED"));
                requestNeeded.setTextColor(Color.parseColor("RED"));
                requestRaised.setTextColor(Color.parseColor("RED"));
                inStockQuantity.setTextColor(Color.parseColor("RED"));
            } else {
                itemName.setVisibility(View.GONE);
                requestNeeded.setVisibility(View.GONE);
                requestRaised.setVisibility(View.GONE);
                inStockQuantity.setVisibility(View.GONE);
                makeDonationBtn.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return currentView;
    }
}
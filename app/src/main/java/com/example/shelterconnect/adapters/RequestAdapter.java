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
import com.example.shelterconnect.controller.ReceiveItemActivity;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.requests.UpdateRequestActivity;
import com.example.shelterconnect.model.Request;
import com.example.shelterconnect.adapters.ItemAdapter;
import java.util.ArrayList;

/**
 * Created by parth on 20-03-2018.
 */

public class RequestAdapter extends ArrayAdapter<Request> {

    public static final String REQUEST_ID_EXTRA = "requestID";
    public static final String ITEM_NAME_EXTRA = "itemName";
    public static final String REQUEST_NEEDED_EXTRA = "requestRaised";
    public static final String REQUEST_RAISED_EXTRA = "requestNeeded";
    public static final String REQUEST_QUANTITY_EXTRA = "quantity";
    public static final String ITEM_ID_EXTRA = "itemId";
    private ArrayList<Request> requests;
    private Context adapterContext;

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, R.layout.activity_open_requests, requests);
        adapterContext = context;
        this.requests = requests;
    }

    @Override
    public View getView(final int indexPosition, View convertView, ViewGroup parent) {
        View currentView = convertView;

        try {
            Request currRequest = this.requests.get(indexPosition);

            if (currentView == null) {
                LayoutInflater vi = (LayoutInflater) this.adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                currentView = vi.inflate(R.layout.request_list, null);
            }




            final TextView itemName = currentView.findViewById(R.id.requestID);
            itemName.setText(currRequest.getName());

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
          //  TextView requestWorkerID = (TextView) currentView.findViewById(R.id.empID);
          //  requestWorkerID.setText(Integer.toString(currRequest.getEmployeeID()));

            TextView requestActive = currentView.findViewById(R.id.act);
            requestActive.setText(Boolean.toString(currRequest.isActive()));

            Button receiveBtn = currentView.findViewById(R.id.receiveButton);
            Button updateBtn = currentView.findViewById(R.id.updateButton);

            receiveBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                   Intent i = new Intent(getContext(), ReceiveItemActivity.class);

                    i.putExtra(REQUEST_ID_EXTRA, String.valueOf(requestIDLabel));
                    adapterContext.startActivity(i);
                    Log.d("requestID value", String.valueOf(requestIDLabel));
                }
            });

            updateBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), UpdateRequestActivity.class);

                    i.putExtra(REQUEST_ID_EXTRA, String.valueOf(requestIDLabel));
                    i.putExtra(ITEM_NAME_EXTRA, String.valueOf(itemNameLabel));
                    i.putExtra(REQUEST_NEEDED_EXTRA, String.valueOf(requiredLabel));
                    i.putExtra(REQUEST_RAISED_EXTRA, String.valueOf(raisedLabel));
                    i.putExtra(REQUEST_QUANTITY_EXTRA, String.valueOf(quantityLabel));
                    i.putExtra(ITEM_ID_EXTRA, String.valueOf(itemIDLabel));

                    adapterContext.startActivity(i);

                    Log.d("requestID value", String.valueOf(requestIDLabel));
                    Log.d("name value", String.valueOf(itemNameLabel));
                    Log.d("Needed value", String.valueOf(requiredLabel));
                    Log.d("raised value", String.valueOf(raisedLabel));
                }
            });


            Boolean x = (currRequest.isActive());

            if (x == true)
            {itemName.setTextColor(Color.parseColor("RED"));
                requestNeeded.setTextColor(Color.parseColor("RED"));
                requestRaised.setTextColor(Color.parseColor("RED"));
                inStockQuantity.setTextColor(Color.parseColor("RED"));
            }
            else {
                itemName.setTextColor(Color.parseColor("BLUE"));
                requestNeeded.setTextColor(Color.parseColor("BLUE"));
                requestRaised.setTextColor(Color.parseColor("BLUE"));
                inStockQuantity.setTextColor(Color.parseColor("BLUE"));

            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return currentView;
    }
}
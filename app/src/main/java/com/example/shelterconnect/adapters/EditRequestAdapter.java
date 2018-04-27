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
import com.example.shelterconnect.model.Request;
import com.example.shelterconnect.adapters.ItemAdapter;
import java.util.ArrayList;

/**
 * Created by parth on 20-03-2018.
 */

public class EditRequestAdapter extends ArrayAdapter<Request> {

    public static final String REQUEST_ID_EXTRA = "requestID";
    private ArrayList<Request> requests;
    private Context adapterContext;

    public EditRequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, R.layout.activity_update_request, requests);
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

                currentView = vi.inflate(R.layout.request_list_edit, null);
            }




            final TextView itemName = currentView.findViewById(R.id.requestID);
            itemName.setText(currRequest.getName());

            final int requestIDLabel = (currRequest.getRequestID());

            TextView requestNeeded = currentView.findViewById(R.id.required);
            requestNeeded.setText(Double.toString(currRequest.getAmountNeeded()));

            TextView requestRaised = currentView.findViewById(R.id.achieved);
            requestRaised.setText(Double.toString(currRequest.getAmountRaised()));

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


            Boolean x = (currRequest.isActive());

            if (x == true)
            {itemName.setTextColor(Color.parseColor("RED"));
                requestNeeded.setTextColor(Color.parseColor("RED"));
                requestRaised.setTextColor(Color.parseColor("RED"));
                //      requestWorkerID.setTextColor(Color.parseColor("RED"));
            }
            else {
                itemName.setTextColor(Color.parseColor("BLUE"));
                requestNeeded.setTextColor(Color.parseColor("BLUE"));
                requestRaised.setTextColor(Color.parseColor("BLUE"));
                //     requestWorkerID.setTextColor(Color.parseColor("BLUE"));

            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return currentView;
    }
}
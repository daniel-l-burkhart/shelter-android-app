package com.example.shelterconnect.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
import com.example.shelterconnect.controller.requests.DonorGetRequestActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Donation;
import com.example.shelterconnect.model.Request;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MakeDonationActivity extends AppCompatActivity {
    private Button submit;
    private Donation donation;
    private Request sendingRequest;
    private int requestIDInt;
    private String email = "";
    private int donorID = 0;
    private Double amountToDonate;

    private TextView dollarAmount;
    private TextView quantityValue;
    private SeekBar seekBar;
    public int userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_donation);

        userLevel = Functions.getUserLevel(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            this.email = user.getEmail();
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_DONORID_FROM_EMAIL+this.email, null, Api.CODE_GET_REQUEST);
            request.execute();
        }

        Intent sendingIntent = getIntent();
        this.sendingRequest = (Request) sendingIntent.getSerializableExtra("requestObject");
        this.requestIDInt = this.sendingRequest.getRequestID();

        dollarAmount = findViewById(R.id.dollarAmount);

        TextView itemName = findViewById(R.id.itemName);
        itemName.setText("Item Name: " + this.sendingRequest.getName());

        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(this.sendingRequest.getQuantity());



        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int currValue, int newValue) {
                 amountToDonate = (newValue * sendingRequest.getItemPrice());
                dollarAmount.setText("$"+amountToDonate.toString());
            }
        });

        System.out.println("REQUEST NAME: " + this.sendingRequest.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("MAKE A DONATION");
        toolbar.setSubtitle("");

        submit = (Button) findViewById(R.id.btnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(donorID != 0 && sendingRequest.getRequestID() != 0 && amountToDonate != 0){

                    HashMap<String, String> params = new HashMap<>();
                    params.put("donorID", Integer.toString(donorID));
                    params.put("requestID", Integer.toString(sendingRequest.getRequestID()));
                    params.put("amountDonated", amountToDonate.toString());

                    System.out.println(donorID);
                    System.out.println(sendingRequest.getRequestID());
                    System.out.println(amountToDonate);

                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_DONATION, params, Api.CODE_POST_REQUEST);
                    request.execute();

                    String newActive = "1";
                    if((sendingRequest.getAmountRaised() + amountToDonate) < sendingRequest.getAmountNeeded()){
                        newActive = "0";
                    }

                    HashMap<String, String> requestParams = new HashMap<>();
                    requestParams.put("rID", Integer.toString(sendingRequest.getRequestID()));
                    requestParams.put("itemID", Integer.toString(sendingRequest.getItemID()));
                    requestParams.put("quantity", Integer.toString(sendingRequest.getQuantity()));
                    double newAmount = sendingRequest.getAmountRaised() + amountToDonate;
                    requestParams.put("amountRaised", Double.toString(newAmount));
                    requestParams.put("amountNeeded", Double.toString(sendingRequest.getAmountNeeded()));
                    requestParams.put("active", newActive);


                    System.out.println("NEW AMOUNT " + newAmount);

                    request = new PerformNetworkRequest(Api.URL_UPDATE_REQUEST, requestParams, Api.CODE_POST_REQUEST);
                    request.execute();

                } else if(donorID == 0){
                    Toast.makeText(getApplicationContext(), "Cannot make donation - not logged in", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem homeMenu = menu.findItem(R.id.home);
        MenuItem listItems = menu.findItem(R.id.listItems);
        MenuItem addItem = menu.findItem(R.id.addItem);
        MenuItem editItems = menu.findItem(R.id.editItems);
        MenuItem editWorkers = menu.findItem(R.id.editWorkers);
        MenuItem logoutMenu = menu.findItem(R.id.logout);

        if (userLevel == 0) {
            homeMenu.setVisible(true);
            listItems.setVisible(false);
            addItem.setVisible(false);
            editItems.setVisible(false);
            editWorkers.setVisible(false);
            logoutMenu.setVisible(true);
        } else if (userLevel == 1) {
            homeMenu.setVisible(true);
            listItems.setVisible(true);
            addItem.setVisible(true);
            editItems.setVisible(true);
            editWorkers.setVisible(false);
            logoutMenu.setVisible(true);
        } else if (userLevel == 2) {
            homeMenu.setVisible(true);
            listItems.setVisible(true);
            addItem.setVisible(true);
            editItems.setVisible(true);
            editWorkers.setVisible(true);
            logoutMenu.setVisible(true);
        } else {
            homeMenu.setVisible(true);
            listItems.setVisible(false);
            addItem.setVisible(false);
            editItems.setVisible(false);
            editWorkers.setVisible(false);
            logoutMenu.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home) {

            int userLevel = Functions.getUserLevel(this);

            if (userLevel == -1) {
                Toast.makeText(getApplicationContext(), "Please sign in to go to your homepage", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(this, LoginActivity.class);
                startActivity(myIntent);
                return true;
            } else if (userLevel == 0) {
                Intent myIntent = new Intent(this, DonorHomeActivity.class);
                startActivity(myIntent);
                return true;
            } else if (userLevel == 1) {
                Intent myIntent = new Intent(this, WorkerHomeActivity.class);
                startActivity(myIntent);
                return true;
            } else if (userLevel == 2) {
                Intent myIntent = new Intent(this, OrganizerHomeActivity.class);
                startActivity(myIntent);
                return true;
            }

        } else if (id == R.id.listItems && (userLevel > 0)) {
            Intent myIntent = new Intent(this, ReadItemActivity.class);
            startActivity(myIntent);
            return true;

        } else if (id == R.id.addItem && (userLevel > 0)) {
            Intent myIntent = new Intent(this, CreateItemActivity.class);
            startActivity(myIntent);
            return true;

        } else if (id == R.id.editItems && (userLevel > 0)) {
            Intent myIntent = new Intent(this, UpdateItemActivity.class);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            getSharedPreferences("userLevel", Context.MODE_PRIVATE).edit().putString("position", "-1").apply();
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.editWorkers && (userLevel > 1)) {
            startActivity(new Intent(this, WorkerListDeleteActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);

                if (!object.getBoolean("error")) {

                    if(object.has("donorID") && !object.isNull("donorID")){
                        donorID = object.getInt("donorID");
                    }
                    else if(object.has("donations") && !object.isNull("donations")) {

                        Toast.makeText(getApplicationContext(), "Donation made!", Toast.LENGTH_LONG).show();

                    } else if(object.has("requests") && !object.isNull("requests")){

                        Toast.makeText(getApplicationContext(), "Request updated!", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(MakeDonationActivity.this, DonorGetRequestActivity.class);
                        startActivity(myIntent);

                    }
                } else{
                    System.out.println(object.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == Api.CODE_POST_REQUEST) {
                return requestHandler.sendPostRequest(url, params);
            }

            if (requestCode == Api.CODE_GET_REQUEST) {
                return requestHandler.sendGetRequest(url);
            }

            return null;
        }
    }


}

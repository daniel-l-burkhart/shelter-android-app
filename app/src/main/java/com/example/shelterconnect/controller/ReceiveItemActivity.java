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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.RequestAdapter;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;
import com.example.shelterconnect.model.Request;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiveItemActivity extends AppCompatActivity {

    private ArrayList<Item> itemList = new ArrayList<Item>();
    private ArrayList<Request> requestList = new ArrayList<Request>();
    private Request foundRequest = null;
    private Item foundItem = null;
    public int userLevel;

    private int requestIDInt;
    private NumberPicker numberPicker;
    private TextView itemName;
    private Button receiveItemButton;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_item);
        String requestID = (String) getIntent().getExtras().get(RequestAdapter.REQUEST_ID_EXTRA);
        this.requestIDInt = Integer.parseInt(requestID);
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_REQUESTS, null, Api.CODE_GET_REQUEST);
        request.execute();

        userLevel = Functions.getUserLevel(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("RECEIVE ITEM");
        toolbar.setSubtitle("");

        this.numberPicker = findViewById(R.id.numberPicker);
        this.itemName = findViewById(R.id.itemName);
        this.receiveItemButton = findViewById(R.id.receiveItemButton);

        this.updateUI();

        this.receiveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (foundRequest == null) {
                    //Show Toast and get out of there.
                    Toast.makeText(getApplicationContext(), "Something went wrong, unable to receive", Toast.LENGTH_LONG).show();
                } else {

                    int receivedQuantity = numberPicker.getValue();

                    double amountRaised = foundItem.getPrice() * receivedQuantity;

                    double newAmountNeeded = foundRequest.getAmountNeeded() - amountRaised;
                    foundRequest.setAmountNeeded(newAmountNeeded);

                    int newQuantity = foundRequest.getQuantity() - receivedQuantity;
                    foundRequest.setQuantity(newQuantity);

                    System.out.println("GOT TO UPDATE REQUEST LIST!");

                    String activeString = "1";

                    if (foundRequest.isActive()) {
                        activeString = "0";
                    }

                    HashMap<String, String> params = new HashMap<>();
                    params.put("rID", Integer.toString(foundRequest.getRequestID()));
                    params.put("quantity", Integer.toString(foundRequest.getQuantity()));
                    params.put("amountRaised", Double.toString(foundRequest.getAmountRaised()));
                    params.put("amountNeeded", Double.toString(foundRequest.getAmountNeeded()));
                    params.put("workerID", Integer.toString(foundRequest.getEmployeeID()));
                    params.put("itemID", Integer.toString(foundRequest.getItemID()));
                    params.put("active", activeString);

                    update = true;
                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_REQUEST, params, Api.CODE_POST_REQUEST);
                    request.execute();
                }

            }
        });


    }

    private void refreshItemList(JSONArray items) throws JSONException {
        itemList.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            System.out.println(obj);

            itemList.add(new Item(
                    obj.getInt("itemID"),
                    obj.getString("name"),
                    obj.getDouble("price"),
                    obj.getInt("quantity")
            ));
        }
    }

    private void refreshRequestList(JSONArray request) throws JSONException {
        requestList.clear();

        for (int i = 0; i < request.length(); i++) {
            JSONObject obj = request.getJSONObject(i);

            System.out.println(obj);

            int activeInt = obj.getInt("active");
            boolean active = false;

            if (activeInt == 0) {
                active = true;
            } else if (activeInt == 1) {
                active = false;
            }

            requestList.add(new Request(
                    obj.getInt("requestID"),
                    obj.getInt("quantity"),
                    obj.getDouble("amountNeeded"),
                    obj.getDouble("amountRaised"),
                    obj.getInt("workerID"),
                    obj.getInt("itemID"), active)
            );
        }
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
                if (!object.getBoolean("error") && update) {

                    Toast.makeText(getApplicationContext(), "updated request!", Toast.LENGTH_LONG).show();

                } else if(!object.getBoolean("error") && !update){

                        refreshItemList(object.getJSONArray("items"));
                        refreshRequestList(object.getJSONArray("requests"));
                        updateUI();
                        numberPicker.invalidate();
                        itemName.invalidate();
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

    private void updateUI() {

        for (Request currRequest : this.requestList) {
            if (currRequest.getRequestID() == requestIDInt) {
                foundRequest = currRequest;
            }
        }

        if (foundRequest != null) {

            System.out.println("FOUND REQUEST " + foundRequest.getQuantity());

            for (Item currItem : this.itemList) {
                if (currItem.getItemID() == foundRequest.getItemID()) {
                    foundItem = currItem;
                }
            }

            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(foundRequest.getQuantity());
        }

        if (foundItem != null) {
            TextView itemName = findViewById(R.id.itemName);
            String itemNameString = "Item Name: " + foundItem.getName();
            itemName.setText(itemNameString);
        }
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

        } else if (id == R.id.listItems) {
            Intent myIntent = new Intent(this, ReadItemActivity.class);
            startActivity(myIntent);
            return true;

        } else if (id == R.id.addItem) {
            Intent myIntent = new Intent(this, CreateItemActivity.class);
            startActivity(myIntent);
            return true;

        } else if (id == R.id.editItems) {
            Intent myIntent = new Intent(this, UpdateItemActivity.class);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            getSharedPreferences("userLevel", Context.MODE_PRIVATE).edit().putString("position", "-1").apply();
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.editWorkers) {
            startActivity(new Intent(this, WorkerListDeleteActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}

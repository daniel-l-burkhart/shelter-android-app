package com.example.shelterconnect.controller.requests;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by parth on 18-03-2018.
 */

public class CreateRequestActivity extends AppCompatActivity {

    private TextView requiredAmount;
    private TextView raisedAmount;
    private TextView itemQuantity;
    private TextView workID;
    private TextView itemID;
    Spinner itemNameSpinner;
    private Boolean active = true;
    private String num;

    private HashMap<String, Integer> itemIdNameMap;
    private HashMap<String, Integer> itemIdQuantityMap;
    private HashMap<Integer, Double> itemIDPriceMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        String workerId = getSharedPreferences("userLevel", Context.MODE_PRIVATE).getString("workerId", "");
        itemIdNameMap = new HashMap<>();
        itemIdQuantityMap = new HashMap<>();
        this.itemIDPriceMap = new HashMap<>();

        GetItemNetworkRequest getItemNetworkRequest = new GetItemNetworkRequest(Api.URL_READ_ITEMS, Api.CODE_GET_REQUEST);
        getItemNetworkRequest.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Create Request");
        toolbar.setSubtitle("");
        itemNameSpinner = findViewById(R.id.itemNameSpinner);
        this.requiredAmount = findViewById(R.id.requiredAmount);
        this.raisedAmount = findViewById(R.id.raisedAmount);
        this.itemQuantity = findViewById(R.id.quantity);
        this.workID = findViewById(R.id.workerID);
        this.itemID = findViewById(R.id.itemID);

        this.requiredAmount.setKeyListener(null);
        this.raisedAmount.setKeyListener(null);
        this.workID.setKeyListener(null);
        this.itemID.setKeyListener(null);

        workID.setText(workerId);
        raisedAmount.setText(String.valueOf("0.0"));

        itemQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!itemQuantity.getText().toString().isEmpty()) {

                    String newQuantity = itemQuantity.getText().toString().trim();

                    double quantity = Double.parseDouble(newQuantity);

                    Integer itemIDInt = Integer.parseInt(itemID.getText().toString().trim());

                    double itemPrice = itemIDPriceMap.get(itemIDInt);

                    double newPrice = quantity * itemPrice;

                    requiredAmount.setText(Double.toString(newPrice));
                }

            }
        });

        //itemQuantity.setInputType(InputType.TYPE_NULL);

        Button itemButton = findViewById(R.id.requestButton);

        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItem();
            }
        });
    }

    private void createItem() {
        String rq = this.requiredAmount.getText().toString().trim();
        String ra = this.raisedAmount.getText().toString().trim();
        String wk = this.workID.getText().toString().trim();
        String it = String.valueOf(itemIdNameMap.get(itemNameSpinner.getSelectedItem().toString()));
        //String ac = this.active.getText().toString().trim();
        String quantity = this.itemQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(rq)) {
            this.requiredAmount.setError("Please enter Amount Required");
            this.requiredAmount.requestFocus();
            return;
        }

        if (Integer.parseInt(rq) < 1) {
            requiredAmount.setError(("Please enter an amount greater than 0"));
            requiredAmount.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ra)) {
            this.raisedAmount.setError("Please enter Amount that has been Raised");
            this.raisedAmount.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(wk)) {
            this.workID.setError("Please enter Worker ID");
            this.workID.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantity)) {
            this.itemQuantity.setError("Please enter quantity");
            this.itemQuantity.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(it)) {
            this.itemID.setError("Please enter Item ID");
            this.itemID.requestFocus();
            return;
        }
        //if (TextUtils.isEmpty(ac)) {
        //  this.active.setError("Please enter name");
        //this.active.requestFocus();
        //return;
        //}

        HashMap<String, String> params = new HashMap<>();
        params.put("quantity", quantity);
        params.put("amountNeeded", rq);
        params.put("amountRaised", ra);
        params.put("workerID", wk);
        params.put("itemID", it);
        params.put("active", active.toString());

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_REQUEST, params, Api.CODE_POST_REQUEST);
        request.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.addItem) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(this, com.example.shelterconnect.controller.items.CreateItemActivity.class);
            startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshItemList(JSONArray items) throws JSONException {
        itemIdNameMap.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);
            itemIdNameMap.put(obj.getString("name"), obj.getInt("itemID"));
            itemIdQuantityMap.put(obj.getString("name"), obj.getInt("quantity"));
            this.itemIDPriceMap.put(obj.getInt("itemID"), obj.getDouble("price"));
        }

        List<String> itemNames = new ArrayList<>(itemIdNameMap.keySet());

        itemNameSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, itemNames));
        itemNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemID.setText(String.valueOf(itemIdNameMap.get(itemNameSpinner.getSelectedItem().toString())));
//                itemQuantity.setText(String.valueOf(itemIdQuantityMap.get(itemNameSpinner.getSelectedItem().toString())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(CreateRequestActivity.this, GetRequestActivity.class);
                    startActivity(myIntent);
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


    private class GetItemNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        int requestCode;

        GetItemNetworkRequest(String url, int requestCode) {
            this.url = url;
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
                    refreshItemList(object.getJSONArray("items"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            return requestHandler.sendGetRequest(url);
        }
    }
}

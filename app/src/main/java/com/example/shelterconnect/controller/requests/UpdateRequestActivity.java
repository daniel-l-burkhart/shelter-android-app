package com.example.shelterconnect.controller.requests;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.RequestAdapter;
import com.example.shelterconnect.database.Api;

import com.example.shelterconnect.database.RequestHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by parth on 18-03-2018.
 */

public class UpdateRequestActivity extends AppCompatActivity {
    private TextView rID;
    private TextView reqAmt;
    private TextView raiAmt;
    private TextView itemQuantity;
    private TextView workID;
    private TextView itemID;
    private Boolean active;
    private String num;

    private Map<Integer, String> itemIdNameMap;
    private Map<Integer, String> itemIdQuantityMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_list_edit);


        String workerId =  getSharedPreferences("userLevel", Context.MODE_PRIVATE).getString("workerId", "");
        String requestID = (String) Objects.requireNonNull(getIntent().getExtras()).get(RequestAdapter.REQUEST_ID_EXTRA);
        String itemIDView = (String) Objects.requireNonNull(getIntent().getExtras().get(RequestAdapter.ITEM_NAME_EXTRA));
        String requiredAmt = (String) Objects.requireNonNull(getIntent().getExtras().get(RequestAdapter.REQUEST_NEEDED_EXTRA));
        String raisedAmt = (String) Objects.requireNonNull(getIntent().getExtras().get(RequestAdapter.REQUEST_RAISED_EXTRA));
        String quantity = (String) Objects.requireNonNull(getIntent().getExtras().get(RequestAdapter.REQUEST_QUANTITY_EXTRA));
        String realItemId = (String) Objects.requireNonNull(getIntent().getExtras().get(RequestAdapter.ITEM_ID_EXTRA));
        num = realItemId;
        itemIdNameMap = new HashMap<>();
        itemIdQuantityMap = new HashMap<>();

        GetItemNetworkRequest getItemNetworkRequest = new GetItemNetworkRequest(Api.URL_READ_ITEMS, Api.CODE_GET_REQUEST);
        getItemNetworkRequest.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Create Request");
        toolbar.setSubtitle("");

        this.reqAmt = findViewById(R.id.requiredAmount);
        reqAmt.setText(requiredAmt);
        this.raiAmt = findViewById(R.id.raisedAmount);
        raiAmt.setText(raisedAmt);
        this.itemQuantity = findViewById(R.id.quantity);
        itemQuantity.setText(quantity);
        this.workID = findViewById(R.id.workerID);
        workID.setText(workerId);
        this.itemID = findViewById(R.id.itemID);
        itemID.setText(itemIDView);
        //this.active = findViewById(R.id.active);
        itemQuantity.setInputType(InputType.TYPE_NULL);
        itemID.setInputType(InputType.TYPE_NULL);
        raiAmt.setInputType(InputType.TYPE_NULL);
        workID.setInputType(InputType.TYPE_NULL);
        this.rID = findViewById(R.id.requestIdView);
        rID.setText(requestID);

        Switch simpleSwitch = (Switch) findViewById(R.id.active);



 //       if (simpleSwitch.isChecked() == true) {
   //         active = true;
      //  }
       // else {
        //    active = false;
        //}

        Log.d("active show value", String.valueOf(simpleSwitch.isChecked()));

        Button itemButton = findViewById(R.id.updaterequestButton);

        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRequest();
            }
        });

        itemID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!itemID.getText().toString().isEmpty()) {
                    try {
                        //num = itemID.getText().toString().trim();
                        Integer value = Integer.valueOf(itemID.getText().toString());
                        itemID.setText(itemIdNameMap.get(value));
                        itemQuantity.setText(itemIdQuantityMap.get(value));
                    } catch (NumberFormatException e) {
                        itemID.setError("Please enter Item Id.");
                        itemID.requestFocus();
                    }
                }
            }
        });
    }

    private void updateRequest() {
        String rq = this.reqAmt.getText().toString().trim();
        String ra = this.raiAmt.getText().toString().trim();
        String wk = this.workID.getText().toString().trim();
        String it = num;
        String rid = this.rID.getText().toString().trim();

        String quantity = this.itemQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(rq)) {
            this.reqAmt.setError("Please enter Amount Required");
            this.reqAmt.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ra)) {
            this.raiAmt.setError("Please enter Amount that has been Raised");
            this.raiAmt.requestFocus();
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
        params.put("rID", rid);
        params.put("quantity", quantity);
        params.put("amountNeeded", rq);
        params.put("amountRaised", ra);
        params.put("workerID", wk);
        params.put("itemID", it);
        params.put("active",String.valueOf(active));

        Log.d("active value:", String.valueOf(active));

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_REQUEST, params, Api.CODE_POST_REQUEST);
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
            itemIdNameMap.put(obj.getInt("itemID"), obj.getString("name"));
            itemIdQuantityMap.put(obj.getInt("itemID"), obj.getString("quantity"));
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

                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(UpdateRequestActivity.this, GetRequestActivity.class);
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

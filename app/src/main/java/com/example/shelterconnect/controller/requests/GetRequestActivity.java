package com.example.shelterconnect.controller.requests;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.RequestAdapter;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;
import com.example.shelterconnect.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by parth on 18-03-2018.
 */

public class GetRequestActivity extends AppCompatActivity {


    ArrayList<Request> requestList;
    ArrayList<Item> itemList;
    private ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Requests");
        toolbar.setSubtitle("");

        this.myListView = (ListView) findViewById(R.id.requestList);

        this.itemList = new ArrayList<Item>();
        this.requestList = new ArrayList<Request>();

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_REQUESTS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_requests, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem request) {

        int id = request.getItemId();

        if (id == R.id.addRequest) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(this, CreateRequestActivity.class);
            startActivity(myIntent);

            return true;

        } else if (id == R.id.editRequest) {
            Intent myIntent = new Intent(this, UpdateRequestActivity.class);
            startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(request);
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
            Log.d("w is active", String.valueOf(activeInt));
            boolean active = true;

            if (activeInt == 0) {
                active = true;
            } else if (activeInt == 1) {
                active = false;
            }

            int checkNeeded = obj.getInt("amountNeeded");
            int checkquantity = obj.getInt("quantity");

            if (checkquantity >= checkNeeded) {
                active = false;
            }

            Request r = new Request(obj.getInt("requestID"),
                    obj.getInt("quantity"),
                    obj.getDouble("amountNeeded"),  // needed attribute
                    obj.getDouble("amountRaised"),// achieved attribute
                    obj.getInt("workerID"),
                    obj.getInt("itemID"), active);
            for (Item item : itemList) {
                if (item.getItemID() == r.getItemID()) {
                    r.setName(item.getName());
                }
            }
            System.out.println("NAME: " + r.getName());
            requestList.add(r);
        }


        //MATCH ITEM ID TO NAME

        RequestAdapter adapter = new RequestAdapter(this, this.requestList);
        this.myListView.setAdapter(adapter);
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
                    refreshItemList(object.getJSONArray("items"));
                    refreshRequestList(object.getJSONArray("requests"));
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


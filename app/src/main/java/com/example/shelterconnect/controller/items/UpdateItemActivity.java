package com.example.shelterconnect.controller.items;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.ItemEditAdapter;
import com.example.shelterconnect.controller.DonorHomeActivity;
import com.example.shelterconnect.controller.LoginActivity;
import com.example.shelterconnect.controller.OrganizerHomeActivity;
import com.example.shelterconnect.controller.WorkerHomeActivity;
import com.example.shelterconnect.controller.WorkerListDeleteActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateItemActivity extends AppCompatActivity {

    ArrayList<Item> itemList;
    private ListView myListView;
    private RecyclerView rv;
    private LinearLayoutManager mLayoutManager;
    public int userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        userLevel = Functions.getUserLevel(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("EDIT ITEMS");
        toolbar.setSubtitle("");

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        this.myListView = (ListView) findViewById(R.id.itemList);
        this.itemList = new ArrayList<>();
        this.readItems();

        Button submitButton = findViewById(R.id.submitChangesButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItems();
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

    private void readItems() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ITEMS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void updateItems() {

        for (Item currItem : this.itemList) {
            if (currItem.hasBeenEdited()) {

                System.out.println("GOT TO UPDATE ITEMS!");

                HashMap<String, String> params = new HashMap<>();
                params.put("itemID", Integer.toString(currItem.getItemID()));
                params.put("name", currItem.getName());
                params.put("price", Double.toString(currItem.getPrice()));
                params.put("quantity", Integer.toString(currItem.getQuantity()));

                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_ITEM, params, Api.CODE_POST_REQUEST);
                request.execute();

                if (userLevel > 0) {
                    Intent myIntent = new Intent(this, ReadItemActivity.class);
                    startActivity(myIntent);
                }
            }
        }

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

        ItemEditAdapter adapter = new ItemEditAdapter(this.itemList, this);
        this.rv.setAdapter(adapter);
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
            } else if (requestCode == Api.CODE_GET_REQUEST) {
                return requestHandler.sendGetRequest(url);
            }

            return null;
        }
    }

}
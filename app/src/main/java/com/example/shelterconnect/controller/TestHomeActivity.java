package com.example.shelterconnect.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
//import com.example.shelterconnect.controller.requests.ClosedRequestActivity;
import com.example.shelterconnect.controller.requests.CreateRequestActivity;
import com.example.shelterconnect.controller.requests.DonorGetRequestActivity;
import com.example.shelterconnect.controller.requests.GetRequestActivity;
import com.example.shelterconnect.controller.requests.OrganizerGetRequestActivity;
import com.example.shelterconnect.controller.requests.WorkerGetRequestActivity;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;

public class TestHomeActivity extends AppCompatActivity implements View.OnClickListener {

    public int userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_home);

        userLevel = Functions.getUserLevel(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("TEST HOMEPAGE");
        toolbar.setSubtitle("");

        findViewById(R.id.goItemList).setOnClickListener(this);
        findViewById(R.id.goAddItem).setOnClickListener(this);
        findViewById(R.id.goLoginPage).setOnClickListener(this);
        findViewById(R.id.goWorkerList).setOnClickListener(this);
        findViewById(R.id.viewRequest).setOnClickListener(this);
        findViewById(R.id.newRequest).setOnClickListener(this);
        findViewById(R.id.goDonorHome).setOnClickListener(this);
        findViewById(R.id.goWorkerHome).setOnClickListener(this);
        findViewById(R.id.goOrganizerHome).setOnClickListener(this);
        findViewById(R.id.goWorkerListDelete).setOnClickListener(this);
        findViewById(R.id.donorViewRequest).setOnClickListener(this);
        findViewById(R.id.organizerViewRequest).setOnClickListener(this);
        findViewById(R.id.workerViewRequest).setOnClickListener(this);
        //   findViewById(R.id.closedViewRequest).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goItemList:
                startActivity(new Intent(this, ReadItemActivity.class));

                break;
            case R.id.goAddItem:
                startActivity(new Intent(this, CreateItemActivity.class));

                break;
            case R.id.goLoginPage:
                startActivity(new Intent(this, LoginActivity.class));

                break;
            case R.id.goWorkerList:
                startActivity(new Intent(this, WorkerListActivity.class));

                break;
            case R.id.newRequest:
                startActivity(new Intent(this, CreateRequestActivity.class));

                break;
            case R.id.viewRequest:
                startActivity(new Intent(this, GetRequestActivity.class));

                break;
            case R.id.goDonorHome:
                startActivity(new Intent(this, DonorHomeActivity.class));

                break;
            case R.id.goWorkerHome:
                startActivity(new Intent(this, WorkerHomeActivity.class));

                break;
            case R.id.goOrganizerHome:
                startActivity(new Intent(this, OrganizerHomeActivity.class));

                break;
            case R.id.goWorkerListDelete:
                startActivity(new Intent(this, WorkerListDeleteActivity.class));

                break;
            case R.id.donorViewRequest:
                startActivity(new Intent(this, DonorGetRequestActivity.class));

                break;
            case R.id.organizerViewRequest:
                startActivity(new Intent(this, OrganizerGetRequestActivity.class));

                break;
            case R.id.workerViewRequest:
                startActivity(new Intent(this, WorkerGetRequestActivity.class));

                break;
            //  case R.id.closedViewRequest:
            //    startActivity(new Intent(this, ClosedRequestActivity.class));

            //  break;

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

package com.example.shelterconnect.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Donor;
import com.example.shelterconnect.model.Employee;
import com.example.shelterconnect.util.Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by paulv on 3/17/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText signInEmail, signInPassword;
    ProgressBar progressBar;
    ArrayList<Donor> donorList;
    ArrayList<Employee> workerList;
    SharedPreferences userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("LOGIN");
        toolbar.setSubtitle("");

        mAuth = FirebaseAuth.getInstance();
        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        progressBar = findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(View.GONE);
        userLevel = getSharedPreferences("userLevel", Context.MODE_PRIVATE);


        LoginActivity.PerformNetworkRequest request = new LoginActivity.PerformNetworkRequest(Api.URL_READ_USERS, null, Api.CODE_GET_REQUEST);
        request.execute();

        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.sign_in).setOnClickListener(this);

        this.donorList = new ArrayList<Donor>();
        this.workerList = new ArrayList<Employee>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
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
                System.out.println("OBJECT!!!!!!!!!" + object);

                if (!object.getBoolean("error")) {

                    JSONArray donors = object.getJSONArray("donors");
                    JSONArray workers = object.getJSONArray("workers");

                        refreshWorkerList(workers);
                        refreshDonorList(donors);


                   // refreshDonorList(object.getJSONArray("donors"));
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

    private void readDonors() {
        LoginActivity.PerformNetworkRequest request = new LoginActivity.PerformNetworkRequest(Api.URL_READ_DONORS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void readWorkers() {
        LoginActivity.PerformNetworkRequest request = new LoginActivity.PerformNetworkRequest(Api.URL_READ_EMPLOYEE, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshDonorList(JSONArray items) throws JSONException {
        donorList.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            donorList.add(new Donor(
                    obj.getInt("donorID"),
                    obj.getString("name"),
                    obj.getString("phone"),
                    obj.getString("address"),
                    obj.getString("email")
            ));
        }
    }

    private void refreshWorkerList(JSONArray items) throws JSONException {

        System.out.println("GOT TO REFRESH WORKER LIST!!!!!!!!");
        workerList.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            System.out.println("Position! " + obj.getInt("position"));
//int employeeID, String name, int position, String phone, String email, String address
            workerList.add(new Employee(
                    obj.getInt("workerID"),
                    obj.getString("name"),
                    obj.getInt("position"),
                    obj.getString("phone"),
                    obj.getString("email"),
                    obj.getString("address")
            ));
        }
    }

    private void userLogin() {
        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        //Check if email is empty
        if (email.isEmpty()) {
            signInEmail.setError("Email is required.");
            signInEmail.requestFocus();
            return;
        }

        //Check if a valid email is used
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signInEmail.setError("Please enter a valid E-mail");
            signInEmail.requestFocus();
            return;
        }

        //Check if password is empty
        if (password.isEmpty()) {
            signInPassword.setError("Password is required");
            signInPassword.requestFocus();
            return;
        }

        //Check if password is at least 6 characters
        if (password.length() < 6) {
            signInPassword.setError("Minimum length of password must be 6 characters");
            signInPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            String fireBaseEmail = "";
            Donor currDonor = null;
            Employee currWorker = null;

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    fireBaseEmail = mAuth.getCurrentUser().getEmail();
                    Log.d("email", fireBaseEmail);
                    for (Donor d : donorList) {
                        if (fireBaseEmail.equals(d.getEmail())) {
                            currDonor = d;
                            //Go to donor home page
                            SharedPreferences.Editor editor = userLevel.edit();
                            editor.putString("position", "0");
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "Donor login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DonorHomeActivity.class));
                        }
                    }
                    for (Employee e : workerList) {
                        if (fireBaseEmail.equals(e.getEmail()) & (currDonor == null)) {
                            currWorker = e;
                            if (e.getPosition() == 1) {
                                //Go to worker home page
                                SharedPreferences.Editor editor = userLevel.edit();
                                editor.putString("position", "1");
                                editor.putString("workerId", String.valueOf(e.getEmployeeID()));
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "Employee login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), WorkerHomeActivity.class));
                            } else if (e.getPosition() == 2) {
                                //Go to organizer home page
                                SharedPreferences.Editor editor = userLevel.edit();
                                editor.putString("position", "2");
                                editor.putString("workerId", String.valueOf(e.getEmployeeID()));
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "Organizer login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), OrganizerHomeActivity.class));
                            }
                        }
                    }
                } else {
                 //   progressBar.setVisibility(View.GONE);
                   // Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(this, SignUpActivity.class));

                break;
            case R.id.sign_in:
                userLogin();

                break;
        }
    }
}

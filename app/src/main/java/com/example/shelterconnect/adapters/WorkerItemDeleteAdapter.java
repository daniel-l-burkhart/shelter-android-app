package com.example.shelterconnect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.TestHomeActivity;
import com.example.shelterconnect.controller.WorkerListDeleteActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Employee;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkerItemDeleteAdapter extends ArrayAdapter<Employee> {

    private ArrayList<Employee> employee;
    private Context adapterContext;
    private Employee currEmployee;

    public WorkerItemDeleteAdapter(Context context, ArrayList<Employee> employees) {
        super(context, R.layout.activity_worker_list_delete, employees);
        adapterContext = context;
        this.employee = employees;
    }

    /**
     * @param indexPosition
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int indexPosition, View convertView, ViewGroup parent) {
        View currentView = convertView;
        String employeePositionText = new String("");

        try {
            this.currEmployee = this.employee.get(indexPosition);

            if (currentView == null) {
                LayoutInflater vi = (LayoutInflater) this.adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                currentView = vi.inflate(R.layout.worker_delete_list_item, null);
            }

            TextView employeeName = (TextView) currentView.findViewById(R.id.name);
            String name = currEmployee.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            employeeName.setText(name);

            TextView employeePosition = (TextView) currentView.findViewById(R.id.position);

            if (currEmployee.getPosition() == 1) {
                employeePositionText = "Employee";
            } else if (currEmployee.getPosition() == 2) {
                employeePositionText = "Organizer";
            }
            employeePosition.setText(employeePositionText);


            TextView employeeEmail = (TextView) currentView.findViewById(R.id.email);
            employeeEmail.setText(currEmployee.getEmail());

            Button deleteButton = (Button) currentView.findViewById(R.id.deleteWorkerButton);

            final Employee deletedEmployee = employee.get(indexPosition);
            // Use the Builder class for convenient dialog construction
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder((WorkerListDeleteActivity) adapterContext);
            builder.setMessage("Are you sure you want to delete?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.out.println("DELETING ITEM!!!");
                            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_EMPLOYEE + deletedEmployee.getEmployeeID(), null, Api.CODE_GET_REQUEST);
                            System.out.println(Api.URL_DELETE_EMPLOYEE + deletedEmployee.getEmployeeID());
                            request.execute();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }


        currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog builder;
                String worker = "";

                Employee myEmployee = employee.get(indexPosition);

                if (myEmployee.getPosition() == 1) {
                    worker = ("WorkerID: " + myEmployee.getEmployeeID() + "\n\n" +
                            "Name: " + myEmployee.getName() + "\n\n" +
                            "Position: Worker" + "\n\n" +
                            "Phone: " + myEmployee.getPhone() + "\n\n" +
                            "Address: " + myEmployee.getAddress() + "\n\n" +
                            "Email: " + myEmployee.getEmail());
                } else if (myEmployee.getPosition() == 2) {
                    worker = ("WorkerID: " + myEmployee.getEmployeeID() + "\n\n" +
                            "Name: " + myEmployee.getName() + "\n\n" +
                            "Position: Organizer" + "\n\n" +
                            "Phone: " + myEmployee.getPhone() + "\n\n" +
                            "Address: " + myEmployee.getAddress() + "\n\n" +
                            "Email: " + myEmployee.getEmail());
                }

                builder = new AlertDialog.Builder(adapterContext).create();
                builder.setTitle("Worker Information");
                builder.setMessage(worker);
                builder.show();
            }
        });


        return currentView;
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
                    Toast.makeText(adapterContext, "Delete Successful!", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(adapterContext, WorkerListDeleteActivity.class);
                    adapterContext.startActivity(myIntent);
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


    private android.app.AlertDialog getDeleteDialog(Activity activity) {
        // Employee myEmployee = employee.get(indexPosition);
        // Use the Builder class for convenient dialog construction
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("DELETING ITEM!!!");
                        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_EMPLOYEE + currEmployee.getEmployeeID(), null, Api.CODE_GET_REQUEST);
                        System.out.println(Api.URL_DELETE_EMPLOYEE + currEmployee.getEmployeeID());
                        request.execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
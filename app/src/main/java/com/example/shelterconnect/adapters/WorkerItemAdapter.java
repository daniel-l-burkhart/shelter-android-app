package com.example.shelterconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.model.Employee;

import java.util.ArrayList;

public class WorkerItemAdapter extends ArrayAdapter<Employee> implements View.OnClickListener {

    private ArrayList<Employee> employee;
    private Context adapterContext;
    private Employee currEmployee;
    private AlertDialog builder;

    public WorkerItemAdapter(Context context, ArrayList<Employee> employees) {
        super(context, R.layout.activity_worker_list, employees);
        adapterContext = context;
        this.employee = employees;

        builder = new android.support.v7.app.AlertDialog.Builder(adapterContext).create();
        builder.setTitle("Worker Information");
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
                currentView = vi.inflate(R.layout.worker_list_item, null);
            }

            currentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String worker = "";

                    Employee myEmployee = employee.get(indexPosition);

                    if (currEmployee.getPosition() == 1) {
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

                    builder.setMessage(worker);
                    builder.show();
                }
            });

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

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }


        return currentView;
    }


    @Override
    public void onClick(View v) {
        String worker = "";

        if (currEmployee.getPosition() == 1) {
            worker = ("WorkerID: " + currEmployee.getEmployeeID() + "\n\n" +
                    "Name: " + currEmployee.getName() + "\n\n" +
                    "Position: Worker" + "\n\n" +
                    "Phone: " + currEmployee.getPhone() + "\n\n" +
                    "Address: " + currEmployee.getAddress() + "\n\n" +
                    "Email: " + currEmployee.getEmail());
        } else if (currEmployee.getPosition() == 2) {
            worker = ("WorkerID: " + currEmployee.getEmployeeID() + "\n\n" +
                    "Name: " + currEmployee.getName() + "\n\n" +
                    "Position: Organizer" + "\n\n" +
                    "Phone: " + currEmployee.getPhone() + "\n\n" +
                    "Address: " + currEmployee.getAddress() + "\n\n" +
                    "Email: " + currEmployee.getEmail());
        }

        builder.setMessage(worker);
        builder.show();
    }
}
package com.example.shelterconnect.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Edit Item Adapter
 * Created by daniel on 3/4/18.
 */
public class ItemEditAdapter extends RecyclerView.Adapter<ItemEditAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    private ItemViewHolder holder;
    private int itemPosition;
    private Item currItem;
    private Context adapterContext;

    public ItemEditAdapter(ArrayList<Item> myDataset, Context context) {
        this.items = myDataset;
        this.adapterContext = context;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView price;
        EditText quantity;
        ImageButton deleteButton;

        ItemViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.cv);
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
            quantity = (EditText) v.findViewById(R.id.quantity);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);

        }
    }

    public void add(int position, Item item) {
        this.items.add(position, item);
        this.notifyItemInserted(position);
    }

    public void remove(int position) {
        this.items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_edit, viewGroup, false);
        ItemViewHolder iVH = new ItemViewHolder(v);
        return iVH;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        currItem = items.get(position);
        holder.name.setText(currItem.getName());
        holder.price.setText(Double.toString(currItem.getPrice()));
        holder.quantity.setText(Integer.toString(currItem.getQuantity()));

        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currItem.setEdited(true);

                if(!holder.quantity.getText().toString().isEmpty()) {
                    currItem.setQuantity(Integer.parseInt(holder.quantity.getText().toString()));
                    System.out.println(currItem.getQuantity());
                } else if (TextUtils.isEmpty(holder.quantity.getText().toString().trim())) {
                    holder.quantity.setError("Quantity must not be empty");
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeleteDialog(((UpdateItemActivity)adapterContext), holder.getAdapterPosition()).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
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
                    Intent myInent = new Intent(adapterContext, ReadItemActivity.class);

                    adapterContext.startActivity(myInent);
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


    private AlertDialog getDeleteDialog(Activity activity, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to delete? ")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("DELETING ITEM!!!");
                        System.out.println(items.get(position).getItemID());
                        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_ITEM + items.get(position).getItemID(), null, Api.CODE_GET_REQUEST);
                        System.out.println(Api.URL_DELETE_ITEM+currItem.getItemID());
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

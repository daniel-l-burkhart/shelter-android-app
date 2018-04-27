package com.example.shelterconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.model.Item;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private Context adapterContext;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        super(context, R.layout.activity_item_list, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int indexPosition, View convertView, ViewGroup parent) {
        View currentView = convertView;

        try {
            Item currItem = this.items.get(indexPosition);

            if (currentView == null) {
                LayoutInflater vi = (LayoutInflater) this.adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                currentView = vi.inflate(R.layout.item_list_item, null);
            }

            TextView itemName = (TextView) currentView.findViewById(R.id.name);
            String name = currItem.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            itemName.setText(name);

            TextView itemPrice = (TextView) currentView.findViewById(R.id.price);

            String itemPriceText = "$"+Double.toString(currItem.getPrice());
            itemPrice.setText(itemPriceText);

            TextView itemQuantity = (TextView) currentView.findViewById(R.id.quantity);
            itemQuantity.setText(Integer.toString(currItem.getQuantity()));

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return currentView;
    }
}
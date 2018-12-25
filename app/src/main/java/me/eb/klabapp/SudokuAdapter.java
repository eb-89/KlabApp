package me.eb.klabapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SudokuAdapter extends ArrayAdapter<String> {

    public SudokuAdapter(Context context, String[] names) {
        super(context, 0, names);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String s = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false);
        }

        TextView itemNameView = (TextView) convertView.findViewById(R.id.listTextView);
        itemNameView.setText(s);

        //TODO: implement start button, call context.startActivity(...);
        //TODO: implement delete button
        //TextView selectButton = (TextView) convertView.findViewById(R.id.selectButton);
        /*
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */

        return convertView;
    }


}

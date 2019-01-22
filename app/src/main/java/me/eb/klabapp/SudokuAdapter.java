package me.eb.klabapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


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

        return convertView;
    }


}

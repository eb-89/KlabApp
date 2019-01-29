package me.eb.klabapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.eb.klabapp.roombase.Puzzle;


public class SudokuAdapter extends ArrayAdapter<Puzzle> {

    public SudokuAdapter(Context context, List<Puzzle> puzzles) {
        super(context, 0, puzzles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String s = getItem(position).publicName;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false);
        }

        TextView itemNameView = (TextView) convertView.findViewById(R.id.listTextView);
        itemNameView.setText(s);

        return convertView;
    }


}

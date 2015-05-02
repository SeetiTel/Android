package com.yoloswag.alex.seetitel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alex on 5/2/15.
 */
public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] descriptions;
    private final String[] dataType;

    public MyArrayAdapter(Context context, String[] values, String[] descriptions, String[] dataType) {
        super(context, R.layout.row_layout, values);
        this.context = context;
        this.values = values;
        this.descriptions = descriptions;
        this.dataType = dataType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        TextView title = (TextView) rowView.findViewById(R.id.label);
        TextView description = (TextView) rowView.findViewById(R.id.des);
        ImageView iv = (ImageView) rowView.findViewById(R.id.icon);

        title.setText(values[position]);
        description.setText(descriptions[position]);

        if(dataType[position].equals("text")) {
            iv.setImageResource(R.drawable.noun_text);
        }
        else if(dataType[position].equals("image")) {
            iv.setImageResource(R.drawable.noun_image);
        }
        else if (dataType[position].equals("audio")) {
            iv.setImageResource(R.drawable.noun_audio);
        }

        return rowView;
    }

}

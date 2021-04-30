package com.example.mappis;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class IconAdapter extends BaseAdapter {
    private Context context;
    private String[] values;
    private TextView prova;
    private int[] images;


    public IconAdapter(Context context, String[] values, int[] images) {
        this.context = context;
        this.values = values;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.icon_list, null);
        }


        prova = convertView.findViewById(R.id.prova);
        ImageView img = convertView.findViewById(R.id.image_icon);

        prova.setText(values[position]);
        img.setImageResource(images[position]);

        return convertView;
    }
}

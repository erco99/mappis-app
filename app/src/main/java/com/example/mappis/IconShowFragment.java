package com.example.mappis;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


public class    IconShowFragment extends DialogFragment {

    GridView gridView;
    Location currentLocation;
    MapView map;
    int[] images;

    public IconShowFragment(int[] images, Location currentLocation, MapView map) {
         this.images = images;
         this.currentLocation = currentLocation;
         this.map = map;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.icon_grid, null);

        gridView = view.findViewById(R.id.gridview);
        getDialog().setTitle("Titolo prova");

        IconAdapter adapter = new IconAdapter(getActivity(), images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconAdder iconAdder = new IconAdder(currentLocation, map, getActivity());
                if(currentLocation != null) {
                    iconAdder.insertIcon(images[position]);
                }
            }
        });

        return view;
    }
}

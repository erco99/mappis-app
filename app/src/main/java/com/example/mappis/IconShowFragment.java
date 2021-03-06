package com.example.mappis;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.osmdroid.views.MapView;

public class IconShowFragment extends DialogFragment {

    private final Location currentLocation;
    private final MapView map;
    private final int[] images;

    public IconShowFragment(int[] images, Location currentLocation, MapView map) {
         this.images = images;
         this.currentLocation = currentLocation;
         this.map = map;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.icon_grid, container, false);

        GridView gridView = view.findViewById(R.id.gridview);
        getDialog().setTitle("Titolo prova");

        IconAdapter adapter = new IconAdapter(getActivity(), images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            IconAdder iconAdder = new IconAdder(currentLocation, map, getActivity());
            if(currentLocation != null) {
                if(images == Utilities.drag_icons) {
                    iconAdder.insertDragIcon(images[position]);
                    getDialog().cancel();
                }
                if(images == Utilities.text_icons) {
                    iconAdder.insertTextIcon(images[position]);
                    getDialog().cancel();
                }
            }
        });

        return view;
    }
}

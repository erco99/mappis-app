package com.example.mappis;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class IconShowFragment extends DialogFragment {

    GridView gridView;
    static final String[] values = new String[] {
            "ciao", "come", "va",
    };

    int[] images = {R.drawable.forest, R.drawable.lake, R.drawable.waves};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.icon_grid, null);

        gridView = view.findViewById(R.id.gridview);
        getDialog().setTitle("Titolo prova");

        IconAdapter adapter = new IconAdapter(getActivity(), values, images);
        gridView.setAdapter(adapter);

        return view;
    }
}

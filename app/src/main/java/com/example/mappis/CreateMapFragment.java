package com.example.mappis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mappis.CardMaps.AddCardViewModel;
import com.example.mappis.CardMaps.CardItem;
import com.google.android.material.textfield.TextInputEditText;

public class CreateMapFragment extends Fragment {

    TextInputEditText mapName;
    TextInputEditText mapDescription;
    Button createButton;
    AddCardViewModel addCardViewModel;
    ImageFilterButton goBack;
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_map, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        mapName = view.findViewById(R.id.mapNameTextInputEditText);
        mapDescription = view.findViewById(R.id.mapDescTextInputEditText);
        spinner = (Spinner) activity.findViewById(R.id.spinner);

        addCardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddCardViewModel.class);

        createButton = view.findViewById(R.id.createMapButton);
        createButton.setOnClickListener(v -> {
            addCardViewModel.addCardItem(new CardItem(mapName.getText().toString(),
                    mapDescription.getText().toString()));
            Intent intent = new Intent(activity, MapActivity.class);
            intent.putExtra("map_type", spinner.getSelectedItem().toString());
            activity.startActivity(intent);
        });

        goBack = view.findViewById(R.id.goBackButton);
        goBack.setOnClickListener(v -> {
            ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
        });
    }
}

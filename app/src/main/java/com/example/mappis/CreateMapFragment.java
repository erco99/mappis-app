package com.example.mappis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mappis.CardMaps.AddCardViewModel;
import com.example.mappis.CardMaps.CardItem;
import com.example.mappis.CardMaps.CardViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.j256.ormlite.stmt.query.In;

public class CreateMapFragment extends Fragment {

    private TextInputEditText mapName;
    private TextInputEditText mapDescription;
    private AddCardViewModel addCardViewModel;
    private Spinner spinner;
    private int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Activity activity = getActivity();

        if (activity != null) {
            mapName = view.findViewById(R.id.mapNameTextInputEditText);
            mapDescription = view.findViewById(R.id.mapDescTextInputEditText);
            spinner = activity.findViewById(R.id.spinner);

            addCardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity)
                    .get(AddCardViewModel.class);


            CardViewModel cardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity)
                    .get(CardViewModel.class);

            cardViewModel.getLastId().observe((LifecycleOwner) activity, integer -> {
                if(integer == null) {
                    id = 0;
                } else {
                    id = integer + 1;
                }
            });

            Button createButton = view.findViewById(R.id.createMapButton);
            createButton.setOnClickListener(v -> {
                if (TextUtils.isEmpty(mapName.getText().toString()) ||
                        TextUtils.isEmpty(mapDescription.getText().toString())) {
                    Toast.makeText(activity, "Fields cannot be empty",
                            Toast.LENGTH_SHORT).show();
                } else {
                    addCardViewModel.addCardItem(new CardItem(
                            id,
                            mapName.getText().toString(),
                            mapDescription.getText().toString()));

                    Intent map_intent = new Intent(activity, MapActivity.class);

                    map_intent.putExtra("map_type", spinner.getSelectedItem().toString());
                    map_intent.putExtra("map_id", id);

                    activity.startActivity(map_intent);
                    activity.finish();
                }
            });

            ImageFilterButton goBack = view.findViewById(R.id.goBackButton);
            goBack.setOnClickListener(v -> {
                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
            });
        }
    }
}

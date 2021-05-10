package com.example.mappis;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappis.CardMaps.CardAdapter;
import com.example.mappis.CardMaps.CardItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CardAdapter adapter;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        if(activity != null) {
            setRecyclerView(activity);
            floatingActionButton = view.findViewById(R.id.fab_add);

            floatingActionButton.setOnClickListener(v -> {
                Utilities.insertFragment((AppCompatActivity) getActivity(), new CreateMapFragment(),
                        "Create map fragment");
            });
        }
    }

    private void setRecyclerView(final Activity activity) {
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        List<CardItem> list = new ArrayList<>();

        list.add(new CardItem("uauauua", "bububu"));
        list.add(new CardItem("asdsadsa", "bububu"));

        list.add(new CardItem("uauauua", "bubuubb"));
        list.add(new CardItem("asdsadsa", "bubububu"));

        adapter = new CardAdapter(activity, list);
        
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.setAdapter(adapter);
    }

}

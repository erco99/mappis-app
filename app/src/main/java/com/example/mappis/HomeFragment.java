package com.example.mappis;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappis.CardMaps.CardAdapter;
import com.example.mappis.CardMaps.CardItem;
import com.example.mappis.CardMaps.CardViewModel;
import com.example.mappis.CardMaps.OnItemListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemListener {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CardAdapter adapter;
    private Activity activity;
    private CardViewModel cardViewModel;
    private ImageFilterButton settingsButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        if(activity != null) {
            setRecyclerView(activity);

            cardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CardViewModel.class);

            System.out.println("home" + cardViewModel.getCardItems().getValue());
            cardViewModel.getCardItems().observe((LifecycleOwner) activity, new Observer<List<CardItem>>() {
                @Override
                public void onChanged(List<CardItem> cardItemList) {
                    adapter.setData(cardItemList);
                }
            });

            floatingActionButton = view.findViewById(R.id.fab_add);
            floatingActionButton.setOnClickListener(v -> {
                Utilities.insertFragment((AppCompatActivity) getActivity(), new CreateMapFragment(),
                        "Create map fragment");
            });

            settingsButton = view.findViewById(R.id.SettingsImageFilterButton);
            settingsButton.setOnClickListener(v -> {
                Utilities.insertFragment((AppCompatActivity) getActivity(), new SettingsFragment(),
                        SettingsFragment.class.getSimpleName());
            });

        }
    }

    private void setRecyclerView(final Activity activity) {
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final OnItemListener listener = this;

        adapter = new CardAdapter(activity, listener);
        
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {

            cardViewModel.select(cardViewModel.getCardItem(position));

            Utilities.insertFragment(appCompatActivity, new MapDetailsFragment(),
                    MapDetailsFragment.class.getSimpleName());
        }
    }
}

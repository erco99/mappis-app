package com.example.mappis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.mappis.CardMaps.AddCardViewModel;

public class MainActivity extends AppCompatActivity {

    AddCardViewModel addCardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            Utilities.insertFragment(this, new HomeFragment(), "HomeFragment");


        addCardViewModel = new ViewModelProvider(this).get(AddCardViewModel.class);
    }
}
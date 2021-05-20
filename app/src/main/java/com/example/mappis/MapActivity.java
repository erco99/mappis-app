package com.example.mappis;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class  MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_track);

        if (savedInstanceState == null){
            Utilities.insertFragment(this, new MapFragment(),
                    MapFragment.class.getSimpleName());
        }
    }
}

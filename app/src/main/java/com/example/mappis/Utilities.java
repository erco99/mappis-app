package com.example.mappis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.osmdroid.bonuspack.kml.KmlDocument;

public class Utilities {

    static final int[] pencil_type_icons = {R.drawable.nature};
    static final int[] drag_icons = {R.drawable.waves, R.drawable.lake};
    static final int[] text_icons = {};

    static KmlDocument kmlDocument = new KmlDocument();


    static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container_view, fragment, tag);

        if (!(fragment instanceof HomeFragment) && !(fragment instanceof MapFragment)) {
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }

}

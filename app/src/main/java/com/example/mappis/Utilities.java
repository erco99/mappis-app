package com.example.mappis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.osmdroid.bonuspack.kml.KmlDocument;

public class Utilities {

    static final String MAP_NAME_STRING = "/mappis_map_";

    static int number = 0;

    static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    static final int REQUEST_READ_EXTERNAL_STORAGE = 3;

    static final int[] drag_icons = {R.drawable.map_icons_standard_antenna,
            R.drawable.map_icons_standard_danger, R.drawable.map_icons_standard_fountain,
            R.drawable.map_icons_standard_house, R.drawable.map_icons_standard_house2,
            R.drawable.map_icons_standard_lake, R.drawable.map_icons_standard_lake2,
            R.drawable.map_icons_standard_lakeandtrees, R.drawable.map_icons_standard_river,
            R.drawable.map_icons_standard_tree, R.drawable.map_icons_standard_tree2,
            R.drawable.map_icons_standard_tree3, R.drawable.map_icons_standard_tree4,
            R.drawable.map_icons_standard_tree5, R.drawable.map_icons_standard_trees,
            R.drawable.map_icons_standard_trees2, R.drawable.map_icons_standard_warning
    };

    static final int[] text_icons = {R.drawable.map_icons_text_alert,
            R.drawable.map_icons_text_pin, R.drawable.map_icons_text_pin1,
            R.drawable.map_icons_text_pin2, R.drawable.map_icons_text_placeholder,
            R.drawable.map_icons_text_placeholder1, R.drawable.map_icons_text_placeholder2,
            R.drawable.map_icons_text_placeholder3, R.drawable.map_icons_text_placeholder4};

    static KmlDocument kmlDocumentMarkers = new KmlDocument();
    static KmlDocument kmlDocumentTrack = new KmlDocument();


    static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container_view, fragment, tag);

        if (!(fragment instanceof HomeFragment) && !(fragment instanceof MapFragment)) {
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }

}

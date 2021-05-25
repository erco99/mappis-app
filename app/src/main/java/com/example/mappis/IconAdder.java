package com.example.mappis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.EditText;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class IconAdder {

    private final Location currentLocation;
    private final MapView map;
    private final Activity activity;

    public IconAdder(Location currentLocation, MapView map, Activity activity) {
        this.currentLocation = currentLocation;
        this.map = map;
        this.activity = activity;
    }

    public void insertDragIcon(int image) {
        Marker marker = new Marker(map);
        GeoPoint point = new GeoPoint(currentLocation);

        marker.setPosition(point);

        marker.setInfoWindow(null);

        map.getOverlays().add(marker);
        map.invalidate();

        applyDraggableListener(marker);

        marker.setIcon(AppCompatResources.getDrawable(activity, image));
    }

    public void insertTextIcon(int image) {
        Marker marker = new Marker(map);
        GeoPoint point = new GeoPoint(currentLocation);

        marker.setPosition(point);


        map.getOverlays().add(marker);
        map.invalidate();

        insertTextMarker(marker);
        applyDraggableListener(marker);
        marker.setIcon(AppCompatResources.getDrawable(activity, image));
    }

    private void applyDraggableListener(Marker poiMarker) {
        poiMarker.setDraggable( true);
        poiMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

                @Override
                public void onMarkerDragEnd(Marker marker) {
                GeoPoint geopoint = marker.getPosition();
                //poiMarker.setDraggable(false);

                Drawable defaultMarker = AppCompatResources.getDrawable(activity, R.drawable.map_icons_woodland);
                Bitmap bitmap = ((BitmapDrawable)defaultMarker).getBitmap();
                Style style = new Style(bitmap, 0x901010AA, 3.0f, 0x20AA1010);

                Utilities.kmlDocument.putStyle("prova-style", style);

                Utilities.kmlDocument.mKmlRoot.addOverlay(marker, Utilities.kmlDocument);
                Utilities.kmlDocument.mKmlRoot.setExtendedData("category", "prova-style");
                System.out.println(Utilities.kmlDocument.mKmlRoot.getExtendedData("category"));
            }

            @Override
            public void onMarkerDrag(Marker marker) {}
        });
    }

    public void insertTextMarker(Marker poiMarker) {
        final EditText input = new EditText(activity);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        alert.setTitle("Insert text");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                poiMarker.setTitle(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                return;
            }
        });
        Dialog dialog = alert.create();
        dialog.show();

    }
}

package com.example.mappis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Location;
import android.widget.EditText;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

public class IconAdder {

    private final Location currentLocation;
    private final MapView map;
    private final Activity activity;
    protected FolderOverlay mKmlOverlay = null;
    private KmlDocument mKmlDocument;

    public IconAdder(Location currentLocation, MapView map, Activity activity) {
        this.currentLocation = currentLocation;
        this.map = map;
        this.activity = activity;
    }

    public void insertDragIcon(int image) {
        Marker marker = new Marker(map);
        GeoPoint point = new GeoPoint(currentLocation);
        mKmlDocument = new KmlDocument();

        marker.setPosition(point);
        marker.setInfoWindow(null);
        marker.setIcon(AppCompatResources.getDrawable(activity, image));
        marker.setTitle("- drag_" + image + " -");

        map.getOverlays().add(marker);
        map.invalidate();

        applyDraggableListener(marker, image);
    }

    public void insertTextIcon(int image) {
        Marker marker = new Marker(map);
        GeoPoint point = new GeoPoint(currentLocation);
        mKmlDocument = new KmlDocument();

        marker.setPosition(point);
        marker.setIcon(AppCompatResources.getDrawable(activity, image));

        map.getOverlays().add(marker);
        map.invalidate();
        insertTextMarker(marker, image);

        applyDraggableListener(marker, image);
    }

    private void applyDraggableListener(Marker poiMarker, int image) {
        poiMarker.setDraggable( true);
        poiMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}
                @Override
                public void onMarkerDragEnd(Marker marker) {
                    /*GeoPoint geopoint = marker.getPosition();
                    poiMarker.setDraggable(false);
                    */

                    KmlPlacemark kmlPlacemark = new KmlPlacemark(marker);

                    Utilities.kmlDocumentMarkers.mKmlRoot.add(kmlPlacemark);
                }

            @Override
            public void onMarkerDrag(Marker marker) {}
        });
    }

    public void insertTextMarker(Marker poiMarker, int image_id) {
        final EditText input = new EditText(activity);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        alert.setTitle("Insert text");
        alert.setView(input);

        alert.setPositiveButton("Ok", (dialog, whichButton) -> poiMarker
                .setTitle("- text_" + image_id + " -\n" + input.getText().toString()));

        alert.setNegativeButton("Cancel", (dialog, which) -> poiMarker
                .setTitle("- text_" + image_id + " -\n"));

        alert.setOnCancelListener(dialog -> poiMarker
                .setTitle("- text_" + image_id + " -\n"));
        Dialog dialog = alert.create();
        dialog.show();

    }

}

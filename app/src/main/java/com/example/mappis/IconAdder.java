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
import androidx.core.content.res.ResourcesCompat;

import org.osmdroid.bonuspack.kml.IconStyle;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.bonuspack.kml.LineStyle;
import org.osmdroid.bonuspack.kml.Style;
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

        FolderOverlay poiMarkers = new FolderOverlay();
        map.getOverlays().add(poiMarkers);

        marker.setPosition(point);
        marker.setInfoWindow(null);
        marker.setIcon(AppCompatResources.getDrawable(activity, image));

        map.getOverlays().add(marker);
        map.invalidate();

        applyDraggableListener(marker, image, mKmlDocument);
    }

    public void insertTextIcon(int image) {
        Marker marker = new Marker(map);
        GeoPoint point = new GeoPoint(currentLocation);
        mKmlDocument = new KmlDocument();

        marker.setPosition(point);
        marker.setIcon(AppCompatResources.getDrawable(activity, image));

        map.getOverlays().add(marker);
        map.invalidate();

        insertTextMarker(marker);
        applyDraggableListener(marker, image, mKmlDocument);
    }

    private void applyDraggableListener(Marker poiMarker, int image, KmlDocument mKmlDocument) {
        poiMarker.setDraggable( true);
        poiMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    /*GeoPoint geopoint = marker.getPosition();
                    poiMarker.setDraggable(false);
                    */


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

        alert.setPositiveButton("Ok", (dialog, whichButton) -> poiMarker.setTitle(input.getText().toString()));

        alert.setNegativeButton("Cancel", (dialog, which) -> {});

        Dialog dialog = alert.create();
        dialog.show();

    }

    private Style buildStyle(){
        Drawable defaultMarker = AppCompatResources.getDrawable(activity, R.drawable.person);
        Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
        return new Style(defaultBitmap, 0x901010AA,
                3.0f, 0x20AA1010);
    }

}

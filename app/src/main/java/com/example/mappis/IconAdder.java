package com.example.mappis;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;

import static com.example.mappis.TrackRecorder.mKmlDocument;

public class IconAdder {

    Location currentLocation = null;
    MapView map;
    GeoPoint point;
    Marker marker;
    Activity activity;

    public IconAdder(Location currentLocation, MapView map, Activity activity) {
        this.currentLocation = currentLocation;
        this.map = map;
        this.activity = activity;
    }

    public void insertIcon(int image) throws IOException {
        marker = new Marker(map);
        point = new GeoPoint(currentLocation);

        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        map.invalidate();

        applyDraggableListener(marker);

        marker.setIcon(activity.getDrawable(image));
    }

    public void applyDraggableListener(Marker poiMarker) {
        poiMarker.setDraggable( true);
        poiMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                GeoPoint geopoint = marker.getPosition();
                //poiMarker.setDraggable(false);

                Drawable defaultMarker = activity.getDrawable(R.drawable.woodland);
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

}

package com.example.mappis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;

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

    public void insertIcon(int image) {
        Marker marker = new Marker(map);
        GeoPoint point = new GeoPoint(currentLocation);

        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_TOP);

        marker.setInfoWindow(null);

        map.getOverlays().add(marker);
        map.invalidate();

        applyDraggableListener(marker);

        marker.setIcon(AppCompatResources.getDrawable(activity, image));
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

                Drawable defaultMarker = AppCompatResources.getDrawable(activity, R.drawable.woodland);
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

package com.example.mappis;

import android.app.Activity;
import android.location.Location;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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

    public void insertIcon(int image){
        marker = new Marker(map);
        point = new GeoPoint(currentLocation);

        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        map.invalidate();

        applyDraggableListener(marker);

        marker.setIcon(activity.getDrawable(image));
    }

    public static void applyDraggableListener(Marker poiMarker) {
        poiMarker.setDraggable(true);
        poiMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                GeoPoint geopoint = marker.getPosition();
                //poiMarker.setDraggable(false);
            }

            @Override
            public void onMarkerDrag(Marker marker) {}
        });
    }

}

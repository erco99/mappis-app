package com.example.mappis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

public class MyKmlStyler implements KmlFeature.Styler {

    Style mDefaultStyle;
    KmlDocument mKmlDocument;
    MapView map;
    Activity activity;


    public MyKmlStyler(){
    }

    public MyKmlStyler(KmlDocument kmlDocument, MapView mMapView, Activity activity) {
        mKmlDocument = kmlDocument;
        map = mMapView;
        this.activity = activity;
    }

    @Override
    public void onFeature(Overlay overlay, KmlFeature kmlFeature) {
            System.out.println("sdsds" + kmlFeature.mExtendedData   );

    }

    @Override
    public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {

        String data = marker.getTitle();
        int id = getImageId(data);
        String type = getMarkerType(data);

        if(activity != null) {
            Drawable defaultMarker = AppCompatResources.getDrawable(activity, id);
            Bitmap defaultBitmap = toBitmap(defaultMarker);
            Style style = new Style(defaultBitmap, 0x901010AA, 3.0f, 0x2010AA10);

            kmlPoint.applyDefaultStyling(marker, style, kmlPlacemark, mKmlDocument, map);

            if(type.equals("drag")) {
                marker.setInfoWindow(null);
            }
        }



    }

    @Override
    public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {

    }

    @Override
    public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {

    }

    @Override
    public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {

    }

    private Bitmap toBitmap(Drawable drawable) {
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }


    private int getImageId(String str) {
        String result = str.split("-")[1];
        String id = result.replaceAll("[^0-9]", "");
        return Integer.parseInt(id);
    }

    private String getMarkerType(String str) {
        return str.replaceAll("[^a-zA-Z ]", "");
    }

}

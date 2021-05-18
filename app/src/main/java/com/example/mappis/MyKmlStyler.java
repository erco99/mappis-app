package com.example.mappis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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

    }

    @Override
    public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {

        Drawable defaultMarker = activity.getDrawable(R.drawable.forest);
        Bitmap defaultBitmap = ((BitmapDrawable)defaultMarker).getBitmap();
        Style prova = new Style(defaultBitmap, 0x901010AA, 3.0f, 0x20AA1010);

        System.out.println(kmlPlacemark.getExtendedData("category"));

        if ("panda_area".equals(kmlPlacemark.getExtendedData("category"))) {
            kmlPlacemark.mStyle = "prova-style";
            System.out.println(kmlPlacemark.mStyle);
        }
        kmlPoint.applyDefaultStyling(marker, prova, kmlPlacemark, mKmlDocument, map);



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
}

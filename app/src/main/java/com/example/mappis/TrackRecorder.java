package com.example.mappis;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.bonuspack.kml.LineStyle;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.util.Date;

public class TrackRecorder {

    protected MapView map;
    protected View view;

    protected FolderOverlay mKmlOverlay = null;
    public static KmlDocument mKmlDocument = new KmlDocument();

    public TrackRecorder(MapView map, View view) {
        this.map = map;
        this.view = view;
    }


    public void recordCurrentLocationInTrack(String trackId, String trackName, GeoPoint currentLocation, boolean newTrack) {
        //Find the KML track in the current KML structure - and create it if necessary:
        KmlTrack t;
        KmlFeature f = mKmlDocument.mKmlRoot.findFeatureId(trackId, false);
        if (f == null && newTrack)
            t = createTrack(trackId, trackName);
        else if (!(f instanceof KmlPlacemark))
            //id already defined but is not a PlaceMark
            return;
        else {
            KmlPlacemark p = (KmlPlacemark)f;
            if (!(p.mGeometry instanceof KmlTrack))
                //id already defined but is not a Track
                return;
            else
                t = (KmlTrack) p.mGeometry;
        }
        //TODO check if current location is really different from last point of the track
        //record in the track the current location at current time:
        t.add(currentLocation, new Date());
        //refresh KML:
        updateUIWithKml();
    }

    private KmlTrack createTrack(String id, String name) {
        KmlTrack t = new KmlTrack();
        KmlPlacemark p = new KmlPlacemark();
        p.mId = id;
        p.mName = name;
        p.mGeometry = t;
        mKmlDocument.mKmlRoot.add(p);
        //set a color to this track by creating a style:
        Style s = new Style();
        int color;
        try {
            color = Integer.parseInt(id);
            color = color % TrackColor.length;
            color = TrackColor[color];
        } catch (NumberFormatException e) {
            color = Color.RED-0x20000000;
        }

        color = Color.BLUE-0x300000;

        s.mLineStyle = new LineStyle(color, 56.0f);
        String styleId = mKmlDocument.addStyle(s);
        p.mStyle = styleId;
        return t;
    }


    static int[] TrackColor = {
            Color.CYAN-0x20000000, Color.BLUE-0x20000000, Color.MAGENTA-0x20000000, Color.RED-0x20000000, Color.YELLOW-0x20000000
    };

    private void updateUIWithKml(){
        if (mKmlOverlay != null){
            mKmlOverlay.closeAllInfoWindows();
            map.getOverlays().remove(mKmlOverlay);
        }
        mKmlOverlay = (FolderOverlay)mKmlDocument.mKmlRoot.buildOverlay(map, buildDefaultStyle(), null, mKmlDocument);
        map.getOverlays().add(mKmlOverlay);
        map.invalidate();
    }

    private Style buildDefaultStyle(){
        Drawable defaultKmlMarker = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_settings_24, null);
        Bitmap bitmap = Bitmap.createBitmap(defaultKmlMarker.getIntrinsicWidth(), defaultKmlMarker.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        return new Style(bitmap, 0x901010AA, 3.0f, 0x20AA1010);
    }

}

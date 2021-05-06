package com.example.mappis;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapFragment extends Fragment implements LocationListener {

    private int PLAY = 1;
    private int PAUSE = 0;
    private int TRACK_NUMBER = 0;
    private boolean newTrack;

    private int TRACK_STATUS = PAUSE;

    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    protected ImageButton btCenterMap;
    private Location currentLocation = null;
    private LocationManager lm;

    private TrackRecorder trackRecorder;

    protected ImageButton pencilButton;
    protected ImageButton iconButton;
    protected ImageButton textButton;

    protected  ImageButton trackButton;

    private MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_track, null);
        mMapView = v.findViewById(R.id.map);

        //Set user agent for osmdroid
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState)

        final Context context = this.getActivity();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        this.mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context),
                mMapView);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),
                mMapView);

        mScaleBarOverlay = new ScaleBarOverlay(mMapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        mRotationGestureOverlay = new RotationGestureOverlay(mMapView);
        mRotationGestureOverlay.setEnabled(true);

        mMapView.getController().setZoom(15.0);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mMapView.setTilesScaledToDpi(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setFlingEnabled(true);
        mMapView.getOverlays().add(this.mLocationOverlay);
        mMapView.getOverlays().add(this.mCompassOverlay);
        mMapView.getOverlays().add(this.mScaleBarOverlay);
        mMapView.setTileSource(TileSourceFactory.OPEN_SEAMAP);

        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        mLocationOverlay.setOptionsMenuEnabled(true);
        mCompassOverlay.enableCompass();

        trackRecorder = new TrackRecorder(mMapView, view);

        btCenterMap = view.findViewById(R.id.ic_center_map);
        btCenterMap.setOnClickListener(v -> {
            if (currentLocation != null) {
                GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMapView.getController().animateTo(myPosition);
            }
        });

        //menu buttons
        pencilButton = view.findViewById(R.id.pencil_button);
        pencilButton.setOnClickListener(v -> {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            if (appCompatActivity != null){
                IconShowFragment dialog = new IconShowFragment(Utilities.pencil_type_icons,
                        this.currentLocation, this.mMapView);
                dialog.show(getParentFragmentManager(), "dialog");
            }
        });

        iconButton = view.findViewById(R.id.icon_button);
        iconButton.setOnClickListener(v -> {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            if (appCompatActivity != null){
                IconShowFragment dialog = new IconShowFragment(Utilities.drag_icons,
                        this.currentLocation, this.mMapView);
                dialog.show(getParentFragmentManager(), "dialog");
            }
        });

        textButton = view.findViewById(R.id.text_button);
        textButton.setOnClickListener(v -> {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            if (appCompatActivity != null){
                IconShowFragment dialog = new IconShowFragment(Utilities.text_icons,
                        this.currentLocation, this.mMapView);
                dialog.show(getParentFragmentManager(), "dialog");
            }
        });

        //tracking button
        trackButton = view.findViewById(R.id.track_button);
        trackButton.setOnClickListener(v -> {
            if(this.TRACK_STATUS == this.PLAY){
                trackButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                this.TRACK_STATUS = this.PAUSE;
            } else if(this.TRACK_STATUS == this.PAUSE) {
                trackButton.setImageResource(R.drawable.ic_baseline_pause_24);
                this.TRACK_STATUS = this.PLAY;
                newTrack = true;
                TRACK_NUMBER++;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            lm.removeUpdates(this);
        } catch (Exception ex){
        }
        mCompassOverlay.disableCompass();
        mLocationOverlay.disableFollowLocation();
        mLocationOverlay.disableMyLocation();
        mScaleBarOverlay.enableScaleBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            //this fails on AVD 19s, even with the appcompat check, says no provided named gps is available
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        } catch (Exception ex) {
        }

        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0l, 0f, this);
        } catch (Exception ex) {
        }

        mLocationOverlay.enableFollowLocation();
        mLocationOverlay.enableMyLocation();
        mScaleBarOverlay.disableScaleBar();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.currentLocation = location;
        if (this.TRACK_STATUS == this.PLAY && !newTrack) {
            trackRecorder.recordCurrentLocationInTrack("my_track" + TRACK_NUMBER,
                    "My Track" + TRACK_NUMBER, new GeoPoint(this.currentLocation), false);
        } else if(this.TRACK_STATUS == this.PLAY && newTrack) {
            trackRecorder.recordCurrentLocationInTrack("my_track" + TRACK_NUMBER,
                    "My Track" + TRACK_NUMBER, new GeoPoint(this.currentLocation), true);
            newTrack = false;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lm = null;
        currentLocation = null;
        mLocationOverlay = null;
        mCompassOverlay = null;
        mScaleBarOverlay = null;
        mRotationGestureOverlay = null;
        btCenterMap = null;
    }
}

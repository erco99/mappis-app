package com.example.mappis;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.IOException;

public class MapFragment extends Fragment implements LocationListener {

    private Activity activity;
    private final int PLAY = 1;
    private final int PAUSE = 0;
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
    protected ImageButton saveButton;
    protected ImageButton trackButton;

    private MapView mMapView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();

        if (activity != null) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Data will be lost").setPositiveButton(
                            "OK", (dialog, which) -> {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                    ).setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        } else {
            System.out.println("Activity is null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_track, container, false);
        mMapView = v.findViewById(R.id.map);

        //Set user agent for osmdroid
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState)

        this.activity = getActivity();

        if (activity != null) {
            final Context context = this.getActivity();
            final DisplayMetrics dm = activity.getResources().getDisplayMetrics();

            this.mCompassOverlay = new CompassOverlay(activity, new InternalCompassOrientationProvider(activity),
                    mMapView);
            this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),
                    mMapView);

            mScaleBarOverlay = new ScaleBarOverlay(mMapView);
            mScaleBarOverlay.setCentred(true);
            mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

            mRotationGestureOverlay = new RotationGestureOverlay(mMapView);
            mRotationGestureOverlay.setEnabled(true);

            mMapView.getController().setZoom(20.0);
            mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
            mMapView.setTilesScaledToDpi(true);
            mMapView.setMultiTouchControls(true);
            mMapView.setFlingEnabled(true);
            mMapView.getOverlays().add(this.mLocationOverlay);
            mMapView.getOverlays().add(this.mCompassOverlay);
            mMapView.getOverlays().add(this.mScaleBarOverlay);

            //map type loading
            if (getActivity().getIntent().getExtras() != null) {
                String value = getActivity().getIntent().getExtras().getString("map_type");
                switch (value) {
                    case "Default map":
                        mMapView.setTileSource(TileSourceFactory.MAPNIK);
                        break;
                    case "Empty map":
                        mMapView.setTileSource(TileSourceFactory.OPEN_SEAMAP);
                        break;
                    case "Hike map":
                        mMapView.setTileSource(TileSourceFactory.HIKEBIKEMAP);
                        break;
                    case "Open topo map":
                        mMapView.setTileSource(TileSourceFactory.OpenTopo);
                        break;
                    case "Wikimedia map":
                        mMapView.setTileSource(TileSourceFactory.WIKIMEDIA);
                        break;
                }
            } else {
                mMapView.setTileSource(TileSourceFactory.MAPNIK);
            }

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
                if (appCompatActivity != null) {
                    IconShowFragment dialog = new IconShowFragment(Utilities.pencil_type_icons,
                            this.currentLocation, this.mMapView);
                    dialog.show(getParentFragmentManager(), "dialog");
                }
            });

            iconButton = view.findViewById(R.id.icon_button);
            iconButton.setOnClickListener(v -> {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                if (appCompatActivity != null) {
                    IconShowFragment dialog = new IconShowFragment(Utilities.drag_icons,
                            this.currentLocation, this.mMapView);
                    dialog.show(getParentFragmentManager(), "dialog");
                }
            });

            textButton = view.findViewById(R.id.text_button);
            textButton.setOnClickListener(v -> {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                if (appCompatActivity != null) {
                    IconShowFragment dialog = new IconShowFragment(Utilities.text_icons,
                            this.currentLocation, this.mMapView);
                    dialog.show(getParentFragmentManager(), "dialog");
                }
            });

            saveButton = view.findViewById(R.id.save_button);
            saveButton.setOnClickListener(v -> {

            });

            //tracking button
            trackButton = view.findViewById(R.id.track_button);
            trackButton.setOnClickListener(v -> {
                if (this.TRACK_STATUS == this.PLAY) {
                    trackButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    this.TRACK_STATUS = this.PAUSE;
                } else if (this.TRACK_STATUS == this.PAUSE) {
                    trackButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    this.TRACK_STATUS = this.PLAY;
                    newTrack = true;
                    TRACK_NUMBER++;
                }
            });


            saveButton = view.findViewById(R.id.save_button);
            saveButton.setOnClickListener(v -> {
                System.out.println("click");
                File localFile = new File(getActivity().getExternalFilesDir(null) + "/prova.kml");
                try {
                    localFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(localFile);
                if (localFile.exists()) {
                    Utilities.kmlDocument.saveAsKML(localFile);
                    System.out.println("save");
                }
            });


            //load the map
            Drawable defaultMarker = AppCompatResources.getDrawable(activity, R.drawable.forest);
            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
            Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3.0f, 0x20AA1010);

            MyKmlStyler styler = new MyKmlStyler(Utilities.kmlDocument, mMapView, getActivity());

            KmlDocument kmlDocument = new KmlDocument();

            File f = new File("/storage/emulated/0/Android/data/com.example.mappis/files/prova.kml");

            if (f.exists()) {
                kmlDocument.parseKMLFile(f);
                FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mMapView, null, styler, kmlDocument);
                mMapView.getOverlays().add(kmlOverlay);
                mMapView.invalidate();
            }
        } else {
            System.out.println("Activity is null");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            lm.removeUpdates(this);
        } catch (Exception ex) {
            System.out.println("Updates error");
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
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0L, 0f, this);
            } else if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showExplanation("Location permission needed", "You need to give " +
                                    "permission to access to your location to use Mappis correctly",
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Utilities.REQUEST_ACCESS_FINE_LOCATION);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        Utilities.REQUEST_ACCESS_FINE_LOCATION);
            }
        } catch (Exception ex) {
            System.out.println("Permission error");
        }

        try {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0L, 0f, this);
            } else if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation("Storage access permission needed", "You need to give " +
                                "permission to access to your storage to save the maps",
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Utilities.REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Utilities.REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } catch (Exception ex) {
            System.out.println("Permission error");
        }



        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this);
        } catch (Exception ex) {
            System.out.println("Permission error");
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

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        (dialog, id) -> requestPermission(permission, permissionRequestCode));
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{permissionName}, permissionRequestCode);
    }
}

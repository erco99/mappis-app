package com.example.mappis;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MapFragment extends Fragment implements LocationListener {

    private boolean CREATE = false;
    private int CURRENT_MAP_ID;
    int counter = 0;

    private Activity activity;
    private final int PLAY = 1;
    private final int PAUSE = 0;
    private int TRACK_NUMBER = 0;
    private boolean newTrack;

    private int TRACK_STATUS = PAUSE;

    private MyLocationNewOverlay mLocationOverlay;
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

    private int map_id_save;

    private MapView mMapView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();

        if (activity != null) {
            map_id_save = activity.getIntent().getExtras().getInt("map_id");
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Are you sure you want to go back to the home?").setPositiveButton(
                            "YES", (dialog, which) -> {
                                Intent intent = new Intent(getActivity(), MainActivity.class);

                                if(TRACK_STATUS == PLAY) {
                                    TRACK_STATUS = PAUSE;
                                }

                                saveMap();

                                Utilities.kmlDocumentMarkers = new KmlDocument();
                                Utilities.kmlDocumentTrack = new KmlDocument();

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
            mMapView.getOverlays().add(this.mScaleBarOverlay);
            mMapView.getOverlays().add(mRotationGestureOverlay);

            mLocationOverlay.enableMyLocation();

            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 12.7f,
                    activity.getResources().getDisplayMetrics());

            mScaleBarOverlay.setAlignBottom(true);
            mScaleBarOverlay.setAlignRight(false);
            mScaleBarOverlay.setScaleBarOffset((int) (context.getResources()
                    .getDisplayMetrics().widthPixels / 2 - px), 15);

            mLocationOverlay.enableFollowLocation();
            mLocationOverlay.setOptionsMenuEnabled(true);

            //map type loading
            String value = getActivity().getIntent().getExtras().getString("map_type");
            if (value != null) {
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
            trackRecorder = new TrackRecorder(mMapView, view);

            btCenterMap = view.findViewById(R.id.ic_center_map);
            btCenterMap.setOnClickListener(v -> {
                if (currentLocation != null) {
                    GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(),
                            currentLocation.getLongitude());
                    mMapView.getController().animateTo(myPosition);
                }
            });

            //menu buttons
            pencilButton = view.findViewById(R.id.pencil_button);
            pencilButton.setOnClickListener(v -> {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                if (appCompatActivity != null) {
                    AmbilWarnaDialog dialog = new AmbilWarnaDialog(activity, TrackRecorder.COLOR,
                            new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {
                            //do nothing
                        }
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            TrackRecorder.COLOR = color;
                        }
                    });
                    dialog.show();
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

            //save map when save button is clicked
            saveButton = view.findViewById(R.id.save_button);
            saveButton.setOnClickListener(v -> {
                saveMap();
                Toast.makeText(activity, "Map updated",
                        Toast.LENGTH_SHORT).show();
            });

            //load the map (markers and path)
            int map_to_be_loaded = activity.getIntent().getExtras().getInt("map_to_be_loaded");
            boolean create = activity.getIntent().getExtras().getBoolean("create");
            CREATE = create;

            Drawable defaultMarker = AppCompatResources.getDrawable(activity, R.drawable.person);
            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
            Style defaultStyle = new Style(defaultBitmap, 0x901010AA,
                    3.0f, 0x20AA1010);

            MyKmlStyler styler = new MyKmlStyler(Utilities.kmlDocumentMarkers, mMapView, getActivity());

            KmlDocument kmlDocumentMarkers = new KmlDocument();
            KmlDocument kmlDocumentTrack = new KmlDocument();

            File f = null;
            File f2 = null;
            if(CREATE) {
                CREATE = false;
                CURRENT_MAP_ID = map_id_save;
            } else {
                f = new File(activity.getExternalFilesDir(null) +
                        Utilities.MAP_NAME_STRING + "markers_" + map_to_be_loaded);
                f2 = new File(activity.getExternalFilesDir(null) +
                        Utilities.MAP_NAME_STRING + "track_" + map_to_be_loaded);
                if (f.exists()) {
                    kmlDocumentMarkers.parseKMLFile(f);
                    FolderOverlay kmlOverlayMarkers = (FolderOverlay) kmlDocumentMarkers.mKmlRoot
                            .buildOverlay(mMapView, null, styler, kmlDocumentMarkers);

                    kmlDocumentTrack.parseKMLFile(f2);
                    FolderOverlay kmlOverlayTrack = (FolderOverlay) kmlDocumentTrack.mKmlRoot
                            .buildOverlay(mMapView, null, null, kmlDocumentTrack);
                    Utilities.kmlDocumentMarkers.mKmlRoot.addOverlay(kmlOverlayMarkers, Utilities.kmlDocumentMarkers);
                    Utilities.kmlDocumentTrack.mKmlRoot.addOverlay(kmlOverlayTrack, Utilities.kmlDocumentTrack);
                    mMapView.getOverlays().add(kmlOverlayMarkers);
                    mMapView.getOverlays().add(kmlOverlayTrack);
                    mMapView.invalidate();
                    System.out.println("LOADING " + f);
                    CURRENT_MAP_ID = map_to_be_loaded;
                }
            }

            System.out.println("NUMBER" + Utilities.number);
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
        mLocationOverlay.disableFollowLocation();
        mLocationOverlay.disableMyLocation();
        mScaleBarOverlay.disableScaleBar();
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
        mScaleBarOverlay.enableScaleBar();
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
        mScaleBarOverlay = null;
        mRotationGestureOverlay = null;
        btCenterMap = null;
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        (dialog, id) -> requestPermission(permission, permissionRequestCode));
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permissionName}, permissionRequestCode);
    }

    private void saveMap() {
        File markersFile = new File(activity.getExternalFilesDir(null) +
                Utilities.MAP_NAME_STRING + "markers_" + CURRENT_MAP_ID);
        File trackFile = new File(activity.getExternalFilesDir(null) +
                Utilities.MAP_NAME_STRING + "track_" + CURRENT_MAP_ID);

        try {
            markersFile.createNewFile();
            trackFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(trackFile);
        if (markersFile.exists() && trackFile.exists()) {
            Utilities.kmlDocumentTrack.saveAsKML(trackFile);
            Utilities.kmlDocumentMarkers.saveAsKML(markersFile);
        }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            counter++;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Location is disabled");
            builder.setMessage("Please enable your location in your device settings to use the app");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            if(counter == 1) {
                alertDialog.show();
            }
        }
    }

}

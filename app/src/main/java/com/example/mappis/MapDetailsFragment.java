package com.example.mappis;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappis.CardMaps.AddCardViewModel;
import com.example.mappis.CardMaps.CardViewModel;
import com.example.mappis.CardMaps.Comments.Comment;
import com.example.mappis.CardMaps.Comments.CommentAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MapDetailsFragment extends Fragment {

    private static final SimpleDateFormat timestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY);
    private Button addButton;
    private TextInputEditText commentBox;
    private RecyclerView commentList;
    private ImageView mapImageView;
    private Button deleteMapButton;
    private TextView mapNameStat;
    private TextView mapTypeStat;
    private Button changeImageButton;
    String value = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        if (activity != null) {
            commentBox = view.findViewById(R.id.commentTextInputEditText);
            addButton = view.findViewById(R.id.add_comment_button);
            commentList = view.findViewById(R.id.commentListView);
            mapImageView = view.findViewById(R.id.details_map);
            deleteMapButton = view.findViewById(R.id.delete_map_button);
            mapNameStat = view.findViewById(R.id.mapNameStat);
            mapTypeStat = view.findViewById(R.id.mapTypeStat);
            changeImageButton = activity.findViewById(R.id.change_image_button);

            AddCardViewModel addCardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddCardViewModel.class);
            CardViewModel cardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CardViewModel.class);


            ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            // There are no request codes
                            if(result.getData() != null) {
                                Uri uri = result.getData().getData();
                                value = uri.toString();

                                setImage(activity, value);

                                mapImageView.setImageBitmap(uriToBitmap(uri, activity));
                            }
                        }
                    });

            changeImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Utilities.REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        mLauncher.launch(intent);
                    }
                }
            });

            cardViewModel.getSelected().observe(getViewLifecycleOwner(), cardItem -> {
                mapNameStat.setText(cardItem.getMapName());
                mapTypeStat.setText(cardItem.getMapType());

                if(cardItem.getMapImageUri() != null) {
                    mapImageView.setImageBitmap(BitmapFactory.decodeFile(cardItem.getMapImageUri()));
                }

                mapImageView.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, MapActivity.class);
                    intent.putExtra("map_to_be_loaded", cardItem.getItemId());
                    intent.putExtra("map_type", cardItem.getMapType());
                    activity.startActivity(intent);
                    activity.finish();
                });

                cardViewModel.getComments(cardItem.getItemId()).observe((LifecycleOwner) activity, comments -> {
                    ArrayList<Comment> list = new ArrayList<>(comments);
                    CommentAdapter adapter = new CommentAdapter(list);
                    commentList.setLayoutManager(new LinearLayoutManager(activity));
                    commentList.setHasFixedSize(true);
                    commentList.setAdapter(adapter);
                });

                addButton.setOnClickListener(v -> {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    addCardViewModel.addComment(new Comment(commentBox.getText().toString(),
                            cardItem.getItemId(),
                            timestamp.format(time)));
                    commentBox.getText().clear();
                    Toast.makeText(activity, "Comment added", Toast.LENGTH_SHORT).show();
                });

                deleteMapButton.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Are you sure you want to delete this map?").setPositiveButton(
                            "YES", (dialog, which) -> {
                                cardViewModel.deleteCardItem(cardItem.getItemId());
                                cardViewModel.deleteComments(cardItem.getItemId());

                                File file = new File(activity.getExternalFilesDir(null) +
                                        Utilities.MAP_NAME_STRING + "markers_" + cardItem.getItemId());
                                file.delete();
                                File file2 = new File(activity.getExternalFilesDir(null) +
                                        Utilities.MAP_NAME_STRING + "track_" + cardItem.getItemId());
                               file2.delete();
                                File file3 = new File(activity.getExternalFilesDir(null) +
                                        Utilities.MAP_NAME_STRING + "image_" + cardItem.getItemId() + ".jpeg");
                                file3.delete();

                                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
                                Toast.makeText(activity, "Map successfully deleted", Toast.LENGTH_SHORT).show();
                            }
                    ).setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
            });

            ImageFilterButton goBack = view.findViewById(R.id.goBackButton);
            goBack.setOnClickListener(v -> {
                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
            });

        }
    }

    private void setImage(Activity activity, String value) {
        CardViewModel cardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CardViewModel.class);
        cardViewModel.getSelected().observe(getViewLifecycleOwner(), cardItem -> {

            String filename = activity.getExternalFilesDir(null) +
                    Utilities.MAP_NAME_STRING + "image_" + cardItem.getItemId() + ".jpeg";
            File file = new File(filename);

            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                uriToBitmap(Uri.parse(value), activity).compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            cardViewModel.changeImage(filename, cardItem.getItemId());
        });
    }

    private Bitmap uriToBitmap(Uri uri, Activity activity) {
        InputStream image_stream = null;
        try {
            image_stream = activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(image_stream);
    }
}

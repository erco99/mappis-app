package com.example.mappis;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mappis.CardMaps.AddCardViewModel;
import com.example.mappis.CardMaps.CardViewModel;
import com.example.mappis.CardMaps.Comments.Comment;
import com.example.mappis.CardMaps.Comments.CommentAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MapDetailsFragment extends Fragment {

    private static final SimpleDateFormat timestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY);
    private Button addButton;
    private TextInputEditText commentBox;
    private ListView listView;

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
            listView = view.findViewById(R.id.commentListView);

            AddCardViewModel addCardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddCardViewModel.class);
            CardViewModel cardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CardViewModel.class);

            cardViewModel.getSelected().observe(getViewLifecycleOwner(), cardItem -> {
                cardViewModel.getComments(cardItem.getItemId()).observe((LifecycleOwner) activity, comments -> {
                    ArrayList<Comment> list = new ArrayList<>(comments);
                    CommentAdapter adapter = new CommentAdapter(activity, R.layout.single_comment_row, list);
                    listView.setAdapter(adapter);
                });

                addButton.setOnClickListener(v -> {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    addCardViewModel.addComment(new Comment(commentBox.getText().toString(),
                            cardItem.getItemId(),
                            timestamp.format(time)));
                    commentBox.getText().clear();
                    Toast.makeText(activity, "Comment added", Toast.LENGTH_SHORT).show();
                });
            });

            ImageFilterButton goBack = view.findViewById(R.id.goBackButton);
            goBack.setOnClickListener(v -> {
                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
            });
        }
    }
}

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mappis.CardMaps.AddCardViewModel;
import com.example.mappis.CardMaps.CardItem;
import com.example.mappis.CardMaps.CardViewModel;
import com.example.mappis.CardMaps.Comments.Comment;
import com.example.mappis.CardMaps.Comments.CommentAdapter;
import com.example.mappis.CardMaps.OnItemListener;
import com.example.mappis.Database.CardItemDAO;
import com.example.mappis.Database.MapDatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MapDetailsFragment extends Fragment {

    ImageFilterButton goBack;
    Button addButton;
    TextInputEditText commentBox;
    ListView listView;
    CommentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        commentBox = view.findViewById(R.id.commentTextInputEditText);
        addButton = view.findViewById(R.id.add_comment_button);
        listView = view.findViewById(R.id.commentListView);


        AddCardViewModel addCardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(AddCardViewModel.class);

        CardViewModel cardViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CardViewModel.class);

        cardViewModel.getSelected().observe(getViewLifecycleOwner(), new Observer<CardItem>() {
            @Override
            public void onChanged(CardItem cardItem) {

                cardViewModel.setId(cardItem.getItemId());

                System.out.println(cardViewModel.getComments().getValue());

                System.out.println(cardViewModel.getCardItems().getValue());

                addButton.setOnClickListener(v -> {
                    addCardViewModel.addComment(new Comment(commentBox.getText().toString(),
                            cardItem.getItemId()));
                    commentBox.getText().clear();

                    Toast.makeText(activity, "Comment added", Toast.LENGTH_SHORT).show();

                });
            }
        });


        goBack = view.findViewById(R.id.goBackButton);
        goBack.setOnClickListener(v -> {
            ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack();
        });
    }
}

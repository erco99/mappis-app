package com.example.mappis.CardMaps.Comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mappis.R;

import java.util.ArrayList;


public class CommentAdapter extends ArrayAdapter<Comment> {

    Context context;
    int resource;
    TextView commentTextView;
    TextView dateCommentTextView;

    public CommentAdapter(@NonNull Context context, int resource, ArrayList<Comment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        commentTextView = convertView.findViewById(R.id.commentTextView);
        dateCommentTextView = convertView.findViewById(R.id.commentDateTextView);

        commentTextView.setText(getItem(position).getComment());
        dateCommentTextView.setText(getItem(position).getTimestamp());

        return convertView;
    }
}

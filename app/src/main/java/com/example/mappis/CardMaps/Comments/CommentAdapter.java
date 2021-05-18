package com.example.mappis.CardMaps.Comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.mappis.CardMaps.CardItem;
import com.example.mappis.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private Context context;
    private int resource;
    List<Comment> comments;

    public CommentAdapter(@NonNull Context context, int resource, List<Comment> comments) {
        super(context, resource);
        this.comments = comments;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String comment = getItem(position).getComment();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, null);

        TextView commentTextView = convertView.findViewById(R.id.commentTextView);


        commentTextView.setText(comment);

        return convertView;
    }

    public void setData(List<Comment> list) {
        this.comments = new ArrayList<>(list);
        notifyDataSetChanged();
    }
}

package com.example.mappis.CardMaps.Comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappis.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> comments;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView comment;
        private final TextView date;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            comment = (TextView) view.findViewById(R.id.commentTextView);
            date = (TextView) view.findViewById(R.id.commentDateTextView);
        }

        public TextView getDate() {
            return date;
        }

        public TextView getComment() {
            return comment;
        }
    }


    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_comment_row, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getComment().setText(comments.get(position).getComment());

        viewHolder.getDate().setText(comments.get(position).getTimestamp());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return comments.size();
    }
}

package com.example.kandydatpl.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Comment;

import java.util.ArrayList;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "CommentsRVAdapter";

    private Context ctx;
    private ArrayList<Comment> comments;

    public CommentsRecyclerViewAdapter(Context ctx, ArrayList<Comment> comments) {
        this.ctx = ctx;
        this.comments = comments;
    }

    @NonNull
    @Override
    //responsible for inflating the view
    //can be always the same (beside the layout name)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    //change what layout look like
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Comment currentComment = comments.get(position);

        holder.fullNameTxt.setText(currentComment.getCreator());
        holder.commentContentTxt.setText(currentComment.getContent());

        holder.commentLikeBtn.setOnClickListener(view -> {
            if(currentComment.isLikedBy(DataStore.getUserData().getUserId())) {
                Runnable afterUnlikeSuccess = () -> ((Activity) ctx).runOnUiThread(() -> {
                    Log.d(TAG, "unliked comment: " + currentComment.getContent());

                    currentComment.getLikedBy().remove(DataStore.getUserData().getUserId());

                    holder.commentLikeBtn.setColorFilter(Color.parseColor("#E9E9E9"));
                    holder.likeCountTxt.setText(String.valueOf(currentComment.getLikeCount()));
                });

                Runnable afterUnlikeFailure = () -> ((Activity) ctx).runOnUiThread(() ->
                        Toast.makeText(ctx, "Failed to unlike the comment", Toast.LENGTH_LONG).show());

                dataProvider.changeLikeStatusComment(afterUnlikeSuccess, afterUnlikeFailure, currentComment, false);
            }
            else {
                Runnable afterLikeSuccess = () -> ((Activity) ctx).runOnUiThread(() -> {
                    Log.d(TAG, "liked comment: " + currentComment.getContent());

                    currentComment.getLikedBy().add(DataStore.getUserData().getUserId());

                    holder.commentLikeBtn.setColorFilter(Color.BLACK);
                    holder.likeCountTxt.setText(String.valueOf(currentComment.getLikeCount()));
                });

                Runnable afterLikeFailure = () -> ((Activity) ctx).runOnUiThread(() ->
                        Toast.makeText(ctx, "Failed to like the comment", Toast.LENGTH_LONG).show());

                dataProvider.changeLikeStatusComment(afterLikeSuccess, afterLikeFailure, currentComment, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //holds individual entries in memory
        TextView fullNameTxt;
        TextView commentContentTxt;
        ImageButton commentLikeBtn;
        TextView likeCountTxt;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTxt = itemView.findViewById(R.id.fullNameTxt);
            commentContentTxt = itemView.findViewById(R.id.commentContentTxt);
            commentLikeBtn = itemView.findViewById(R.id.commentLikeBtn);
            likeCountTxt = itemView.findViewById(R.id.likeCountTxt);
            cardView = itemView.findViewById(R.id.recyclerCommentItemCardView);
        }
    }
}

package com.example.kandydatpl.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.models.Comment;

import java.util.ArrayList;

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

        //TODO: change to db query of user id
        if(holder.fullNameTxt == null) {
            Log.d(TAG, "onBindViewHolder: fullname is null");
        }
        holder.fullNameTxt.setText("ANONYMOUS");
        holder.commentContentTxt.setText(comments.get(position).getContent());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + comments.get(position));

                Toast.makeText(ctx, comments.get(position).getId(), Toast.LENGTH_SHORT).show();
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
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTxt = itemView.findViewById(R.id.fullNameTxt);
            commentContentTxt = itemView.findViewById(R.id.commentContentTxt);
            cardView = itemView.findViewById(R.id.recyclerCommentItemCardView);
        }
    }
}

package com.example.kandydatpl.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kandydatpl.R;
import com.example.kandydatpl.activities.QuestionDetailsActivity;
import com.example.kandydatpl.models.Question;

import java.util.ArrayList;

public class QuestionsRecyclerViewAdapter extends RecyclerView.Adapter<QuestionsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "QuestionsRVAdapter";

    private Context ctx;
    private ArrayList<Question> questions;

    public QuestionsRecyclerViewAdapter(Context ctx, ArrayList<Question> questions) {
        this.ctx = ctx;
        this.questions = questions;
    }

    @NonNull
    @Override
    //responsible for inflating the view
    //can be always the same (beside the layout name)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_question_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    //change what layout look like
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.questionTxt.setText(questions.get(position).getContent());

        holder.commentsCountTxt.setText(String.valueOf(questions.get(position).getCommentCount()));

        //todo zmniejszyć duplikację kodu
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "QuestionsList => onClick: clicked on title: " + questions.get(position));

                Intent questionDetailsIntent = new Intent(ctx, QuestionDetailsActivity.class);

                questionDetailsIntent.putExtra("questionId", questions.get(position).getId());
                Log.d(TAG, "QuestionsList onClick: " + questions.get(position).getContent() + " | id: " + questions.get(position).getId());
                ctx.startActivity(questionDetailsIntent);

//                Toast.makeText(ctx, questions.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "QuestionsList => onClick: clicked on card: " + questions.get(position));

                Intent questionDetailsIntent = new Intent(ctx, QuestionDetailsActivity.class);

                questionDetailsIntent.putExtra("questionId", questions.get(position).getId());
                Log.d(TAG, "QuestionsList onClick: " + questions.get(position).getContent() + " | id: " + questions.get(position).getId());
                ctx.startActivity(questionDetailsIntent);

//                Toast.makeText(ctx, questions.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //holds individual entries in memory
        TextView questionTxt;
        TextView commentsCountTxt;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTxt = itemView.findViewById(R.id.questionTxt);
            commentsCountTxt = itemView.findViewById(R.id.commentsCountTxt);
            cardView = itemView.findViewById(R.id.recyclerQuestionItemCardView);
        }
    }
}

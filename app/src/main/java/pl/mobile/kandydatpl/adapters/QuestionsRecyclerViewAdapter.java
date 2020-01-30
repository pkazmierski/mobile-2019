package pl.mobile.kandydatpl.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import pl.mobile.kandydatpl.activities.QuestionDetailsActivity;
import pl.mobile.kandydatpl.activities.QuestionsActivity;
import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.models.Question;

import java.util.ArrayList;
import java.util.HashMap;

import static pl.mobile.kandydatpl.logic.Logic.dataProvider;

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

        if(DataStore.getUserData().isQuestionBookmarked(questions.get(position)))
            holder.bookmarkQuestionBtn.setImageDrawable(ResourcesCompat.getDrawable(ctx.getResources(), R.drawable.ic_bookmark_gray_20dp, null));
        else
            holder.bookmarkQuestionBtn.setImageDrawable(ResourcesCompat.getDrawable(ctx.getResources(), R.drawable.ic_bookmark_border_gray_20dp, null));

        //todo zmniejszyć duplikację kodu
        holder.cardView.setOnClickListener(view -> {
            Log.d(TAG, "QuestionsList => onClick: clicked on title: " + questions.get(position));

            Intent questionDetailsIntent = new Intent(ctx, QuestionDetailsActivity.class);

            questionDetailsIntent.putExtra("questionId", questions.get(position).getId());
            Log.d(TAG, "QuestionsList onClick: " + questions.get(position).getContent() + " | id: " + questions.get(position).getId());
            ctx.startActivity(questionDetailsIntent);

//                Toast.makeText(ctx, questions.get(position).getId(), Toast.LENGTH_SHORT).show();
        });


        holder.bookmarkQuestionBtn.setOnClickListener(view -> {
            Log.d(TAG, "Attempting to bookmark: " + questions.get(position));

            if (DataStore.getUserData().isQuestionBookmarked(questions.get(position))) {
                Runnable afterUnBookmarkSuccess = () -> ((Activity) ctx).runOnUiThread(() -> {
                    Toast.makeText(ctx, ctx.getString(R.string.remove_from_bookmarks), Toast.LENGTH_SHORT).show();
                    holder.bookmarkQuestionBtn.setImageDrawable(ResourcesCompat.getDrawable(ctx.getResources(), R.drawable.ic_bookmark_border_gray_20dp, null));

                    if (ctx instanceof QuestionsActivity) {
                        if(((QuestionsActivity) ctx).isBookmarksOnly()) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("questionId", questions.get(position).getId());
                            ((QuestionsActivity) ctx).updateUI(params);
                        }
                    }
                });
                Runnable afterUnBookmarkFailure = () -> ((Activity) ctx).runOnUiThread(() ->
                    Toast.makeText(ctx, ctx.getString(R.string.failed_to_remove_from_bookmarks), Toast.LENGTH_LONG).show());

                dataProvider.bookmarkQuestion(afterUnBookmarkSuccess, afterUnBookmarkFailure, questions.get(position), false);
            } else {
                Runnable afterBookmarkSuccess = () -> ((Activity) ctx).runOnUiThread(() -> {
                    Toast.makeText(ctx, ctx.getString(R.string.added_to_bookmarks), Toast.LENGTH_SHORT).show();
                    holder.bookmarkQuestionBtn.setImageDrawable(ResourcesCompat.getDrawable(ctx.getResources(), R.drawable.ic_bookmark_gray_20dp, null));
                });
                Runnable afterBookmarkFailure = () -> ((Activity) ctx).runOnUiThread(() ->
                        Toast.makeText(ctx, ctx.getString(R.string.failed_to_bookmark), Toast.LENGTH_LONG).show());


                dataProvider.bookmarkQuestion(afterBookmarkSuccess, afterBookmarkFailure, questions.get(position), true);
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
        ImageButton bookmarkQuestionBtn;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTxt = itemView.findViewById(R.id.questionTxt);
            commentsCountTxt = itemView.findViewById(R.id.commentsCountTxt);
            bookmarkQuestionBtn = itemView.findViewById(R.id.bookmarkQuestionBtn);
            cardView = itemView.findViewById(R.id.recyclerQuestionItemCardView);
        }
    }

    public void filterList(ArrayList<Question> filteredQuestions) {
        questions = filteredQuestions;
        notifyDataSetChanged();
    }
}

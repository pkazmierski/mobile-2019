package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateCommentMutation;
import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.amazonaws.amplify.generated.graphql.CreateStudyOfferMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Comment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateCommentInput;
import type.CreateQuestionInput;
import type.CreateStudyOfferInput;

import static com.example.kandydatpl.logic.Logic.appSyncClient;
import static com.example.kandydatpl.logic.Logic.dataProvider;

public class MainActivity extends NavigationDrawerActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

        Logic.initAppSync(getApplicationContext());

        dataProvider.getUserData(null, null);

        dataProvider.getStudyOffers(afterGetStudyOffersSuccess, null);

        /*String qid = "b13e9428-d1db-4f51-81a7-7aa24a2256ae";
        mutationAddComment(new Comment(null, "At XYZ.", qid));
        mutationAddComment(new Comment(null, "At ABC.", qid));
        mutationAddComment(new Comment(null, "At DEF.", qid));

        List<String> commentIds = new ArrayList<>();
        commentIds.add("4597f234-3e3c-4cb9-a785-9a8435c97959");
        commentIds.add("79e33384-1f37-4b2e-971f-5111bfad2cf7");
        commentIds.add("bc44ea2f-0399-43dd-87bd-2993353824c4");

        mutationAddQuestion("Where can I get some cheap and tasty food?", commentIds);
        mutationAddQuestion("What is the minimum level of English proficiency required?");
        mutationAddQuestion("How much does the student housing cost per month?");
        mutationAddQuestion("When is the deadline for application?");

        String content1 = "Test offer 1";
        ArrayList<String> tags1 = new ArrayList<>();
        tags1.add("it");
        tags1.add("1szy_stopien");
        tags1.add("ftims");


        String content2 = "Test offer 2";
        ArrayList<String> tags2 = new ArrayList<>();
        tags2.add("elektronika");
        tags2.add("2gi_stopien");
        tags2.add("weeia");

        String content3 = "Test offer 3";
        ArrayList<String> tags3 = new ArrayList<>();
        tags3.add("biotech");
        tags3.add("1szy_stopien");
        tags3.add("chemia");

        addOffers(content1, tags1);
        addOffers(content2, tags2);
        addOffers(content3, tags3);*/
    }

//    public void addOffers(String content, ArrayList<String> tags) {
//        GraphQLCall.Callback<CreateStudyOfferMutation.Data> addOfferCallback = new GraphQLCall.Callback<CreateStudyOfferMutation.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<CreateStudyOfferMutation.Data> response) {
//                Log.i("Results", "Added question: " + response.data().toString());
//
//            }
//
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//                Log.e("Error", e.toString());
//            }
//        };
//
//
//        CreateStudyOfferInput createStudyOfferInput = CreateStudyOfferInput.builder()
//                .content(content)
//                .tags(tags)
//                .build();
//
//        appSyncClient.mutate(CreateStudyOfferMutation.builder().input(createStudyOfferInput).build())
//                .enqueue(addOfferCallback);
//    }

    Runnable afterGetStudyOffersSuccess = () -> runOnUiThread(() ->
            Log.d(TAG, "Acquired study offers: " + DataStore.getStudyOffers().toString()));

    public void goToQuestions(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        startActivity(questionsIntent);
    }

    public void goToBookmarks(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        questionsIntent.putExtra("bookmarksOnly", true);
        startActivity(questionsIntent);
    }
}

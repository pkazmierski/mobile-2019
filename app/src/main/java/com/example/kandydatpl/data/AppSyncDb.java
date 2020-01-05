package com.example.kandydatpl.data;

import android.annotation.SuppressLint;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreateCommentMutation;
import com.amazonaws.amplify.generated.graphql.CreatePublicEventMutation;
import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.amazonaws.amplify.generated.graphql.CreateUserEventMutation;
import com.amazonaws.amplify.generated.graphql.CreateUserMutation;
import com.amazonaws.amplify.generated.graphql.DeleteCommentMutation;
import com.amazonaws.amplify.generated.graphql.DeleteUserEventMutation;
import com.amazonaws.amplify.generated.graphql.GetUserQuery;
import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;
import com.amazonaws.amplify.generated.graphql.ListContactsQuery;
import com.amazonaws.amplify.generated.graphql.ListFilesQuery;
import com.amazonaws.amplify.generated.graphql.ListPublicEventsQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.amplify.generated.graphql.ListUserEventsQuery;
import com.amazonaws.amplify.generated.graphql.UpdateCommentMutation;
import com.amazonaws.amplify.generated.graphql.UpdateQuestionMutation;
import com.amazonaws.amplify.generated.graphql.UpdateUserEventMutation;
import com.amazonaws.amplify.generated.graphql.UpdateUserMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.models.ChecklistEvent;
import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Contact;
import com.example.kandydatpl.models.File;
import com.example.kandydatpl.models.Question;
import com.example.kandydatpl.models.UserData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import javax.annotation.Nonnull;

import type.CreateCommentInput;
import type.CreatePublicEventInput;
import type.CreateQuestionInput;
import type.CreateUserEventInput;
import type.CreateUserInput;
import type.DeleteCommentInput;
import type.DeleteUserEventInput;
import type.ModelCommentFilterInput;
import type.ModelStringFilterInput;
import type.UpdateCommentInput;
import type.UpdateQuestionInput;
import type.UpdateUserEventInput;
import type.UpdateUserInput;

import static com.example.kandydatpl.logic.Logic.appSyncClient;

public class AppSyncDb implements DataProvider {
    //TODO sprawdzić te response.data() czy warto robić nullchecki i jakie
    //TODO posprawdzać logi i ew. poprawić
    //TODO zamienić if getId().equals("") na wyrzucanie wyjątków
    //TODO dodać sprawdzanie, czy nie chcemy edytować/usuwać pytań, które nie zostały wysłane
    //TODO zamienić tworzenie zawsze nowego callbacka na wewnętrzną klasę, która ma callback i runnable (jeżeli się da)
    //TODO czasem z bazy moze wrocic null
    //TODO dodać sprawdzanie w onResult, tam też mogą być błędy np. uprawnień

    private static AppSyncDb instance = null;
    private static final String TAG = "AppSyncDb";
    private static final @SuppressLint("SimpleDateFormat")
    DateFormat awsDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private AppSyncDb() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        awsDateFormat.setTimeZone(tz);
    }

    public static AppSyncDb getInstance() {
        if (instance == null) {
            instance = new AppSyncDb();
        }

        return instance;
    }

    private String questionsNextToken;

    private ArrayList<ChecklistEvent> userEventsToArrayList(List<ListUserEventsQuery.Item> dbEvents) {
        ArrayList<ChecklistEvent> checklistEvents = new ArrayList<>();

        for (ListUserEventsQuery.Item item : dbEvents) {
            try {
                ChecklistEvent checklistEvent = new ChecklistEvent(item.id(), item.title(), item.description(), true, item.done(), awsDateFormat.parse(item.deadline()));
                checklistEvents.add(checklistEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return checklistEvents;
    }

    private ArrayList<ChecklistEvent> publicEventsToArrayList(List<ListPublicEventsQuery.Item> dbEvents) {
        ArrayList<ChecklistEvent> checklistEvents = new ArrayList<>();

        for (ListPublicEventsQuery.Item item : dbEvents) {
            try {
                ChecklistEvent checklistEvent = new ChecklistEvent(item.id(), item.title(), item.description(), false, item.done(), awsDateFormat.parse(item.deadline()));
                checklistEvents.add(checklistEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return checklistEvents;
    }

    private ArrayList<Contact> contactsToArrayList(List<ListContactsQuery.Item> dbContacts) {
        ArrayList<Contact> contacts = new ArrayList<>();

        for (ListContactsQuery.Item dbContact : dbContacts) {
            Contact contact = new Contact(dbContact.id(), dbContact.name());
            contact.setEmail(dbContact.email() == null ? "" : dbContact.email());
            contact.setPhone(dbContact.phone() == null ? -1 : Integer.valueOf(dbContact.phone()));
            contacts.add(contact);
        }

        return contacts;
    }

    private ArrayList<File> filesToArrayList(List<ListFilesQuery.Item> dbFiles) {
        ArrayList<File> files = new ArrayList<>();

        for (ListFilesQuery.Item dbFile : dbFiles) {
            File file = new File(dbFile.id(), dbFile.name(), dbFile.link());
            files.add(file);
        }

        return files;
    }

    private ArrayList<Question> questionsToArrayList(List<ListQuestionsQuery.Item> dbQuestions) {
        ArrayList<Question> questions = new ArrayList<>();

        for (ListQuestionsQuery.Item item : dbQuestions) {
            try {
                questions.add(new Question(item.id(), item.content(), item.commentCount(), awsDateFormat.parse(item.createdAt())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(questions, new Comparator<Question>() {
            @Override
            public int compare(Question lhs, Question rhs) {
                return lhs.getCreatedAt().compareTo(rhs.getCreatedAt()) > 0 ? -1 : 1;
            }
        });

        return questions;
    }

    private ArrayList<Comment> commentsToArrayList(List<ListCommentsQuery.Item> dbComments) {
        ArrayList<Comment> comments = new ArrayList<>();

        for (ListCommentsQuery.Item item : dbComments) {
            try {
                if (item.likedBy() != null) {
                    comments.add(new Comment(
                            item.id(),
                            item.content(),
                            item.questionId(),
                            awsDateFormat.parse(item.createdAt()),
                            new ArrayList<>(item.likedBy()),
                            item.creator()));
                } else {
                    comments.add(new Comment(
                            item.id(),
                            item.content(),
                            item.questionId(),
                            awsDateFormat.parse(item.createdAt()),
                            item.creator()
                    ));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment lhs, Comment rhs) {
                return lhs.getCreatedAt().compareTo(rhs.getCreatedAt()) > 0 ? -1 : 1;
            }
        });

        return comments;
    }

    private HashMap<String, Integer> eventsOrderToHashMap(List<String> dbEventsOrder) {
        HashMap<String, Integer> eventsOrder = new HashMap<>();

        if (dbEventsOrder != null) {
            for (String eventOrder : dbEventsOrder) {
                String[] tokens = eventOrder.split(",");
                String id = tokens[0];
                String index = tokens[1];

                eventsOrder.put(id, Integer.parseInt(index));
            }
        }

        return eventsOrder;
    }

    @Override
    public void getQuestions(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<ListQuestionsQuery.Data> listQuestionsCallback = new GraphQLCall.Callback<ListQuestionsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListQuestionsQuery.Data> response) {
                if (!response.hasErrors()) {
                    if (response.data().listQuestions().items() != null) {
                        Log.i("Results", "Questions (first): " + Objects.requireNonNull(response.data().listQuestions()).toString());

                        DataStore.setQuestions(questionsToArrayList(response.data().listQuestions().items()));

                        if (response.data().listQuestions().nextToken() != null) {
                            questionsNextToken = response.data().listQuestions().nextToken();
                        }
                    } else {
                        Log.i("Results", "listQuestions() is null");
                    }

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "getQuestions: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        appSyncClient.query(ListQuestionsQuery.builder()
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listQuestionsCallback);
    }

    @Override
    public boolean moreQuestionsAvailable() {
        return questionsNextToken != null;
    }

    @Override
    public void getNextQuestions(Runnable onSuccess, Runnable onFailure) {
        if (moreQuestionsAvailable()) {
            GraphQLCall.Callback<ListQuestionsQuery.Data> listQuestionsCallback = new GraphQLCall.Callback<ListQuestionsQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<ListQuestionsQuery.Data> response) {
                    if (!response.hasErrors()) {
                        if (response.data().listQuestions() != null) {
                            Log.i("Results", "Questions (next): " + Objects.requireNonNull(response.data().listQuestions()).toString());
                            DataStore.setQuestions(questionsToArrayList(response.data().listQuestions().items()));
                            if (response.data().listQuestions().nextToken() != null) {
                                questionsNextToken = response.data().listQuestions().nextToken();
                            }
                        } else {
                            Log.i("Results", "listQuestions() is null");
                        }

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } else {
                        Log.e(TAG, "getQuestions: " + response.errors().toString());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("ERROR", e.toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            };

            appSyncClient.query(ListQuestionsQuery.builder()
                    .nextToken(questionsNextToken)
                    .build())
                    .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                    .enqueue(listQuestionsCallback);
        }
    }

    @Override
    public void addQuestion(Runnable onSuccess, Runnable onFailure, Question question) {
        if (question.getId().equals("")) { //new local (unpushed) question
            GraphQLCall.Callback<CreateQuestionMutation.Data> addQuestionCallback = new GraphQLCall.Callback<CreateQuestionMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<CreateQuestionMutation.Data> response) {
                    if (!response.hasErrors()) {
                        Log.i("Results", "Added question: " + response.data().toString());

                        question.setId(response.data().createQuestion().id());
                        DataStore.addQuestion(question);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } else {
                        Log.e(TAG, "addQuestion: " + response.errors().toString());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("Error", e.toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            };

            String isoDateTime = awsDateFormat.format(question.getCreatedAt());

            CreateQuestionInput createQuestionInput = CreateQuestionInput.builder()
                    .content(question.getContent())
                    .commentCount(question.getCommentCount())
                    .createdAt(isoDateTime)
                    .build();

            appSyncClient.mutate(CreateQuestionMutation.builder().input(createQuestionInput).build())
                    .enqueue(addQuestionCallback);
        } else {
            throw new IllegalArgumentException("Trying to add a question with an ID already assigned.");
        }
    }

    private void changeCommentCount(@Nonnull Question question, @Nonnull int change) {
        GraphQLCall.Callback<UpdateQuestionMutation.Data> updateQuestionCallback = new GraphQLCall.Callback<UpdateQuestionMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateQuestionMutation.Data> response) {
                Log.i("Results", "Updated question: " + response.data().toString());
                question.setCommentCount(question.getCommentCount() + change);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
            }
        };

        UpdateQuestionInput updateQuestionInput = UpdateQuestionInput.builder()
                .id(question.getId())
                .commentCount(question.getCommentCount() + change)
                .build();

        appSyncClient.mutate(UpdateQuestionMutation.builder().input(updateQuestionInput).build())
                .enqueue(updateQuestionCallback);
    }

    @Override
    public void bookmarkQuestion(Runnable onSuccess, Runnable onFailure, Question question, boolean add) {
        GraphQLCall.Callback<UpdateUserMutation.Data> updateUserDataCallback = new GraphQLCall.Callback<UpdateUserMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateUserMutation.Data> response) {
                Log.i("Results", "Added bookmark: " + response.data().toString());

                if (add)
                    DataStore.getUserData().addQuestionBookmark(question);
                else
                    DataStore.getUserData().removeQuestionBookmark(question);

                if (onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        ArrayList<String> newBookmarks = new ArrayList<>(DataStore.getUserData().getQuestionBookmarks());
        if (add)
            newBookmarks.add(question.getId());
        else
            newBookmarks.remove(question.getId());

        UpdateUserInput updateUserDataInput = UpdateUserInput.builder()
                .id(DataStore.getUserData().getLogin())
                .bookmarks(newBookmarks)
                .build();

        appSyncClient.mutate(UpdateUserMutation.builder().input(updateUserDataInput).build())
                .enqueue(updateUserDataCallback);
    }

    @Override
    public void getComments(Runnable onSuccess, Runnable onFailure, Question question) {
        GraphQLCall.Callback<ListCommentsQuery.Data> listCommentsCallback = new GraphQLCall.Callback<ListCommentsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListCommentsQuery.Data> response) {
                if (!response.hasErrors()) {
                    Log.i("Results", response.data().listComments().toString());

                    question.setComments(commentsToArrayList(response.data().listComments().items()));

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "getComments: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        ModelStringFilterInput questionIdFilter = ModelStringFilterInput.builder().eq(question.getId()).build();
        ModelCommentFilterInput commentFilter = ModelCommentFilterInput.builder().questionId(questionIdFilter).build();

        appSyncClient.query(ListCommentsQuery.builder()
                .filter(commentFilter)
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listCommentsCallback);
    }

    @Override
    public void addComment(Runnable onSuccess, Runnable onFailure, Comment comment) {
        if (comment.getId().equals("")) { //new local (unpushed) comment
            GraphQLCall.Callback<CreateCommentMutation.Data> createCommentCallback = new GraphQLCall.Callback<CreateCommentMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<CreateCommentMutation.Data> response) {
                    if (!response.hasErrors()) {
                        Log.i("Results", "Added comment: " + response.data().toString());

                        comment.setId(response.data().createComment().id());

                        Question question = DataStore.getQuestion(comment.getQuestionId());
                        assert question != null;
                        changeCommentCount(question, 1);


                        DataStore.getQuestion(comment.getQuestionId()).addComment(comment);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } else {
                        Log.e(TAG, "addComment: " + response.errors().toString());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("Error", e.toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            };

            String isoDateTime = awsDateFormat.format(comment.getCreatedAt());

            CreateCommentInput createCommentInput = CreateCommentInput.builder()
                    .content(comment.getContent())
                    .questionId(comment.getQuestionId())
                    .createdAt(isoDateTime)
                    .build();

            appSyncClient.mutate(CreateCommentMutation.builder().input(createCommentInput).build())
                    .enqueue(createCommentCallback);
        }
    }

    @Override
    public void removeComment(Runnable onSuccess, Runnable onFailure, Comment comment) {
        GraphQLCall.Callback<DeleteCommentMutation.Data> removeCommentCallback = new GraphQLCall.Callback<DeleteCommentMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<DeleteCommentMutation.Data> response) {
                if (!response.hasErrors()) {
                    Log.i("Results", "Removed comment: " + response.data().toString());

                    Question question = DataStore.getQuestion(comment.getQuestionId());
                    assert question != null;
                    changeCommentCount(question, -1);

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "removeComment: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        DeleteCommentInput deleteCommentInput = DeleteCommentInput.builder()
                .id(comment.getId())
                .build();

        appSyncClient.mutate(DeleteCommentMutation.builder().input(deleteCommentInput).build())
                .enqueue(removeCommentCallback);
    }

    @Override
    public void modifyComment(Runnable onSuccess, Runnable onFailure, Comment comment) {
        GraphQLCall.Callback<UpdateCommentMutation.Data> updateCommentCallback = new GraphQLCall.Callback<UpdateCommentMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateCommentMutation.Data> response) {
                Log.i("Results", "Modified comment: " + response.data().toString());

                if (onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        UpdateCommentInput updateCommentInput = UpdateCommentInput.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();

        appSyncClient.mutate(UpdateCommentMutation.builder().input(updateCommentInput).build())
                .enqueue(updateCommentCallback);
    }


    //todo zrobic funckje lambda do tego lub przynajmniej pobierac liste like'ow przed dodaniem nowego
    @Override
    public void changeLikeStatusComment(Runnable onSuccess, Runnable onFailure, Comment comment, boolean add) {
        GraphQLCall.Callback<UpdateCommentMutation.Data> updateCommentCallback = new GraphQLCall.Callback<UpdateCommentMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateCommentMutation.Data> response) {
                Log.i("Results", "Changed like list of the comment: " + response.data().toString());

                if (onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        ArrayList<String> newLikedBy = new ArrayList<>(comment.getLikedBy());
        if (add && !newLikedBy.contains(DataStore.getUserData().getUserId())) {
            newLikedBy.add(DataStore.getUserData().getUserId());
        } else if (!add && newLikedBy.contains(DataStore.getUserData().getUserId())) {
            newLikedBy.remove(DataStore.getUserData().getUserId());
        } else {
            throw new Error("Trying to set wrong likedBy list entries for comment: " + comment.toString() + "///Add status: " + add);
        }

        UpdateCommentInput updateCommentInput = UpdateCommentInput.builder()
                .id(comment.getId())
                .likedBy(newLikedBy)
                .build();

        appSyncClient.mutate(UpdateCommentMutation.builder().input(updateCommentInput).build())
                .enqueue(updateCommentCallback);
    }

    //User

    @Override
    public void getUserData(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<GetUserQuery.Data> getUserDataCallback = new GraphQLCall.Callback<GetUserQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetUserQuery.Data> response) {
                if (!response.hasErrors()) {
                    assert response.data() != null;
                    assert response.data().getUser() != null;
                    if(response.data().getUser() == null) {
                        Log.e(TAG, "getUserData: " + "no data exists for the user" + AWSMobileClient.getInstance().getUsername());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                        return;
                    }
                    if (response.data().getUser().bookmarks() != null) {
                        Log.i("Results", Objects.requireNonNull(response.data().getUser().bookmarks()).toString());
                        DataStore.getUserData().setQuestionBookmarks(new ArrayList<String>(response.data().getUser().bookmarks()));
                        DataStore.getUserData().setEventsOrder(eventsOrderToHashMap(response.data().getUser().eventsOrder()));
                    } else {
                        Log.i("Results", "No bookmarks for the user " + DataStore.getUserData().getLogin());
                        DataStore.getUserData().setQuestionBookmarks(null);
                    }

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "getUserData: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        appSyncClient.query(GetUserQuery.builder()
                .id(DataStore.getUserData().getLogin())
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(getUserDataCallback);
    }

    @Override
    public void getUserDataOnLogin(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<GetUserQuery.Data> getUserDataCallback = new GraphQLCall.Callback<GetUserQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetUserQuery.Data> response) {
                if (!response.hasErrors()) {
                    if(response.data().getUser() == null) {
                        Log.d(TAG, "getUserData: " + "no data exists for the user" + AWSMobileClient.getInstance().getUsername());
                        DataStore.setUserData(null);
                    } else {
                        DataStore.setUserData(UserData.getInstance());
                        if (response.data().getUser().bookmarks() != null) {
                            Log.i("Results", Objects.requireNonNull(response.data().getUser().bookmarks()).toString());
                            DataStore.getUserData().setQuestionBookmarks(new ArrayList<String>(response.data().getUser().bookmarks()));
                            DataStore.getUserData().setEventsOrder(eventsOrderToHashMap(response.data().getUser().eventsOrder()));
                        } else {
                            Log.i("Results", "No bookmarks for the user " + DataStore.getUserData().getLogin());
                            DataStore.getUserData().setQuestionBookmarks(null);
                        }
                    }

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "getUserData: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        appSyncClient.query(GetUserQuery.builder()
                .id(AWSMobileClient.getInstance().getUsername())
                .build())
                .responseFetcher(AppSyncResponseFetchers.NETWORK_ONLY)
                .enqueue(getUserDataCallback);
    }

    @Override
    public void createNewUserData(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<CreateUserMutation.Data> createCommentCallback = new GraphQLCall.Callback<CreateUserMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<CreateUserMutation.Data> response) {
                if (!response.hasErrors()) {
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "createNewUserData: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };


        CreateUserInput createCommentInput = CreateUserInput.builder()
                .build();

        appSyncClient.mutate(CreateUserMutation.builder().input(createCommentInput).build())
                .enqueue(createCommentCallback);
    }

    @Override
    public void getUserEvents(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<ListUserEventsQuery.Data> listUserEventsCallback = new GraphQLCall.Callback<ListUserEventsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListUserEventsQuery.Data> response) {
                if(response.data().listUserEvents() != null && response.data().listUserEvents().items() != null) {
                    boolean tokenExists = false;
                    if(response.data().listUserEvents().nextToken() != null && !response.data().listUserEvents().nextToken().isEmpty()) {
                        tokenExists = true;
                    }
//                    Log.d(TAG, "getUserEvents list: " + response.data().listUserEvents().items().toString() + "; nextToken: " + tokenExists);
                } else {
                    Log.e(TAG, "getUserEvents contains nulls");
                }
                if (!response.hasErrors()) {
                    DataStore.setUserChecklistEvents(AppSyncDb.this.userEventsToArrayList(response.data().listUserEvents().items()));

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "getUserEvents: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        appSyncClient.query(ListUserEventsQuery.builder()
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listUserEventsCallback);

    }

    @Override
    public void getPublicEvents(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<ListPublicEventsQuery.Data> listPublicEventsCallback = new GraphQLCall.Callback<ListPublicEventsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListPublicEventsQuery.Data> response) {
                if(response.data().listPublicEvents() != null && response.data().listPublicEvents().items() != null) {
                    boolean tokenExists = false;
                    if(response.data().listPublicEvents().nextToken() != null && !response.data().listPublicEvents().nextToken().isEmpty()) {
                        tokenExists = true;
                    }
//                    Log.d(TAG, "getPublicEvents list: " + response.data().listPublicEvents().items().toString() + "; nextToken: " + tokenExists);
                } else {
                    Log.e(TAG, "getPublicEvents contains nulls");
                }
                if (!response.hasErrors()) {
                    DataStore.setPublicChecklistEvents(publicEventsToArrayList(response.data().listPublicEvents().items()));

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "getPublicEvents failed: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        appSyncClient.query(ListPublicEventsQuery.builder()
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listPublicEventsCallback);
    }

    @Override
    public void getAllEvents(Runnable onSuccess, Runnable onUserEventsFailure, Runnable onPublicEventsFailure) {
        DataStore.clearEvents();
        Runnable getUserEventsSuccess = new Runnable() {
            @Override
            public void run() {
                getPublicEvents(onSuccess, onPublicEventsFailure);
            }
        };
        getUserEvents(getUserEventsSuccess, onUserEventsFailure);
    }

    @Override
    public void updateSingleUserEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checklistEvent) {
        GraphQLCall.Callback<UpdateUserEventMutation.Data> updateUserEventMutationCallback = new GraphQLCall.Callback<UpdateUserEventMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateUserEventMutation.Data> response) {
                //TODO: zmienić, żeby była praca na kopii i ta kopia była tutaj przepisywana lub dodawana po usunięciu oryginału
                if (!response.hasErrors()) {
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "updateSingleUserEvent: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        UpdateUserEventInput updateUserEventInput = UpdateUserEventInput.builder()
                .id(checklistEvent.getId())
                .title(checklistEvent.getTitle())
                .description(checklistEvent.getDescription())
                .deadline(awsDateFormat.format(checklistEvent.getDeadline()))
                .done(checklistEvent.isDone())
                .build();

        appSyncClient.mutate(UpdateUserEventMutation.builder().input(updateUserEventInput).build())
                .enqueue(updateUserEventMutationCallback);
    }

    @Override
    public void createSingleUserEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checklistEvent) {
        if (checklistEvent.getId().equals("")) { //new, uninitialized event
            GraphQLCall.Callback<CreateUserEventMutation.Data> createUserEventMutationCallback = new GraphQLCall.Callback<CreateUserEventMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<CreateUserEventMutation.Data> response) {
                    if (!response.hasErrors()) {
                        checklistEvent.setId(response.data().createUserEvent().id());
                        DataStore.addEvent(checklistEvent);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } else {
                        Log.e(TAG, "createSingleUserEvent: " + response.errors().toString());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("Error", e.toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            };

            CreateUserEventInput createUserEventInput = CreateUserEventInput.builder()
                    .title(checklistEvent.getTitle())
                    .description(checklistEvent.getDescription())
                    .deadline(awsDateFormat.format(checklistEvent.getDeadline()))
                    .done(checklistEvent.isDone())
                    .build();

            appSyncClient.mutate(CreateUserEventMutation.builder().input(createUserEventInput).build())
                    .enqueue(createUserEventMutationCallback);
        }
    }

    public void removeSingleUserEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checklistEvent) {
        if (!checklistEvent.getId().equals("") || !checklistEvent.isUserCreated()) { //don't try to delete events with unknown id
            GraphQLCall.Callback<DeleteUserEventMutation.Data> deleteUserEventMutationCallback = new GraphQLCall.Callback<DeleteUserEventMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<DeleteUserEventMutation.Data> response) {
                    if (!response.hasErrors()) {
                        DataStore.getAllChecklistEvents().remove(checklistEvent);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } else {
                        Log.e(TAG, "removeSingleUserEvent: " + response.errors().toString());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("Error", e.toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            };

            DeleteUserEventInput createUserEventInput = DeleteUserEventInput.builder()
                    .id(checklistEvent.getId())
                    .build();

            appSyncClient.mutate(DeleteUserEventMutation.builder().input(createUserEventInput).build())
                    .enqueue(deleteUserEventMutationCallback);
        } else {
            Log.e(TAG, "removeSingleUserEvent: event doesn't have the ID assigned" + checklistEvent.toString());
        }
    }

    @Override
    public void setEventsOrder(Runnable onSuccess, Runnable onFailure, HashMap<String, Integer> eventsOrder) {
        GraphQLCall.Callback<UpdateUserMutation.Data> createUserEventMutationCallback = new GraphQLCall.Callback<UpdateUserMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateUserMutation.Data> response) {
                if (!response.hasErrors()) {
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e(TAG, "setEventsOrder: " + response.errors().toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        ArrayList<String> newEventsOrder = new ArrayList<>();

        for (String eventId : eventsOrder.keySet()) {
            newEventsOrder.add(eventId + "," + String.valueOf(eventsOrder.get(eventId)));
        }

        UpdateUserInput updateUserInput = UpdateUserInput.builder()
                .id(DataStore.getUserData().getLogin())
                .eventsOrder(newEventsOrder)
                .build();

        appSyncClient.mutate(UpdateUserMutation.builder().input(updateUserInput).build())
                .enqueue(createUserEventMutationCallback);
    }

    @Override
    public void addPublicEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checklistEvent) {
        if (checklistEvent.getId().equals("")) { //new, uninitialized event
            GraphQLCall.Callback<CreatePublicEventMutation.Data> createPublicEventCallback = new GraphQLCall.Callback<CreatePublicEventMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<CreatePublicEventMutation.Data> response) {
                    if (!response.hasErrors()) {
                        checklistEvent.setId(response.data().createPublicEvent().id());
                        DataStore.addEvent(checklistEvent);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } else {
                        Log.e(TAG, "addPublicEvent: " + response.errors().toString());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("Error", e.toString());
                    if (onFailure != null) {
                        onFailure.run();
                    }
                }
            };

            CreatePublicEventInput createPublicEventInput = CreatePublicEventInput.builder()
                    .title(checklistEvent.getTitle())
                    .description(checklistEvent.getDescription())
                    .deadline(awsDateFormat.format(checklistEvent.getDeadline()))
                    .done(checklistEvent.isDone())
                    .build();

            appSyncClient.mutate(CreatePublicEventMutation.builder().input(createPublicEventInput).build())
                    .enqueue(createPublicEventCallback);
        }
    }

    @Override
    public void getContacts(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<ListContactsQuery.Data> listContactsQueryCallback = new GraphQLCall.Callback<ListContactsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListContactsQuery.Data> response) {

                Log.d(TAG, "onResponse getContacts errors: " + response.errors());
                Log.d(TAG, "onResponse getContacts: " + response.data().listContacts().items());

                DataStore.setContacts(contactsToArrayList(response.data().listContacts().items()));

                if (onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        appSyncClient.query(ListContactsQuery.builder()
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listContactsQueryCallback);
    }

    @Override
    public void getFiles(Runnable onSuccess, Runnable onFailure) {
        GraphQLCall.Callback<ListFilesQuery.Data> listFilesQueryCallback = new GraphQLCall.Callback<ListFilesQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListFilesQuery.Data> response) {

                DataStore.setFiles(filesToArrayList(response.data().listFiles().items()));

                if (onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        };

        appSyncClient.query(ListFilesQuery.builder()
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listFilesQueryCallback);
    }
}

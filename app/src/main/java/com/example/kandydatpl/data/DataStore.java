package com.example.kandydatpl.data;

import android.util.Log;

import com.amazonaws.amplify.generated.graphql.ListUserEventsQuery;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String TAG = "DataStore";
    private static ArrayList<Event> events = new ArrayList<>();

    public static ArrayList<Event> getEvents() {
        return events;
    }

//    public static void setEvents(ArrayList<Event> events) {
//        DataStore.events = events;
//    }

    public static void setEvents(List<ListUserEventsQuery.Item> items) {
        events.clear();
        for (ListUserEventsQuery.Item item : items) {
            try {
                JSONObject jsonEvent = new JSONObject(item.data());
                Event event = new Event(
                        item.id(),
                        jsonEvent.getString("title"),
                        jsonEvent.getString("description"),
                        Logic.defaultFormat.parse(jsonEvent.getString("date")),
                        true,
                        jsonEvent.getBoolean("completed")
                        );
//                if(!events.contains(event)) {
                    events.add(event);
                    Log.d(TAG, "setEvents: adding new user event: " + event.toString());
//                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }

        }
    }
}

package com.example.kandydatpl.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.ChecklistEventRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.ChecklistEvent;
import com.example.kandydatpl.models.UserData;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class CalendarActivity extends AppCompatActivity {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM  YYYY", Locale.getDefault());
    private List<ChecklistEvent> events = new ArrayList<ChecklistEvent>();
    private List<Event> eventCal = new ArrayList<Event>();
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        dataProvider.getAllEvents(afterAllEventsSuccess, afterAllPublicEventsFailure, afterAllUserEventsFailure);
    }


    private Runnable afterAllEventsSuccess = () -> runOnUiThread(() -> {
        events = DataStore.getAllChecklistEvents();

        for (ChecklistEvent e : events){
            eventCal.add(new Event(Color.BLACK, e.getDeadline().getTime(),e.getTitle()));
        }
        compactCalendar.addEvents(eventCal);
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                for ( Event e : eventCal ) {
                    if (dateClicked.getTime() == e.getTimeInMillis()) {
                        // add -> to go into checklistactivity and display only the events from that day
                        Intent i = new Intent(getApplicationContext(),ChecklistEventActivity.class);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
    });

    private Runnable afterAllPublicEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, "Public events fetch failed", Toast.LENGTH_SHORT).show());

    private Runnable afterAllUserEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, "User events fetch failed", Toast.LENGTH_SHORT).show());

}



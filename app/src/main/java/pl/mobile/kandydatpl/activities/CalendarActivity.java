package pl.mobile.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.models.ChecklistEvent;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static pl.mobile.kandydatpl.logic.Logic.dataProvider;

public class CalendarActivity extends NavigationDrawerActivity {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL YYYY", Locale.getDefault());
    private List<ChecklistEvent> events = new ArrayList<ChecklistEvent>();
    private List<Event> eventCal = new ArrayList<Event>();

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_calendar, null, false);
        drawer.addView(contentView, 0);

        ctx = this;

        actionBar.setTitle(dateFormat.format(new Date()));

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        dataProvider.getAllEvents(afterAllEventsSuccess, afterAllPublicEventsFailure, afterAllUserEventsFailure);
    }


    private Runnable afterAllEventsSuccess = () -> runOnUiThread(() -> {
        events = new ArrayList<>(DataStore.getAllChecklistEvents());

        List<ChecklistEvent> toRemove = new ArrayList<>();

        for (ChecklistEvent ev : events) {
            if (!ev.isUserCreated() && ev.getOfferId() != null && !DataStore.getUserData().getActiveOffersIds().contains(ev.getOfferId())) {
                toRemove.add(ev);
            }
        }

        events.removeAll(toRemove);

        for (ChecklistEvent e : events) {
            eventCal.add(new Event(Color.BLACK, e.getDeadline().getTime(), e.getTitle()));
        }
        compactCalendar.addEvents(eventCal);
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Event clickedEvent = null;
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                for (Event e : eventCal) {
                    cal1.setTime(dateClicked);
                    cal2.setTimeInMillis(e.getTimeInMillis());

                    if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                        clickedEvent = e;
                }
                if (clickedEvent != null) {
                    // add -> to go into checklistactivity and display only the events from that day
                    Intent i = new Intent(ctx, ChecklistEventActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("filterDate", dateClicked);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
              actionBar.setTitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
    });

    private Runnable afterAllPublicEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.public_events_fetch_failed), Toast.LENGTH_SHORT).show());

    private Runnable afterAllUserEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.user_events_fetch_failed), Toast.LENGTH_SHORT).show());

}



package com.example.kandydatpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.logic.Logic;

import java.util.Calendar;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    protected ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        actionBar = getSupportActionBar();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.nav_home) {
//            startAnimatedActivity(new Intent(getApplicationContext(), MainActivity.class));
//        } else
        if (id == R.id.nav_questions) {
            startAnimatedActivity(new Intent(getApplicationContext(), QuestionsActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.nav_question_bookmarks) {
            startAnimatedActivity(new Intent(getApplicationContext(), QuestionsActivity.class)
                    .putExtra("bookmarksOnly", true)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.nav_files) {
            startAnimatedActivity(new Intent(getApplicationContext(), FileBrowserActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.nav_contacts) {
            startAnimatedActivity(new Intent(getApplicationContext(), ContactBrowserActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.nav_checklist) {
            //intent.putExtra("filterDate", Calendar.getInstance().getTime());
            startAnimatedActivity(new Intent(getApplicationContext(), ChecklistEventActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.nav_calendar){
            //intent.putExtra("filterDate", Calendar.getInstance().getTime());
            startAnimatedActivity(new Intent(getApplicationContext(), CalendarActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if(id == R.id.nav_logout) {
            AWSMobileClient.getInstance().signOut();
            DataStore.setUserData(null);
            Logic.appSyncInitialized = false;
            Intent loginIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        } else if (id == R.id.nav_study_offers) {
            startAnimatedActivity(new Intent(getApplicationContext(), StudyOffersActivity.class));
        } else if (id == R.id.nav_active_study_offers) {
            startAnimatedActivity(new Intent(getApplicationContext(), StudyOffersResultsActivity.class).putExtra("showOnlyActive", true));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void startAnimatedActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}

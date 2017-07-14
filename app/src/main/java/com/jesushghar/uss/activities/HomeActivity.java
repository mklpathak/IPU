package com.jesushghar.uss.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jesushghar.uss.Fragments.AboutFragment;
import com.jesushghar.uss.Fragments.AroundYouFragment;
import com.jesushghar.uss.Fragments.BookmarkFragment;
import com.jesushghar.uss.Fragments.ContactUsFragment;
import com.jesushghar.uss.Fragments.ExaminationFragment;
import com.jesushghar.uss.Fragments.NotificationFragment;
import com.jesushghar.uss.Fragments.ProfileFragment;
import com.jesushghar.uss.R;
import com.jesushghar.uss.helper.SQLiteHandler;
import com.jesushghar.uss.helper.SessionManager;
import com.jesushghar.uss.sync.UssSyncAdapter;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Fragment fragment = null;
    private Menu menu;
    private SQLiteHandler db;
   // private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing Sync Adapter.
        UssSyncAdapter.initializeSyncAdapter(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());

     //   session = new SessionManager(getApplicationContext());

       // if (!session.isLoggedIn()) {
         //   logoutUser();
       // }

        //HashMap<String, String> user = db.getUserDetails();
        //String name = user.get("name");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

//        MenuItem item = menu.findItem(R.id.nav_profile);
//        item.setTitle(name.toUpperCase());
        navigationView.setNavigationItemSelectedListener(this);


//        if (getIntent().getIntExtra("Fragment", 0) == 2){
//            fragment = new NotificationFragment();
  //          toolbar.setTitle("Notices");
    //    } else {
      //      fragment = new ProfileFragment();
        //    toolbar.setTitle("Profile");
        //}

        fragment = new NotificationFragment();
                  toolbar.setTitle("Notices");
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container,fragment).commit();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

       // if (id == R.id.nav_profile) {
        //    fragment = new ProfileFragment();
          //  toolbar.setTitle("Profile");
        //} else
        if (id == R.id.nav_notification) {
            fragment = new NotificationFragment();
            toolbar.setTitle("Notices");
        } else if (id == R.id.nav_bookmarks) {
            fragment = new BookmarkFragment();
            toolbar.setTitle("Bookmarks");
            //Toast.makeText(HomeActivity.this, "BookMarks Selected", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_around_you) {
            fragment = new AroundYouFragment();
            toolbar.setTitle("Around You");
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
            startActivity(intent);
            //fragment = new ActivitiesFragment();
            //toolbar.setTitle("Activities");
        } else if (id == R.id.nav_exam) {
            fragment = new ExaminationFragment();
            toolbar.setTitle("Examination");
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
            toolbar.setTitle("About University");
        } else if (id == R.id.nav_contact) {
            fragment = new ContactUsFragment();
            toolbar.setTitle("Contact");
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container,fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


   // private void logoutUser() {
        //session.setLogin(false);

        //db.deleteUsers();

        // Launching the login activity
        //Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        //startActivity(intent);
        //finish();
   // }
}

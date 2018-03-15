package com.superteam.timeme;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.superteam.timeme.Client.ClientListActivity;
import com.superteam.timeme.Project.ProjectListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);   // no need for up button on main screen
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_MI_clients:
                // start all client activity
                ClientListActivity.start(this);
                return true;

            case R.id.main_MI_projects:
                // start all projects activity
                ProjectListActivity.start(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

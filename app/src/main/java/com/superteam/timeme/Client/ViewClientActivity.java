package com.superteam.timeme.Client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.superteam.timeme.R;

/**
 * Created by User on 3/15/2018.
 */

public class ViewClientActivity  extends AppCompatActivity {

    /* start an activity in a static function */
    public static void start(Context context) {
        Intent intent = new Intent(context, ViewClientActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.viewClient_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);   // Enable the Up button
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_client, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.view_client_delete:
                // start all client activity
                return true;

            case R.id.view_client_edit:
                // start all client activity
                return true;

            case android.R.id.home: //In case the user clicked the up button, raise the canceled flag and fallthrough.
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

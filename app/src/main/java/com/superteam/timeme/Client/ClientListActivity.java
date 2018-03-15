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

public class ClientListActivity extends AppCompatActivity {

    /* start an activity in a static function */
    public static void start(Context context) {
        Intent intent = new Intent(context, ClientListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_client_list);

            Toolbar myToolbar = (Toolbar) findViewById(R.id.clientList_toolbar);
            setSupportActionBar(myToolbar);

            // Get a support ActionBar corresponding to this toolbar
            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);   // Enable the Up button

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_client_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.client_MI_1:
                AddClientActivity.start(this); // temporary, to have some flow
                return true;

            case R.id.client_MI_2:
                ViewClientActivity.start(this); // temporary, to have some flow
                return true;

            case android.R.id.home: //In case the user clicked the up button, raise the canceled flag and fallthrough.
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

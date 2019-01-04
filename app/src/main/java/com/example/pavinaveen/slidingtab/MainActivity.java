package com.example.pavinaveen.slidingtab;


import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;

import static com.example.pavinaveen.slidingtab.Tab3.wallet;
import static com.example.pavinaveen.slidingtab.userProfile.SHARED_PREFS;
import static com.example.pavinaveen.slidingtab.userProfile.name;
import static com.example.pavinaveen.slidingtab.userProfile.phone;
import static com.example.pavinaveen.slidingtab.userProfile.userid;

public class MainActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener,Tab3.OnFragmentInteractionListener {

    public static final String LOG_TAG = "MainActivity";

    SharedPreferences shrp;

    ConnectionReceiver receiver;
    IntentFilter intentFilter;



    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        ButterKnife.bind(this);

        receiver = new ConnectionReceiver();
        intentFilter = new IntentFilter("com.journaldev.broadcastreceiver.SOME_ACTION");




//        if (isNetworkAvailable()) {
//            // Create an Alert Dialog
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            // Set the Alert Dialog Message
//            builder.setMessage("Internet Connection Required")
//                    .setCancelable(false)
//                    .setPositiveButton("Retry",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int id) {
//                                    // Restart the Activity
//                                    Intent intent = getIntent();
//                                    finish();
//                                    startActivity(intent);
//                                }
//                            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }

        Log.i(LOG_TAG,"oncreate");
        //
        String userName;
        String phone1;

        shrp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        userName = shrp.getString(userid,"");
        phone1 = shrp.getString(wallet,"");


        Log.i(LOG_TAG,"Nmae   "+userName);
        Log.i(LOG_TAG,"Phone  "+phone1);

        //

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Vehicle List"));
        tabLayout.addTab(tabLayout.newTab().setText("Vehicle Logs"));
        tabLayout.addTab(tabLayout.newTab().setText("Intel Wallet"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitems,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                //Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this,userProfile.class);
                startActivity(intent);
//                finish();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Reported", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                finish();
                System.exit(0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
//    private boolean isNetworkAvailable() {
//        // Using ConnectivityManager to check for Network Connection
//        ConnectivityManager connectivityManager = (ConnectivityManager) this
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo == null;
//    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerReceiver(receiver, intentFilter);
    }
}
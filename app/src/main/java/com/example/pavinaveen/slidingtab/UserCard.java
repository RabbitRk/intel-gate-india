package com.example.pavinaveen.slidingtab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.pavinaveen.slidingtab.Tab3.wallet;
import static com.example.pavinaveen.slidingtab.userProfile.SHARED_PREFS;
import static com.example.pavinaveen.slidingtab.userProfile.name;
import static com.example.pavinaveen.slidingtab.userProfile.phone;
import static com.example.pavinaveen.slidingtab.userProfile.lnumber;
import static com.example.pavinaveen.slidingtab.userProfile.userid;

public class UserCard extends AppCompatActivity {

    TextView idTxt, nameTxt, phoneTxt, lnumberTxt, walletTxt;

    SharedPreferences shrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);

        idTxt = findViewById(R.id.idtext);
        nameTxt = findViewById(R.id.name);
        phoneTxt = findViewById(R.id.phoneNumber);
        lnumberTxt = findViewById(R.id.lnumber);
        walletTxt = findViewById(R.id.walletamount);

        shrp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        idTxt.setText(shrp.getString(userid,null));
        nameTxt.setText(shrp.getString(name,null));
        phoneTxt.setText(shrp.getString(phone,null));
        lnumberTxt.setText(shrp.getString(lnumber,null));
        walletTxt.setText(shrp.getString(wallet,null));

    }


}

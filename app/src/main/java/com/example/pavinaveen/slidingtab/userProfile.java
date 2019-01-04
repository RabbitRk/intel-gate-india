package com.example.pavinaveen.slidingtab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class userProfile extends AppCompatActivity {

    public static final String LOG_TAG = "userProfile";

    EditText nameTxt, phoneTxt, lisenceTxt;
    String nameStr, phoneStr, lisenceStr;
    public String getId = "ID";
    ProgressDialog progressDialog;
    //Shared preferences
    SharedPreferences shrp;

    public static final String name = "NameKey";
    public static final String phone = "PhoneKey";
    public static final String lnumber = "LisenceKey";
    public static final String userid = "IDKey";
    public static final String SHARED_PREFS = "myprefs";
    //Shared preferences End


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        SharedPreferences after  = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        String check_avail;

        check_avail = after.getString(name,null);
        Log.i(LOG_TAG,"Availability     "+check_avail);
//        Toast.makeText(this,"No profile"+check_avail ,Toast.LENGTH_SHORT).show();

        if (!Objects.equals(check_avail, null))
        {
            Intent intent = new Intent(this,UserCard.class);
            startActivity(intent);
            finish();
        }

        //Controls definition
        nameTxt = findViewById(R.id.pro);
        phoneTxt = findViewById(R.id.phoneNumber);
        lisenceTxt = findViewById(R.id.lisence);
        //



        shrp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

    }

    public void saved(View view) {

        nameStr = nameTxt.getText().toString();
        phoneStr = phoneTxt.getText().toString();
        lisenceStr = lisenceTxt.getText().toString();

       if (nameStr.equals("") || phoneStr.equals("") || lisenceStr.equals(""))
       {
           Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
           return;
       }

//       progressDialog = new ProgressDialog(userProfile.this);
//       progressDialog.setMessage("Please wait...");
//       progressDialog.show();

       StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

//                   progressDialog.dismiss();
                   try
                   {
                       JSONArray arr = new JSONArray(response);
                       JSONObject jb = arr.getJSONObject(0);
                       getId = jb.getString("id");

                       SharedPreferences.Editor sharedEdi = shrp.edit();

                       sharedEdi.putString(name, nameStr);
                       sharedEdi.putString(phone, phoneStr);
                       sharedEdi.putString(lnumber, lisenceStr);
                       sharedEdi.putString(userid, getId);
                       sharedEdi.apply();

                       Toast.makeText(getApplicationContext(), "User registration succeed..."+response, Toast.LENGTH_LONG).show();
                   }
                   catch (JSONException e) {
                       e.printStackTrace();
                   }

               }
//           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               progressDialog.dismiss();
               Toast.makeText(getApplicationContext(), "User registration failed", Toast.LENGTH_LONG).show();
           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params = new HashMap<>();
               params.put("userName", nameStr);
               params.put("phoneNumber", phoneStr);
               params.put("lisenceNumber", lisenceStr);
               return params;
           }
       };
       //inseting into  the iteluser table
        RequestQueue requestQueue = Volley.newRequestQueue(userProfile.this);
        requestQueue.add(stringRequest);


        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

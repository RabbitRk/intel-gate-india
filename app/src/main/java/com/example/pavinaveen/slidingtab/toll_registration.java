package com.example.pavinaveen.slidingtab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.pavinaveen.slidingtab.Tab3.wallet;
import static com.example.pavinaveen.slidingtab.userProfile.SHARED_PREFS;
import static com.example.pavinaveen.slidingtab.userProfile.userid;

public class toll_registration extends AppCompatActivity {

    public static final String LOG_TAG = "TTS";
    private TextToSpeech T2S;
    EditText single_or_dual;
    final String welcome_note = "Welcome to IntelGate";
    final String singleordual = "Please tell us the transportation method";
    final String make_Payment= "Do you want to make payment via wallet";
    final String no_valid_cash = "You dont have a valid balance";
    public String user_input = "";
    SharedPreferences shrp;
    SharedPreferences.Editor editor;
    public String walletAmt;
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll);

        single_or_dual = findViewById(R.id.trans_txt);

        T2S = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    int result = T2S.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.d(LOG_TAG, "Language not Supported");
                    }
                    else
                    {
                        speak();

                    }
                }
                else
                {
                    Log.d(LOG_TAG,"Initialization failed");
                }
            }
        });


        shrp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        walletAmt = shrp.getString(wallet,"");
        userID = shrp.getString(userid,"");
//        shrp.getString(,"");
//        shrp.getString(,"");









    }

    private void speak(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                T2S.speak(singleordual,TextToSpeech.QUEUE_FLUSH,null);
            }
        }, 4000);


        T2S.speak(welcome_note,TextToSpeech.QUEUE_FLUSH,null);

        Log.d(LOG_TAG, "inside speak");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSpeechInput();
            }
        }, 5000);

    }
    public void getSpeechInput() {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent, 10);
        }else{
            Toast.makeText(this,"Your Device Don't Support Speech Input",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK&&data!=null){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(result.contains("single"))
                    {
                        Toast.makeText(this, "single", Toast.LENGTH_SHORT).show();
                    }
                    else if (result.contains("double"))
                    {
                        Toast.makeText(this, "double", Toast.LENGTH_SHORT).show();
                    }
                    else if (result.contains(""))
                    {
                        retryVoice();
                    }
                   
                }
                break;
        }
    }

    private void retryVoice() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                T2S.speak(no_valid_cash,TextToSpeech.QUEUE_FLUSH,null);
            }
        },5000);
        T2S.speak(welcome_note,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(T2S!=null){
            T2S.stop();
            T2S.shutdown();
        }
        super.onDestroy();
    }
}

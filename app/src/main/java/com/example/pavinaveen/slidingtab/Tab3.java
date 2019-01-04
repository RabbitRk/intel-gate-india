package com.example.pavinaveen.slidingtab;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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

import static com.example.pavinaveen.slidingtab.userProfile.SHARED_PREFS;
import static com.example.pavinaveen.slidingtab.userProfile.userid;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab3 extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = "Tab3";

    public static final String wallet = "";

    TextView mScoreText1;
    SharedPreferences shrp;
    ProgressDialog progressDialog;
    int mScore1;
    public String OWNERID = "";


    static final String STATE_SCORE_1 = "Team 1 Score";

    private OnFragmentInteractionListener mListener;

    public Tab3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab3.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);

        //Find the TextViews by ID
        mScoreText1 = view.findViewById(R.id.score_1);

        ImageButton decreaseTeam1 = view.findViewById(R.id.decreaseTeam1);
        ImageButton increaseTeam1 = view.findViewById(R.id.increaseTeam1);
        Button submitBtn = view.findViewById(R.id.add_vehicle);


        shrp = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

       // Log.i(LOG_TAG, shrp.getString(wallet,null));

        String walletPref = shrp.getString(wallet,"");

        mScoreText1.setText(walletPref);

        OWNERID = shrp.getString(userid,"");

        Toast.makeText(this.getActivity(),"ID "+ OWNERID+"   wallet   "+walletPref ,Toast.LENGTH_LONG).show();

        decreaseTeam1.setOnClickListener(this);
        increaseTeam1.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //Save the scores
        outState.putInt(STATE_SCORE_1, mScore1);
        super.onSaveInstanceState(outState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        mScore1 = Integer.parseInt(mScoreText1.getText().toString());

        switch (v.getId())
        {
            case R.id.increaseTeam1:
                    mScore1++;
                    mScoreText1.setText(String.valueOf(mScore1));
                break;
            case R.id.decreaseTeam1:
                    mScore1--;
                    mScoreText1.setText(String.valueOf(mScore1));
                break;
            case R.id.add_vehicle:
                AddScorePreference(mScore1);
                Log.i(LOG_TAG,"Score: "+mScore1);
                break;
            default:
                break;
        }
    }

    private void AddScorePreference(final int score) {

        try
        {

            shrp = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedEdi = shrp.edit();
            sharedEdi.putString(wallet, String.valueOf(score));
            sharedEdi.apply();

            Log.i(LOG_TAG,"Inside pref function");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.WALLET_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("failed")) {
                        progressDialog.dismiss();
                        Log.i(LOG_TAG,"response is   "+response);
                        Toast.makeText(getActivity(), "Money added to your wallet", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Money not deposited in your account...Try Again!", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("userid", OWNERID);

                    Log.i(LOG_TAG, "Woner id   "+OWNERID); //Iam log

                    params.put("amount", String.valueOf(score));
                    return params;
                }
            };
            //inseting into  the iteluser table
            RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
            requestQueue.add(stringRequest);


        }
        catch (NullPointerException e)
        {
            Log.i(LOG_TAG,e.getMessage());
            e.printStackTrace();
        }

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.)



    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

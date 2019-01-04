package com.example.pavinaveen.slidingtab;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.pavinaveen.slidingtab.userProfile.SHARED_PREFS;
import static com.example.pavinaveen.slidingtab.userProfile.userid;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = "Tab_1";
    private static final int READ_BLOCK = 100;

    public static final String VEHICLE_PREFS = "veh_prefs";
    public static final String VEHICLE_LIST[] = new String[100];
    public static final String VEHICLE_ITEMS = "vehlist";

    SharedPreferences shrp;
    SharedPreferences vshrp;

  // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Tab1 context = this;

    private OnFragmentInteractionListener mListener;

    //Rk declaration
    public String USERID = "";
    String message;
    public ArrayAdapter<String> arrayAdapter;
    public ListView listView;
    public ArrayList<String> theList = new ArrayList<>();
    DatabaseHelper myDB;
    //Rk declaration end

    public Tab1() {
        // Required empty public constructor

    }

    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        //open the dialog box
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton2);

        //declaration
        listView = view.findViewById(R.id.listView);
        listView.setSelector(R.drawable.itemselector);

        listView.setOnItemLongClickListener(this);


        myDB = new DatabaseHelper(getActivity());

        //populate an ArrayList<String> from the database and then view it
        //get the data which is previously stored
        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(getActivity(), "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(arrayAdapter);
            }
        }


        //onclick listener
        floatingActionButton.setOnClickListener(this);

        //get user id preferences
        shrp = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        USERID = shrp.getString(userid,"");
        Log.i(LOG_TAG,"this user id "+USERID);


        return view;
    }




    private void ShowDialog() {

        final Dialog dialog = new Dialog(Objects.requireNonNull(this.getContext()));

        dialog.setContentView(R.layout.dialog);
        dialog.show();

        Button add_vehicle = dialog.findViewById(R.id.add_vehicle);
        final EditText vechicleNumber = dialog.findViewById(R.id.VehicleNumber);
        final EditText chasisNumber = dialog.findViewById(R.id.ChasisNumber);

        add_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String vehicleTxt = vechicleNumber.getText().toString();
                final String chasisTxt = chasisNumber.getText().toString();

                if (!vehicleTxt.equals("") || !chasisTxt.equals("")) {

                    //add vehicle to the list
                    theList.add(vehicleTxt);

                    //add vehicle number in the sql database
                    addVehicle(vehicleTxt);

                    //Store in the database using php and mysql
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SET_VEHICLE_LIST, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Toast.makeText(getActivity(), "Vehicle successfully added", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Vehicle number already exists", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Vehicle is not added", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("id", USERID);
                            params.put("vehiclenumber", vehicleTxt);
                            params.put("chasenumber", chasisTxt);
                            return params;
                        }
                    };
                    //inseting into  the iteluser table
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getActivity(),"Please provide a vehicle number",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addVehicle(String vehicleTxt) {
        boolean insertData = myDB.addData(vehicleTxt);

        if(insertData){
            Toast.makeText(getActivity(), "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.floatingActionButton2:
                ShowDialog();
                break;
            case R.id.listView:
                ShowDialog();
                break;

        }
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
    }





}

package chainofvolunteers.chainofvolunteers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chainofvolunteers.chainofvolunteers.AppHelpers.JSONAdapter;
import chainofvolunteers.chainofvolunteers.AppHelpers.Route;
import chainofvolunteers.chainofvolunteers.AppHelpers.Segment;

public class UserActivity extends AppCompatActivity  implements OnMapReadyCallback {

    static String start_point = "1234 Parkmont Ct. San Jose";
    static String end_point = "1169 Robalo Ct. San Jose";
    private JSONObject jo;
    private EditText mStartingLocation;
    private EditText mEndingLocation;
    String userName;
    Route r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_mission);
//        Bundle extras = getIntent().getExtras();
//        if(extras == null) {
//            userName= null;
//        } else {
//            userName = extras.getString("userName");
//        }
            SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mStartingLocation = (EditText)findViewById(R.id.startinglocation);
        mEndingLocation = (EditText)findViewById(R.id.endinglocation);

        //when click
        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_point = mStartingLocation.getText().toString();
                end_point = mEndingLocation.getText().toString();
                if(start_point.equals("") | end_point.equals("")) {
                    //display an error message and ask them to input another address?
                } else {
                    new HandleJSON().execute("");
                }


            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(42.3601, -71.0942)).title("Marker"));
    }


    private class HandleJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            jo = JSONAdapter.getJson(start_point, end_point);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            handleJson();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void handleJson() {
        try {
            JSONObject jo2 = jo.getJSONArray("routes").getJSONObject(0).
                    getJSONArray("legs").getJSONObject(0).getJSONObject("start_location");
            LatLng start = new LatLng(jo.getJSONArray("routes").getJSONObject(0).
                    getJSONArray("legs").getJSONObject(0).getJSONObject("start_location").getDouble("lat"),
                    jo.getJSONArray("routes").getJSONObject(0).
                            getJSONArray("legs").getJSONObject(0).getJSONObject("start_location").getDouble("long"));
            LatLng end = new LatLng(jo.getJSONArray("routes").getJSONObject(0).
                    getJSONArray("legs").getJSONObject(0).getJSONObject("end_location").getDouble("lat"),
                    jo.getJSONArray("routes").getJSONObject(0).
                            getJSONArray("legs").getJSONObject(0).getJSONObject("end_location").getDouble("long"));
            r.distributeRoute(start, end, "user"); //TODO: need to change to username...
        } catch (JSONException e) {
        e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}

package chainofvolunteers.chainofvolunteers;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    GoogleMap map;

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
        this.map=map;
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
        r = Route.GetInstance();
        try {
            JSONObject jo2 = jo.getJSONArray("routes").getJSONObject(0).
                    getJSONArray("legs").getJSONObject(0).getJSONObject("start_location");
            LatLng start = new LatLng((Double) jo.getJSONArray("routes").getJSONObject(0).
                    getJSONArray("legs").getJSONObject(0).getJSONObject("start_location").get("lat"),
                    (Double) jo.getJSONArray("routes").getJSONObject(0).
                            getJSONArray("legs").getJSONObject(0).getJSONObject("start_location").get("lng"));
            LatLng end = new LatLng((Double) jo.getJSONArray("routes").getJSONObject(0).
                    getJSONArray("legs").getJSONObject(0).getJSONObject("end_location").get("lat"),
                    (Double) jo.getJSONArray("routes").getJSONObject(0).
                            getJSONArray("legs").getJSONObject(0).getJSONObject("end_location").get("lng"));
            r.jsonToList(jo); //TODO DELETE
            r.distributeRoute(start, end, "user"); //TODO: need to change to username...
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Segment> segmentList = r.getList();
        PolylineOptions lineOptions = null;
        lineOptions = new PolylineOptions();
        // Traversing through all the routes
        for (int i = 0; i < segmentList.size(); i++) {
//            points = new ArrayList<>();


            // Fetching i-th route
            //Segment path = segmentList.get(i);

            // Fetching all the points in i-th route
            HashMap<String, String> point = new HashMap<>();

            double lat = segmentList.get(i).startPoint.latitude;
            double lng = segmentList.get(i).startPoint.longitude;
            LatLng position1 = new LatLng(lat, lng);

            double lat2 = segmentList.get(i).endPoint.latitude;
            double lng2 = segmentList.get(i).endPoint.longitude;
            LatLng position2 = new LatLng(lat2, lng2);

            //points.add(position);


            // Adding all the points in the route to LineOptions
            lineOptions.add(position1);
            lineOptions.add(position2);
            lineOptions.width(10);
            if (segmentList.get(i).user == null)
                lineOptions.color(Color.RED);
            else
                lineOptions.color(Color.GREEN);

            Log.d("onPostExecute", "onPostExecute lineoptions decoded");
        }
        map.addPolyline(lineOptions);
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}

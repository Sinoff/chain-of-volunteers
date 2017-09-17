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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.lang.StringBuffer;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import chainofvolunteers.chainofvolunteers.AppHelpers.JSONAdapter;
import chainofvolunteers.chainofvolunteers.AppHelpers.Route;
import chainofvolunteers.chainofvolunteers.AppHelpers.Segment;

public class SubmitMissionActivity extends AppCompatActivity implements OnMapReadyCallback {

    static String start_point = "1234 Parkmont Ct. San Jose";
    static String end_point = "1169 Robalo Ct. San Jose";
    private JSONObject jo;
    private EditText mStartingLocation;
    private EditText mEndingLocation;
    Route r;
    ArrayList<Segment> segmentList;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_mission);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mStartingLocation = (EditText) findViewById(R.id.startinglocation);
        mEndingLocation = (EditText) findViewById(R.id.endinglocation);


        //when click
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onClick");
                start_point = mStartingLocation.getText().toString();
                end_point = mEndingLocation.getText().toString();
                System.out.println("Starting pointing: " + start_point);
                System.out.println("Ending point: " + end_point);
                if (start_point == "" | end_point == "") {
                    //display an error message and ask them to input another address?
                } else {
                    new HandleJSON().execute();


                }


            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.addMarker(new MarkerOptions().position(new LatLng(42.3601, -71.0942)).title("Marker"));
    }


    protected void onPostExecute(List<Segment> segments) {
        System.out.println(segments);
//        ArrayList<LatLng> points;
//        PolylineOptions lineOptions = null;
//
//        // Traversing through all the routes
//        for (int i = 0; i < result.size(); i++) {
//            points = new ArrayList<>();
//            lineOptions = new PolylineOptions();
//
//            // Fetching i-th route
//            List<HashMap<String, String>> path = result.get(i);
//
//            // Fetching all the points in i-th route
//            for (int j = 0; j < path.size(); j++) {
//                HashMap<String, String> point = path.get(j);
//
//                double lat = Double.parseDouble(point.get("lat"));
//                double lng = Double.parseDouble(point.get("lng"));
//                LatLng position = new LatLng(lat, lng);
//
//                points.add(position);
//            }
//
//            // Adding all the points in the route to LineOptions
//            lineOptions.addAll(points);
//            lineOptions.width(10);
//            lineOptions.color(Color.RED);
//
//            Log.d("onPostExecute","onPostExecute lineoptions decoded");

//        }
//
//        // Drawing polyline in the Google Map for the i-th route
//        if(lineOptions != null) {
//
//            mMap.addPolyline(lineOptions);
//        }
//        else {
//            Log.d("onPostExecute","without Polylines drawn");
//        }
    }


    private class HandleJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("segment hello");
            System.out.println("segment hello");
            jo = JSONAdapter.getJson(start_point, end_point);
            return "Executed";

        }

        @Override
        protected void onPostExecute(String result) {
            handleJson();


//        // Drawing polyline in the Google Map for the i-th route
//        if(lineOptions != null) {
//
//            mMap.addPolyline(lineOptions);
//        }
//        else {
//            Log.d("onPostExecute","without Polylines drawn");
//        }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void handleJson() {
        System.out.println("handle json");
        r = Route.GetInstance();
        r.jsonToList(jo);
        segmentList = r.getList();
        System.out.println("segment hi" + segmentList.size());
        ArrayList<LatLng> points;
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
            if (segmentList.get(i).user ==null)
                lineOptions.color(Color.RED);
            else
                lineOptions.color(Color.GREEN);

            Log.d("onPostExecute", "onPostExecute lineoptions decoded");

        }
        map.clear();
        map.addPolyline(lineOptions);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}

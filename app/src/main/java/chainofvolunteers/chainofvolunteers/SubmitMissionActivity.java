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

import org.json.JSONException;
import org.json.JSONObject;

import chainofvolunteers.chainofvolunteers.AppHelpers.JSONAdapter;
import chainofvolunteers.chainofvolunteers.AppHelpers.Route;

public class SubmitMissionActivity extends AppCompatActivity implements OnMapReadyCallback{

    static String start_point = "1234 Parkmont Ct. San Jose";
    static String end_point = "1169 Robalo Ct. San Jose";
    private JSONObject jo;
    private EditText mStartingLocation;
    private EditText mEndingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_mission);
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
                new HandleJSON().execute("");

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
        Route r = Route.GetInstance();
        r.jsonToList(jo);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}

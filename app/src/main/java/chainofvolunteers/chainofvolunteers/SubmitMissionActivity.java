package chainofvolunteers.chainofvolunteers;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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


    private String  startPoint;
    private String endPoint;
    private JSONObject jo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_mission);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(42.3601, -71.0942)).title("Marker"));
        startPoint = "1234 Parkmont Ct. San Jose";
        endPoint = "1169 Robalo Ct. San Jose";
        new HandleJSON().execute("");


    }


    private class HandleJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            jo = JSONAdapter.getJson(startPoint, endPoint);
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
        Route r = new Route();
        r.jsonToList(jo);
    }
}

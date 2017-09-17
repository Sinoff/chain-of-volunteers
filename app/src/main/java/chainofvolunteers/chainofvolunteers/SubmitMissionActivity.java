package chainofvolunteers.chainofvolunteers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class SubmitMissionActivity extends AppCompatActivity {


    private static final String USER_AGENT = "Mozilla/5.0";

    static String start_point = "1234 Parkmont Ct. San Jose";
    static String end_point = "1169 Robalo Ct. San Jose";
    private static final String UTF8 = StandardCharsets.UTF_8.toString();

    //static String maps_api_key = "AIzaSyCpuojrKYVJG2xuz7Crgw2rv11ueftv4_M";
    static String directions_api_key = "AIzaSyAaSm6dUhCQ3cP_507jtiLWB3UL6I3H4TA";

    public static URL getFullURL(String start_point, String end_point) {
        String start_point_url = null;
        String end_point_url = null;
        try {
            start_point_url = URLEncoder.encode(start_point, UTF8);
            end_point_url = URLEncoder.encode(end_point, UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        URL url = null;
        try {
            url = new URL("https://maps.googleapis.com/maps/api/directions/json?key="
                    + directions_api_key + "&origin=" + start_point_url + "&destination=" + end_point_url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    /**
     * call this function and it returns the JSON Object
     */

    public static JSONObject getJson(String start_point, String end_point) {
        URL url = getFullURL(start_point, end_point);
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return new JSONObject(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_mission);
    }

}

package chainofvolunteers.chainofvolunteers.AppHelpers;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by rssinoff on 9/17/2017.
 */

public class JSONAdapter {

    private static final String UTF8 = StandardCharsets.UTF_8.toString();

    //static String maps_api_key = "AIzaSyCpuojrKYVJG2xuz7Crgw2rv11ueftv4_M";
    static String directions_api_key = "AIzaSyAaSm6dUhCQ3cP_507jtiLWB3UL6I3H4TA";

    private static final String USER_AGENT = "Mozilla/5.0";


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
}

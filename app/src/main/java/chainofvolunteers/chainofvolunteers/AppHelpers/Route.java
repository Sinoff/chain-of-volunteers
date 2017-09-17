package chainofvolunteers.chainofvolunteers.AppHelpers;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.LinkedList;



/**
 * Created by madeline on 9/16/17.
 */

public class Route {

    LinkedList<Segment> segments;

    public Route ()
    {
        segments = new LinkedList<>();
    }

    public void jsonToList(JSONObject obj)
    {
        JSONArray jsonSegments = null;
        try {
            jsonSegments = obj.getJSONArray("routes").getJSONArray(3);
            for (int i=0; i < jsonSegments.length(); i++)
            {
                segments.add(new Segment
                        (new LatLng(
                                jsonSegments.getJSONObject(i).getJSONObject("start_location").getDouble("lat"),
                                jsonSegments.getJSONObject(i).getJSONObject("start_location").getDouble("lng"))
                                ,
                        new LatLng(
                                jsonSegments.getJSONObject(i).getJSONObject("end_location").getDouble("lat"),
                                jsonSegments.getJSONObject(i).getJSONObject("end_location").getDouble("lng"))
                        ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
    }
    // typecasting obj to JSONObject
    JSONObject jo = (JSONObject) obj;
}

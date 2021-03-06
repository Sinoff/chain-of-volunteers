package chainofvolunteers.chainofvolunteers.AppHelpers;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;


/**
 * Created by madeline on 9/16/17.
 */

public class Route {

    ArrayList<Segment> segments;
    private static Route r;
    //MAKING IT SINGELTON FOR COMPETITION ONLY
    public static Route GetInstance()
    {
        if (r==null) {
            Route r = new Route();
            return r;
        }
        else
            return r;
    }
    protected Route ()
    {
        segments = new ArrayList<>();
    }

    public ArrayList<Segment> getList()
    {
        return segments;
    }
    public Route distributeRoute(LatLng start, LatLng end, String userName) {
        int s;
        for (s = 0; s < segments.size(); s++)     //get first segment
        {
            Segment currSegment = (Segment) segments.get(s);
            if (start.equals(currSegment.startPoint)) {
                currSegment.markSegmentTaken(userName);
                break;
            }
        }
        for (; s < segments.size(); s++) //add all other segments
        {
            Segment currSegment = (Segment) segments.get(s);
            if (!end.equals(currSegment.endPoint)) {
                currSegment.markSegmentTaken(userName);
            }
            else
            {
                currSegment.markSegmentTaken(userName);
                break;
            }
        }
        return this;
    }

    public void jsonToList(JSONObject obj)
    {
        try {
            JSONArray jsonSegments = obj.getJSONArray("routes").getJSONObject(0).
                    getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            int s = jsonSegments.length();
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

}

//returns list of segments-which is a start point and end point in a flat.
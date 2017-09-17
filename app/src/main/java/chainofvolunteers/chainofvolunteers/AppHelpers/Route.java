package chainofvolunteers.chainofvolunteers.AppHelpers;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;


/**
 * Created by madeline on 9/16/17.
 */

public class Route {

    ArrayList<Segment> segments;
    Route r = new Route();

    public Route ()
    {
        segments = new ArrayList<>();
    }

    public Route distributeRoute(LatLng start, LatLng end, String userName) {
        int s = 0;
        //Route r = new Route(); //moved up - probably can delete?
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


//    public double getSegmentDistance(Segment s)
//    {
//        return Math.sqrt(Math.pow((s.startPoint.latitude - s.endPoint.latitude),2) + Math.sqrt(Math.pow((s.startPoint.latitude - s.endPoint.latitude),2)));
//    }

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

}

//returns list of segments-which is a start point and end point in a flat.
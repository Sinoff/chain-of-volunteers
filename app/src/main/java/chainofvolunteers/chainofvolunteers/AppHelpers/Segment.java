package chainofvolunteers.chainofvolunteers.AppHelpers;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by madeline on 9/16/17.
 */

public class Segment {

    public LatLng      startPoint;
    public LatLng      endPoint;
    public String      user;

    public Segment(LatLng start, LatLng end)
    {
        startPoint = start;
        endPoint = end;
    }

    public void markSegmentTaken(String u)
    {
        user = u;
    }
}

package chainofvolunteers.chainofvolunteers.AppHelpers;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by madeline on 9/16/17.
 */

public class Segment {

    LatLng      startPoint;
    LatLng      endPoint;

    public Segment(LatLng start, LatLng end)
    {
        startPoint = start;
        endPoint = end;
    }
}

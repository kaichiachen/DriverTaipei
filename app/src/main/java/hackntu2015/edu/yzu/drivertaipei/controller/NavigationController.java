package hackntu2015.edu.yzu.drivertaipei.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by andy on 8/25/15.
 */
public class NavigationController {

    public static void navigate(Context ctx, final LatLng startLocation, final LatLng endLocation){
        String saddr = "saddr=" + startLocation.latitude + "," + startLocation.longitude;
        String daddr = "daddr=" + endLocation.latitude + "," + endLocation.longitude;
        String uriString = "http://maps.google.com/maps?" + saddr + "&" + daddr;
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        ctx.startActivity(intent);
    }
}

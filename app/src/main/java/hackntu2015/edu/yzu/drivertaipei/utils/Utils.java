package hackntu2015.edu.yzu.drivertaipei.utils;

import android.content.Context;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utils {

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt=new Date();
        String dts=sdf.format(dt);
        return dts;
    }

    public static boolean isNight(){
        SimpleDateFormat sdf=new SimpleDateFormat("HH", Locale.getDefault());
        sdf.format(new Date());
        if(6 < Integer.parseInt(sdf.format(new Date())) && Integer.parseInt(sdf.format(new Date())) < 18) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isGPSEnabled(Context context){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static int getWindowHeight(WindowManager manager){
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getWindowWidth(WindowManager manager){
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}

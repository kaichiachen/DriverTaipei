package hackntu2015.edu.yzu.drivertaipei.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt=new Date();
        String dts=sdf.format(dt);
        return dts;
    }
}

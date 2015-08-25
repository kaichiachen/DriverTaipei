package hackntu2015.edu.yzu.drivertaipei.controller;

import android.os.Handler;

/**
 * Created by andy on 8/25/15.
 */
public class DataUpdate extends Handler{

    Runnable construct;

    int constructInterval = 5 * 60 * 1000;

    public void updateConstruct(){
        construct = new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().downloadConstructData();
                postDelayed(this,constructInterval);
            }
        };
        postDelayed(construct, constructInterval);
    }

    public void removeUpdate(){
        this.removeCallbacks(construct);
    }
}

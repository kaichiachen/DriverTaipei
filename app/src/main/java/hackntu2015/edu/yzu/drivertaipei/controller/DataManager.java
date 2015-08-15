package hackntu2015.edu.yzu.drivertaipei.controller;

/**
 * Created by andy on 8/15/15.
 */
public class DataManager {
    private final String TAG = "DataManager";

    private volatile static DataManager mInstance;
    private DataController mCentralController = null;

    public static DataManager getInstance() {
        if (mInstance == null) {
            synchronized (DataManager.class) {
                if (mInstance == null) {
                    mInstance = new DataManager();
                }
            }
        }
        return mInstance;
    }

    DataManager() {
        mCentralController = new DataController();
    }

    public void setListener(DataListener l){
        mCentralController.setListener(l);
    }

    public void updateData(){
        mCentralController.updateData();
    }


}

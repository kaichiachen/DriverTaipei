package hackntu2015.edu.yzu.drivertaipei.utils;

/**
 * Created by andy on 8/15/15.
 */
public enum ErrorCode {
    ERR_JSON(0,"JSON Key Wrong!"),
    ERR_WRONGURL(1,"URL is Wrong!");

    private final int mCode;
    private final String mDescription;
    private ErrorCode(int code, String des) {
        this.mCode = code;
        this.mDescription = des;
    }

    public String toString(){
        return mCode + " : " + mDescription;
    }
}

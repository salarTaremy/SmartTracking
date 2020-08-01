package smart.tracking.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyLocation {


    public MyLocation() {
    }

    @SerializedName("imei")
    @Expose
    public String IMEI;

    @SerializedName("provider")
    @Expose
    public String Provider;

    @SerializedName("longitude")
    @Expose
    public double Longitude;

    @SerializedName("latitude")
    @Expose
    public double Latitude;

    @SerializedName("altitude")
    @Expose
    public double Altitude;

    @SerializedName("UTCtime")
    @Expose
    public long UTCtime;

    @SerializedName("time")
    @Expose
    public short Time;

    @SerializedName("accuracy")
    @Expose
    public float Accuracy;

    @SerializedName("speed")
    @Expose
    public float Speed;

    @SerializedName("bearing")
    @Expose
    public float Bearing;



    @SerializedName("batteryLevel")
    @Expose
    public int BatteryLevel;

}


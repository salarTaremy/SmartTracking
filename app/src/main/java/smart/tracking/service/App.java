package smart.tracking.service;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


public class App extends Application {
    private static Context context;
    public static final String CHANNEL_ID = "SmartTrackingServiceChannel";
    public static final String ConfigName = "Config";
    public  static  final  int ApiVersionForManualIMEI = Build.VERSION_CODES.Q;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        createNotificationChannel();
    }

    public static Context getAppContext() {
        return App.context;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Smart Tracking Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    public  String GetManualIMEI( ){
        try {
            final SharedPreferences shPref =  context.getSharedPreferences(App.ConfigName,Context.MODE_PRIVATE);
            String IMEI = shPref.getString(ConfigName, null);
            return  IMEI;
        }catch (Exception ex){
            return  null;
        }
    }

    public String IMEI( ) {
        try {
            if (Build.VERSION.SDK_INT < ApiVersionForManualIMEI ) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
                String imei = telephonyManager.getDeviceId();
                if (imei != null && !imei.isEmpty()) {
                    return imei;
                } else {
                    return android.os.Build.SERIAL;
                }
            }else {
                return  GetManualIMEI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }


}
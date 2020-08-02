package smart.tracking.service;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;


public class App extends Application {
    private static Context context;
    public static final String CHANNEL_ID = "SmartTrackingServiceChannel";
    public static final String ConfigName = "Config";
    public  static  final  int ApiVersionForManualIMEI = Build.VERSION_CODES.Q;

    public static float getBatteryLevel() {
        Float BatteryLevel = 100.0f;
        try {
            String name = "BatteryLevel";
            final SharedPreferences shPref =  App.getAppContext().getSharedPreferences(name,Context.MODE_PRIVATE);
            String val = shPref.getString(name, null);
            BatteryLevel =   Float.valueOf(val) ;
        } catch (Exception ex){
            BatteryLevel = 0.0f ;
        }
        return  BatteryLevel;
    }

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



    public static boolean isMockSettingsON() {
        try {
            // returns true if mock location enabled, false if not enabled.
            if (Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
                return false;
            else
                return true;
        } catch ( Exception ex){
             return  true;
        }
    }


    public static int areThereMockPermissionApps() {
        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception " , e.getMessage());
            }
        }
        return  count;
    }


}
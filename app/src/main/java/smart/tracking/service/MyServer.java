package smart.tracking.service;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import java.util.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServer {

    private static void SendLocationToServer(MyLocation Location) {
        TrackingServiceAPI API = APIClient.getClient().create(TrackingServiceAPI.class);
        API.save(Location).enqueue(new Callback<MyLocation>() {
            @Override
            public void onResponse(Call<MyLocation> call, Response<MyLocation> response) {
                try {
                    if (response.isSuccessful()) {
                        MyLocation t = response.body();
                        Logcat.Send("Api Ok : " +  t.IMEI);
                    } else {
                        Logcat.e("Api Error Not Successful: " + response.errorBody().string());
                    }
                } catch (Exception e) {
                    Logcat.e("Api Error Exception : " + e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<MyLocation> call, Throwable t) {
                Logcat.e("Api Error Failure : " + t.getMessage());
            }
        });
    }

    public static void SendMyLocation(Location location) {
        Logcat.Send("Sending...");
        String str =   location.getProvider() +" : "+String.valueOf( location.getLatitude())+","+ String.valueOf( location.getLongitude());
        Logcat.Send(str);
        java.util.Date currentTime        = Calendar.getInstance().getTime();
        String HH , MM ;
        HH=  String.valueOf(Calendar.getInstance().getTime().getHours());
        MM=  String.valueOf(Calendar.getInstance().getTime().getMinutes());
        if ( MM.length() == 1 ){
            MM = "0" + MM;
        }

        MyLocation myLocation   =new MyLocation();
        myLocation.IMEI         = new  App().IMEI();
        myLocation.Provider     =location.getProvider();
        myLocation.Longitude    =location.getLongitude();
        myLocation.Latitude     =location.getLatitude();
        myLocation.Altitude     =location.getAltitude();
        myLocation.Bearing      =location.getBearing();
        myLocation.UTCtime      =location.getTime();
        myLocation.Time         =Short.valueOf(HH+MM);
        myLocation.Accuracy     =location.getAccuracy();
        myLocation.Speed        =location.getSpeed();
        myLocation.BatteryLevel = App.getBatteryLevel();
        SendLocationToServer(myLocation);
        //Logcat.Send("Complate ...");
        //new MyLocationDbHelper(getApplicationContext()).create(myLocation);
    }


}

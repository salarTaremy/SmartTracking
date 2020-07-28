package smart.tracking.service.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import smart.tracking.service.Logcat;
import smart.tracking.service.Services.LocationService;

public class AutoStart extends BroadcastReceiver
{
    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        Logcat.Send("Smart Tracking Service Started Automatic ...");
    }
}
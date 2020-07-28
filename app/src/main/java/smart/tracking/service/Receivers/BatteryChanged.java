package smart.tracking.service.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import smart.tracking.service.App;
import smart.tracking.service.Logcat;
import smart.tracking.service.R;

public class BatteryChanged extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        Save(level);
    }

    private void Save(int level) {
        String name = "BatteryLevel";
        final SharedPreferences shPref =  App.getAppContext().getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor sEdit = shPref.edit();
        sEdit.putString(name, String.valueOf(level));
        sEdit.apply();
        String val = shPref.getString(name, null);
        Logcat.i("new val is : " +val);
    }
}

package smart.tracking.service;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import smart.tracking.service.Finder.LocationFinder;
import smart.tracking.service.Receivers.BatteryChanged;
import smart.tracking.service.Receivers.MyAdmin;

public class MainActivity extends AppCompatActivity {

    Context context;
    int PERMISSION_ALL = 1;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName SmartTrackingComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_status, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        GetPermission();
        CheckImei();
        Load();
        //for register BATTERY_CHANGED Broadcast Receiver ( because can not register it from manifest) :
        this.registerReceiver(new BatteryChanged(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void CheckImei() {
        if (Build.VERSION.SDK_INT < App.ApiVersionForManualIMEI) {
            return;
        }
        final SharedPreferences shPref = getSharedPreferences(App.ConfigName,Context.MODE_PRIVATE);
        if (shPref.contains(App.ConfigName)) {
            String IMEI = new App().GetManualIMEI();
            Toast.makeText(   context,  " Q Api IMEI Is : " + IMEI,Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            final View dialogView = LayoutInflater.from(context).inflate(R.layout.input_imei, null);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.show();
            final EditText tv_input_imei = dialogView.findViewById(R.id.tv_input_imei);

            Button btn_confirm_imei = dialogView.findViewById(R.id.btn_confirm_imei);
            btn_confirm_imei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String IMEI = tv_input_imei.getText().toString().trim();
                    SharedPreferences.Editor sEdit = shPref.edit();
                    sEdit.putString(App.ConfigName, IMEI);
                    sEdit.apply();
                    Toast.makeText(   context,  "IMEI Is  Saved for Q Api",Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            });
        }
    }

    private void EnableDeviceAdmin() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        SmartTrackingComponent = new ComponentName(this, MyAdmin.class);

        if (IsDeviceAdmin(SmartTrackingComponent) == false){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, SmartTrackingComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "جهت کارکرد صحیح اپلیکیشن این گزینه ختما باید فعال شود.");
            startActivityForResult(intent, RESULT_ENABLE);
        }
        // for Disable Device Admin:
        // devicePolicyManager.removeActiveAdmin(SmartTrackingComponent);
    }
    private void DisableDeviceAdmin() {
        devicePolicyManager.removeActiveAdmin(SmartTrackingComponent);
    }
    private boolean IsDeviceAdmin(ComponentName smartTrackingComponent) {
        boolean active = devicePolicyManager.isAdminActive(smartTrackingComponent);
        return  active;
    }

    private void GetPermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.INTERNET
                        , Manifest.permission.VIBRATE
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.CALL_PHONE
                        , Manifest.permission.INTERNET
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_NETWORK_STATE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        , Manifest.permission.LOCATION_HARDWARE
                        , Manifest.permission.FOREGROUND_SERVICE
                        , Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
                }, PERMISSION_ALL);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            if ( grantResults[2] < 0 ){
                Logcat.e("READ_PHONE_STATE Permission required ... ") ;
                finish();
            }
            if ( grantResults[4] < 0 ){
                Logcat.e("INTERNET Permission required ... ") ;
                finish();
            }
            if ( grantResults[5] < 0 ){
                Logcat.e("ACCESS_COARSE_LOCATION Permission required ... ") ;
                finish();
            }
            if ( grantResults[6] < 0 ){
                Logcat.e("ACCESS_FINE_LOCATION Permission required ... ") ;
                finish();
            }
            if ( grantResults[2] < 0 ){
                Logcat.e("READ_PHONE_STATE Permission required ... ") ;
                finish();
            }
            if ( grantResults[11] < 0  && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                Logcat.Send("FOREGROUND_SERVICE Permission for api>(28) is required");
                finish();
            }
            EnableDeviceAdmin();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Load() {
        if ( LocationHelper.isGPSEnabled(context) == false){
            new LocationFinder(context).showSettingsAlert();
        }
    }

}

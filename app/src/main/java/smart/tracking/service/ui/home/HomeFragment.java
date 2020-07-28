package smart.tracking.service.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import smart.tracking.service.Logcat;
import smart.tracking.service.R;
import smart.tracking.service.Services.LocationService;
import smart.tracking.service.Services.ServiceStatus;

public class HomeFragment extends Fragment {
    Context context;
    Switch Sw;
    ImageView Img ;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        InitView(root);
        SetListener();
        CheckService();
        return root;
    }
    private void CheckService() {
        boolean IsRunning =  ServiceStatus.isMyServiceRunning(this.context, LocationService.class);
        if (IsRunning){
            Img.setImageResource(R.drawable.connected);
            this.Sw.setChecked(true);
            this.Sw.setEnabled( false);
        }else {
            Img.setImageResource(R.drawable.disconnect);
            this.Sw.setChecked(false);
            this.Sw.setEnabled( true);
        }
    }

    private void InitView(View root) {
        this.context = this.getActivity();
        this.Sw = root.findViewById(R.id.switch1);
        this.Img =  root.findViewById(R.id.imageViewHome);
    }

    private void SetListener() {
        this.Sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SetService(true);
                    Sw.setEnabled( false);
                }else {
                    SetService(false);
                }
            }
        });
    }

    private void SetService(boolean status) {
        Intent intent = new Intent(context,LocationService.class);
        intent.putExtra("inputExtra", "Smart Tracking Service Is Running");
        boolean IsRunning =  ServiceStatus.isMyServiceRunning(this.context, LocationService.class);
        String Msg;
        if (  status == true){
            if (IsRunning == false){
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    Logcat.Send("startForegroundService");
//                    context.startForegroundService(intent);
//                    //ContextCompat.startForegroundService(this, intent);
//                    //this.startService(intent);
//                } else {
//                    Logcat.Send("startService");
//                    context.startService(intent);
//                }
                ContextCompat.startForegroundService(context ,intent );
                Msg = getResources().getString(R.string.ServiceActived);
                Toast.makeText(context,Msg,Toast.LENGTH_SHORT).show();
            }
        }else {
            if (IsRunning){
                context.stopService(intent);
                Msg = getResources().getString(R.string.ServiceDeactivated);
                Toast.makeText(context,Msg,Toast.LENGTH_SHORT).show();
            }
        }
        CheckService();
    }
}
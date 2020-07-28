package smart.tracking.service.ui.status;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import smart.tracking.service.Finder.LocationFinder;
import smart.tracking.service.LocationHelper;
import smart.tracking.service.R;

public class StatusFragment extends Fragment {
   Context context;
    TextView  TxtCurrentLocationValue,TxtNetworkStatusValue,TxtGpsStatusValue;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_status, container, false);
        IntView(root);
        SetValues();
        return root;
    }

    private void IntView(View root) {
        this.TxtCurrentLocationValue  =   root.findViewById(R.id.TxtCurrentLocationValue);
        this.TxtNetworkStatusValue  =   root.findViewById(R.id.TxtNetworkStatusValue);
        this.TxtGpsStatusValue  =   root.findViewById(R.id.TxtGpsStatusValue);
    }

    private void SetValues() {
        if ( LocationHelper.isGPSEnabled(context)){
            this.TxtGpsStatusValue.setText(R.string.enable);
        }
        else {
            this.TxtGpsStatusValue.setText(R.string.disable);
        }
        if ( LocationHelper.isNetworkEnabled(context)){
            this.TxtNetworkStatusValue.setText(R.string.enable);
        }
        else {
            this.TxtNetworkStatusValue.setText(R.string.disable);
        }
        // get Current location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LocationFinder finder;
            double longitude = 0.0, latitude = 0.0;
            finder = new LocationFinder(context);
            if (finder.canGetLocation()) {
                latitude = finder.getLatitude();
                longitude = finder.getLongitude();
                this.TxtCurrentLocationValue.setText(latitude+" , "+longitude);
            } else {
                finder.showSettingsAlert();
            }
        } else {
            this.TxtCurrentLocationValue.setText(R.string.NotAvailable);
        }
    }
}
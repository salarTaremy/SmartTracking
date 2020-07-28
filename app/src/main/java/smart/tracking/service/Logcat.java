package smart.tracking.service;

import android.util.Log;

public class Logcat {
    public static void Send(String salar) {
        if(salar != null) {
            Log.i("salar" , salar);
        }
    }
    public static void i(String salar) {
        if(salar != null) {
            Log.i("salar" , salar);
        }
    }
    public static void d(String salar) {
        if(salar != null) {
            Log.d("salar" , salar);
        }
    }
    public static void e(String salar) {
        if(salar != null) {
            Log.e("salar" , salar);
        }
    }
    public static void v(String salar) {
        if(salar != null) {
            Log.v("salar" , salar);
        }
    }
    public static void w(String salar) {
        if(salar != null) {
            Log.w("salar" , salar);
        }
    }

}

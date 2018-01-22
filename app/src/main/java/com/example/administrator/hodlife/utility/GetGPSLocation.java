package com.example.administrator.hodlife.utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.hodlife.screen.HomeActivity;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 1/19/2018.
 */

public class GetGPSLocation implements LocationListener {

    LocationManager mLocationManager;
    private Context mContext;
    private static String addStr;
    private static double mLatitude = 0, mLongitude = 0;
    private GPSInterface mInterface;
    public GetGPSLocation(Context context, GPSInterface mInterface){
        this.mContext = context;
        this.mInterface = mInterface;
        getLocation();
    }
    public static double getLatitude() {
        return mLatitude;
    }

    public static double getLongitude() {
        return mLongitude;
    }
    public static String getAddress(){
        return addStr;
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            addStr =   "\n"+addresses.get(0).getAddressLine(0);
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            mInterface.onCurrentLocation(mLatitude, mLongitude, addStr);

        }catch(Exception e)
        {

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(mContext, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }
    private void getLocation(){

        try {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }catch (SecurityException ex){
            ex.printStackTrace();
        }
    }
}

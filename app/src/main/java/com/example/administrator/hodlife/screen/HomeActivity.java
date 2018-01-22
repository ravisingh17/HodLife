package com.example.administrator.hodlife.screen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.hodlife.fragment.AboutFragment;
import com.example.administrator.hodlife.fragment.CityFragment;
import com.example.administrator.hodlife.fragment.HomeFragment;
import com.example.administrator.hodlife.fragment.SettingsFragment;
import com.example.administrator.hodlife.fragment.WeekWeatherFragment;
import com.example.administrator.hodlife.model.Sys;
import com.example.administrator.hodlife.model.WeatherModel;
import com.example.administrator.hodlife.utility.Constant;
import com.example.administrator.hodlife.utility.GPSInterface;
import com.example.administrator.hodlife.utility.GetGPSLocation;
import com.example.administrator.hodlife.R;
import com.example.administrator.hodlife.utility.WeatherAPI;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);





        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        HomeFragment hFragment = new HomeFragment();
        openHomeFragment(hFragment, Constant.HOME_FRAGMENT);
    }
    private void openHomeFragment(Fragment mFragment, String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mFragment, tag);
        if(!tag.equals(Constant.HOME_FRAGMENT))
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private boolean isFragmentAvailable(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            System.out.println("Print Error");
            return false;
        }else{
            return true;
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_current_location) {
            if(!isFragmentAvailable(Constant.HOME_FRAGMENT))
                openHomeFragment(new HomeFragment(), Constant.HOME_FRAGMENT);
        } else if (id == R.id.nav_city) {
            if(!isFragmentAvailable(Constant.CITY_FRAGMENT))
                openHomeFragment(new CityFragment(), Constant.CITY_FRAGMENT);
        } else if (id == R.id.nav_week) {
            if(!isFragmentAvailable(Constant.WEEK_FRAGMENT))
                openHomeFragment(new WeekWeatherFragment(), Constant.WEEK_FRAGMENT);
        } else if (id == R.id.nav_aboutus) {
            if(!isFragmentAvailable(Constant.ABOUT_FRAGMENT))
                openHomeFragment(new AboutFragment(), Constant.ABOUT_FRAGMENT);
        }else if(id == R.id.nav_setting){
            if(!isFragmentAvailable(Constant.SETTING_FRAGMENT))
                openHomeFragment(new SettingsFragment(), Constant.SETTING_FRAGMENT);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            /*int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }*/
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }


}

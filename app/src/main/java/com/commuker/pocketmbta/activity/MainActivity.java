package com.commuker.pocketmbta.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.commuker.pocketmbta.R;
import com.commuker.pocketmbta.adapter.RouteAdapter;
import com.commuker.pocketmbta.datamodel.Route;
import com.commuker.pocketmbta.datamodel.RouteState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static Logger LOGGER = Logger.getLogger("MainActivity");
    private static final String SELECTED_ITEM = "arg_selected_item";

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;
    private String mLatitude;
    private String mLongitude;
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    private List<Route> routeList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RouteAdapter mAdapter;

    private List<Route> routeList2 = new ArrayList<>();
    private RecyclerView mRecyclerView2;
    private RouteAdapter mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.routes_recycler_view);

        mAdapter = new RouteAdapter(routeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        prepareRouteData();

        mRecyclerView2 = (RecyclerView) findViewById(R.id.go_places_recycler_view);

        mAdapter2 = new RouteAdapter(routeList2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView2.setAdapter(mAdapter2);

        prepareRouteData2();


        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        return true;
                    }

                });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongitude = String.valueOf(mLastLocation.getLongitude());
        }
        LOGGER.info("Current location obtained: " + mLatitude + mLongitude);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.places) {
            Intent intent = new Intent(this, YourPlacesActivity.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.help_feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_drawer, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item) {
        int mSelectedItem = item.getItemId();
        // update selected item
        // uncheck the other items.
        if (mSelectedItem == R.id.navigation_go) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mRecyclerView2.setVisibility(View.VISIBLE);
        } else if (mSelectedItem == R.id.navigation_routes) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView2.setVisibility(View.INVISIBLE);
        }
    }

    private void prepareRouteData() {
        Route route = new Route("Green-A", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-C", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-C", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-D", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-F", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-A2", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-P", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-S", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-B2", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B3", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B4", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);
    }

    private void prepareRouteData2() {
        Route route = new Route("Green-A", RouteState.ON_TIME);
        routeList2.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList2.add(route);

        route = new Route("Green-C", RouteState.DELAY);
        routeList2.add(route);
    }

}

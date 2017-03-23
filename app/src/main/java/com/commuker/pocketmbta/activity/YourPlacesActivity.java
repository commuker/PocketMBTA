package com.commuker.pocketmbta.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commuker.pocketmbta.R;
import com.commuker.pocketmbta.datamodel.Place;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

public class YourPlacesActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String MESSAGES_CHILD = "messages";

    private RecyclerView mRecyclerView;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Place, MessageViewHolder> mFirebaseAdapter;
    private GoogleApiClient mGoogleApiClient;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView addressView;

        public MessageViewHolder(View v) {
            super(v);
            nameView = (TextView) itemView.findViewById(R.id.place_name);
            addressView = (TextView) itemView.findViewById(R.id.place_address);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setContentView(R.layout.activity_your_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Place,
                MessageViewHolder>(
                Place.class,
                R.layout.place_list_row,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Place model, int position) {
                viewHolder.nameView.setText(model.getName());
                viewHolder.addressView.setText(model.getAddress());
            }
        };
        mRecyclerView = (RecyclerView) findViewById(R.id.your_places_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.add_input_place_fab);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(YourPlacesActivity.this);
                alertDialog.setTitle("Add place to your library");
                alertDialog.setMessage("Enter place name: ");

                final EditText input = new EditText(YourPlacesActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String locationName = input.getText().toString();
                                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                                        .push().setValue(new Place(locationName, "unknown", 0.0f, 0.0f));

                                mFirebaseAdapter.notifyDataSetChanged();

                                Snackbar.make(view, "Added location " + locationName, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });

                alertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.add_current_place_fab);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(YourPlacesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(YourPlacesActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (lastLocation != null) {
                    String latitude = String.valueOf(lastLocation.getLatitude());
                    String longitude = String.valueOf(lastLocation.getLongitude());
                    String locationName = latitude + "," + longitude;
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                            .push().setValue(new Place(locationName, "", Float.valueOf(latitude), Float.valueOf(longitude)));

                    mFirebaseAdapter.notifyDataSetChanged();

                    Snackbar.make(view, "Added current location: " + locationName, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Can't fetch current GPS location", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

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

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

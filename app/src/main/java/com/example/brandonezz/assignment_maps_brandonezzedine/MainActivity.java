package com.example.brandonezz.assignment_maps_brandonezzedine;

import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,  GoogleMap.OnCameraIdleListener {

    public GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseLoadData();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("location");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    public ArrayList<MapLocation> arrayLocations = new ArrayList<MapLocation>();

    public void firebaseLoadData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {


                    Double latitude = locationSnapshot.child("latitude").getValue(Double.class);

                    Double longitude = locationSnapshot.child("longitude").getValue(Double.class);

                    String location = locationSnapshot.child("location").getValue(String.class);

                    Log.d("Loading Data", "location" + location + "longitude" +
                            longitude + "latitude" + latitude);

                    arrayLocations.add(new MapLocation(location, longitude, latitude));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;

        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);
        googleMap.setOnCameraMoveListener(this);

        LatLng Philadelphia = new LatLng(-13.49103, 151.2093);

        googleMap.addMarker(new MarkerOptions().position(Philadelphia).title("Marker at Philadelphia"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Philadelphia));


    }

    public void createMarkerFromFirebase(ArrayList<MapLocation> locations) {

        for (MapLocation location : locations) {
            LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(newLocation).title(location.getLocation()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
        }

    }

    private static LatLng STARTING_MARKER_POSITION =new LatLng(1.491029, 142.3913004);

    private LatLng distanceFrom= STARTING_MARKER_POSITION;

    private Polyline line=null;

    private static Geocoder geocoder=null;

    private GoogleMap.OnMapClickListener clickListener=new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(final LatLng pos) {

            if (line!=null)
                line.remove();


            line = googleMap.addPolyline(new PolylineOptions()
                    .add(distanceFrom, pos)
                    .width(9)
                    .color(Color.BLUE));


        }
    };


    private void setUpMapIfNeeded() {

        if (googleMap == null) {

            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

            if (googleMap != null) {
                setUpMap();
            }
        }


    }

    @Override
    public void onCameraIdle()

    {
        Toast.makeText(this, "CAMERA IS IDLE", Toast.LENGTH_SHORT).show();
    }

        private void setUpMap() {

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(STARTING_MARKER_POSITION, 14));

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            MarkerOptions markerOptions=new MarkerOptions()
                    .position(STARTING_MARKER_POSITION)
                    .draggable(true);

            googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
            {
                @Override
                public void onMarkerDragStart(Marker arg0)
                {
                    if (line!=null)
                        line.remove();
                }
                @Override
                public void onMarkerDragEnd(final Marker pos)
                {
                    distanceFrom=pos.getPosition();
                }

                @Override
                public void onMarkerDrag(Marker arg0)
                {
                }
            });

            // the callback to invoke is set
            googleMap.setOnMapClickListener(clickListener);
        }

    public boolean onMarkerClick(Marker marker) {

        marker = googleMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(-106.261, 63.5630)));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                return false;
            }
        });
        return false;
    }


    @Override
    public void onCameraMove()
    {
        Toast.makeText(this, "CAMERA IS MOVING", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMoveCanceled()
    {
        Toast.makeText(this, "CAMERA HAS STOPPED MOVING", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMapClick(LatLng latLng) {

    }
}





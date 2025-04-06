package com.example.getajobdone;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private DatabaseReference databaseReference, dbRef;
    private List<String> todoListSPIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("UsersTodoList").child(Objects.requireNonNull(auth.getUid()));
        todoListSPIds = new ArrayList<>();

        // Fetch the SP IDs from the user's todo list
        fetchTodoListSPIds();
    }

    private void fetchTodoListSPIds() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            databaseReference.child("TodoList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String spId = dataSnapshot.child("spUid").getValue(String.class);
                        if (spId != null) {
                            todoListSPIds.add(spId);
                        }
                    }
                    // Fetch SP details after getting the todo list SP IDs
                    fetchSPDetails();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("MapsActivity", "Failed to fetch todo list SP IDs", error.toException());
                }
            });
        }
    }

    private void fetchSPDetails() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Customers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String spId = dataSnapshot.getKey();
                    if (todoListSPIds.contains(spId)) {
                        double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                        String businessName = dataSnapshot.child("businessName").getValue(String.class);

                        // Add marker for each SP in the todo list
                        LatLng spLocation = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(spLocation).title(businessName));
                    }
                }
                // Move camera to the first SP location if available
                if (!todoListSPIds.isEmpty()) {
                    DataSnapshot firstSP = snapshot.child(todoListSPIds.get(0));
                    double latitude = firstSP.child("latitude").getValue(Double.class);
                    double longitude = firstSP.child("longitude").getValue(Double.class);
                    LatLng firstSPLocation = new LatLng(latitude, longitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstSPLocation, 15));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MapsActivity", "Failed to fetch SP details", error.toException());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
}
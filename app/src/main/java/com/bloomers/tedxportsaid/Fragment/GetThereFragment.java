package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Model.Coordinates;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetThereFragment extends Fragment {

    public static GetThereFragment newInstance() {
        return new GetThereFragment();
    }

    private MapView mMapView;
    private GoogleMap googleMap;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_get_there, container, false);

        mMapView = root.findViewById(R.id.map_view);

        final TextView getThereDesc =root.findViewById(R.id.get_there_desc);

        mMapView.onCreate(savedInstanceState);


        FirebaseDatabase.getInstance().getReference().child("Coordinates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    final Coordinates coordinates = dataSnapshot.getValue(Coordinates.class);
                    if (HeavilyUsed.isContextSafe(getActivity())) {
                        try {
                            MapsInitializer.initialize(getActivity().getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {

                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap mMap) {
                                    googleMap = mMap;

                                    LatLng venue = new LatLng(coordinates.getLat(), coordinates.getLongd());
                                    googleMap.addMarker(new MarkerOptions().position(venue).title("إيفينت تيدكس"));

                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(venue).zoom(15).build();
                                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        FirebaseDatabase.getInstance().getReference().child("getThereDesc").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    getThereDesc.setText((CharSequence) dataSnapshot.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        if (mMapView != null) {
            if (googleMap != null) {
                googleMap.clear();
            }
            mMapView.onDestroy();
            mMapView = null;
            googleMap = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }
}
package com.bloomers.tedxportsaid.Fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
@SuppressWarnings("ConstantConditions")
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
        View root = inflater.inflate(R.layout.fragment_get_there, container, false);

        mMapView = root.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

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

                    LatLng venue = new LatLng(31.27048432, 32.30471898);
                    googleMap.addMarker(new MarkerOptions().position(venue).title("إيفينت تيدكس"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(venue).zoom(16).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        root.findViewById(R.id.careem_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("كود برنامج كريم", "54as43a");
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    MainActivity.showCusomtToast(getActivity(),getString(R.string.careem_code_copied),null,true);
                }

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMapView!=null){
            mMapView.onDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView!=null){
            mMapView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView!=null){
            mMapView.onResume();
        }
    }
}
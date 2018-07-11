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
import android.widget.TextView;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.Model.Coordinates;
import com.bloomers.tedxportsaid.Model.VoucherCode;
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
    private TextView voucher_code_text;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_get_there, container, false);

        mMapView = root.findViewById(R.id.map_view);

        final TextView getThereDesc =root.findViewById(R.id.get_there_desc);
        voucher_code_text = root.findViewById(R.id.voucher_code_text);

        final TextView voucher_code_desc = root.findViewById(R.id.voucher_code_desc);
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


        FirebaseDatabase.getInstance().getReference().child("voucher_code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    VoucherCode voucherCode = dataSnapshot.getValue(VoucherCode.class);
                    voucher_code_text.setText(voucherCode.getCode());
                    voucher_code_desc.setText(voucherCode.getDesc());
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        root.findViewById(R.id.careem_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("كود برنامج كريم", voucher_code_text.getText());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    MainActivity.showCustomToast(getActivity(), getString(R.string.careem_code_copied), null, true);
                }

            }
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
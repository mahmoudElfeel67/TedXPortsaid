package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;

import
     butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class DeveloperFragment extends Fragment {

    @BindView(R.id.developer_image) ImageView developer_image;
    Unbinder unbinder;

    public static DeveloperFragment newInstance() {
        return new DeveloperFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_developer, container, false);
        unbinder =  ButterKnife.bind(this,root);

        if (getActivity()!=null){
            GlideApp.with(getActivity()).load(R.drawable.official).circleCrop().into(developer_image);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

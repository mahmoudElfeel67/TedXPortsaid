package com.bloomers.tedxportsaid.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowUsFragment extends Fragment {

    @BindView(R.id.developer_image) ImageView developer_image;

    public static FollowUsFragment newInstance() {
        return new FollowUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_follow_us, container, false);
        ButterKnife.bind(this, root);


        return root;
    }


    @OnClick({R.id.developer_image, R.id.face_book, R.id.instagram})
    void OnClik(View view) {
        developer_image.animate().scaleX(0.95F).withEndAction(new Runnable() {
            @Override
            public void run() {
                developer_image.animate().scaleX(1).scaleY(1).start();
            }
        }).scaleY(0.95F).start();

        switch (view.getId()) {
            case R.id.face_book:
            case R.id.developer_image:
                AppController.getInstance().launchFacebook(getActivity(), "page", "588576854819880");
                break;
            case R.id.instagram:
                if (AppController.getInstance().isAppInstalled("com.instagram.android", getContext())) {
                    Uri uri = Uri.parse("http://instagram.com/_u/tedxportsaidofficial");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.instagram.android");
                    startActivity(likeIng);
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/tedxportsaidofficial")));
                }
                break;

        }
    }


}

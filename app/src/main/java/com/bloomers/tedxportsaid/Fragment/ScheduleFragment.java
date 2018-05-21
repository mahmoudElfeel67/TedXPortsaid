package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
public class ScheduleFragment extends Fragment {

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    private Unbinder unbinder;
    private boolean isAtTimeline = true;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        unbinder = ButterKnife.bind(this, root);
        change();

        return root;
    }

    private void change() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in,
             R.anim.fade_out,
             R.anim.fade_out,
             R.anim.fade_out);
        ft.replace(R.id.frag, isAtTimeline ? EventTimeLineFragment.newInstance() : GetThereFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.schedule_text, R.id.get_there_text})
    void onCLik(View view) {
        switch (view.getId()) {
            case R.id.schedule_text:
                isAtTimeline = true;
                change();
                break;
            case R.id.get_there_text:
                isAtTimeline = false;
                change();
                break;
        }
    }
}
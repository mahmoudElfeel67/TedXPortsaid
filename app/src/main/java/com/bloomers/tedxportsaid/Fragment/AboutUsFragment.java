package com.bloomers.tedxportsaid.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.Model.TeamMember;
import com.bloomers.tedxportsaid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
public class AboutUsFragment extends Fragment {

    private TeamFragment.onCLick onCLick;
    public static AboutUsFragment newInstance(TeamFragment.onCLick onCLick) {
        AboutUsFragment aboutUsFragment = new AboutUsFragment();
        aboutUsFragment.onCLick = onCLick;
        return aboutUsFragment;
    }

    private Unbinder unbinder;
    @BindView(R.id.team)  TextView team;
    @BindView(R.id.partners)  TextView partners;
    @BindView(R.id.follow_us)  TextView follow_us;
    @BindView(R.id.developer)  TextView developer;
    private static int lastClickedId;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_about_us, container, false);
        unbinder = ButterKnife.bind(this, root);
        if (onCLick==null){
            onCLick = new TeamFragment.onCLick() {
                @Override
                public void onClick(View view, TeamMember teamMember) {
                    ((MainActivity)getActivity()).callTeamPreview(view, teamMember,1100);
                }
            };
        }
        if (lastClickedId==0){
            change(TeamFragment.newInstance(onCLick));
        }else {
            root.findViewById(lastClickedId).performClick();
        }

        return root;
    }


    private void change(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in,
             R.anim.fade_out,
             R.anim.fade_out,
             R.anim.fade_out);
        ft.replace(R.id.frag,fragment).addToBackStack("as").commitAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void grayOut(View view){
        team.setTextColor(view.getId()==team.getId() ?AppController.easyColor(getContext(),R.color.red_color):Color.parseColor("#862f2d"));
        partners.setTextColor(view.getId()==partners.getId() ?AppController.easyColor(getContext(),R.color.red_color):Color.parseColor("#862f2d"));
        follow_us.setTextColor(view.getId()==follow_us.getId() ?AppController.easyColor(getContext(),R.color.red_color):Color.parseColor("#862f2d"));
        developer.setTextColor(view.getId()==developer.getId() ?AppController.easyColor(getContext(),R.color.red_color):Color.parseColor("#862f2d"));
    }

    @OnClick({R.id.team, R.id.partners, R.id.follow_us, R.id.developer})
    void onCLik(View view) {
        grayOut(view);
        lastClickedId = view.getId();
        switch (view.getId()) {
            case R.id.team:
                change(TeamFragment.newInstance(onCLick));
                break;
            case R.id.partners:
                change(PartnersFragment.newInstance());
                break;
            case R.id.follow_us:
                change(FollowUsFragment.newInstance());
                break;
            case R.id.developer:
                change(DeveloperFragment.newInstance());
                break;
        }
    }

}
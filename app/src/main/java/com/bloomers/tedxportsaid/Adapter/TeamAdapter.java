package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.Fragment.TeamFragment;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    private final TeamFragment.onCLick onCLick;

    public TeamAdapter(AppCompatActivity editActivity, TeamFragment.onCLick clicked) {
        this.mContext = new WeakReference<>(editActivity);
        this.onCLick = clicked;
    }

    @NonNull
    @Override
    public TeamAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new TeamAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {

        return 40;
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.team_member)
        ImageView team_member;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.team_member)
        public void onClick(View view) {
            onCLick.onClick(view);
        }

        void bind() {
            GlideApp.with(mContext.get()).load(R.drawable.portofilio).circleCrop().into(team_member);
        }

    }

}

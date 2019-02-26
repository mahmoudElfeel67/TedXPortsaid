package com.bloomers.tedxportsaidadmin.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaidadmin.R;
import com.bloomers.tedxportsaidadmin.model.question;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsAdapter  extends RecyclerView.Adapter<QuestionsAdapter.SingleItemRowHolder> {

    private final WeakReference<FragmentActivity> mContext;
    private final ArrayList<question> arrayList;

    public QuestionsAdapter(FragmentActivity editActivity, ArrayList arrayList) {
        this.mContext = new WeakReference<>(editActivity);
        this.arrayList =arrayList;
    }

    @NonNull
    @Override
    public QuestionsAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);

        return new QuestionsAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    public void removeItem(int position) {
        deleteItem(position,this,arrayList);

    }

    public void restoreItem(question item, int position) {
        arrayList.add(position, item);
        notifyItemInserted(position);
    }

    public void deleteItem(int index, RecyclerView.Adapter adapter, ArrayList arrayList) {
        try {
            arrayList.remove(index);
            adapter.notifyItemRemoved(index);
            adapter.notifyItemRangeRemoved(index, 1);
        } catch (IndexOutOfBoundsException e) {
            adapter.notifyDataSetChanged();
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.question)
        TextView question;

        @BindView(R.id.view_background)
        RelativeLayout viewBackground;

        @BindView(R.id.view_foreground) public
        RelativeLayout viewForeground;


        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }


        void bind() {
            question.setText(arrayList.get(getAdapterPosition()).getQuestion());
        }

    }

}
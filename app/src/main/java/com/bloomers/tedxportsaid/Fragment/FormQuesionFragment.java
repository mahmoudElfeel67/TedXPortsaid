package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.R;
public class FormQuesionFragment extends Fragment {

    public static FormQuesionFragment newInstance() {
        return new FormQuesionFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.form_question, container, false);
        Button sendQuestion = root.findViewById(R.id.send_question);
        sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showCusomtToast(getActivity(),"تمام ياقمر السؤال اتبعت",((ViewGroup)root.findViewById(R.id.toast_lay)),true);
            }
        });


        return root;
    }

}
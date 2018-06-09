package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
public class FormQuesionFragment extends Fragment {

    public static FormQuesionFragment newInstance() {
        return new FormQuesionFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.form_question, container, false);
        final EditText editText = root.findViewById(R.id.question_text_view);
        Button sendQuestion = root.findViewById(R.id.send_question);
        sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())){
                    MainActivity.showCusomtToast(getActivity(),"برجاء كتابه سؤالك اولا !",((ViewGroup)root.findViewById(R.id.toast_lay)),true);
                    return;
                }


                String key = FirebaseDatabase.getInstance().getReference().child("speaker_questions").push().getKey();
                AppController.getInstance().addLoadingBlock(getActivity(),((ViewGroup)root.findViewById(R.id.toast_lay)));
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("question", editText.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("speaker_questions").child(key).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppController.getInstance().removeLoadingScreen(getActivity(),((ViewGroup)root.findViewById(R.id.toast_lay)));
                        if (task.isSuccessful()){
                            MainActivity.showCusomtToast(getActivity(),"تم ارسال سؤالك لفريق تيدكس بورسعيد !",((ViewGroup)root.findViewById(R.id.toast_lay)),true);
                        }else {
                            MainActivity.showCusomtToast(getActivity(),"لم يتم ارسال سؤالك نرجو المحاوله مره اخري",((ViewGroup)root.findViewById(R.id.toast_lay)),true);
                        }
                    }
                });



            }
        });


        return root;
    }

}
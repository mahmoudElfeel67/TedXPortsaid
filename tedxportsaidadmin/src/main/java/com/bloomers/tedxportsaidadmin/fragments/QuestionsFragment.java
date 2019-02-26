package com.bloomers.tedxportsaidadmin.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bloomers.tedxportsaidadmin.Activity.MainActivity;
import com.bloomers.tedxportsaidadmin.Adapter.QuestionsAdapter;
import com.bloomers.tedxportsaidadmin.R;
import com.bloomers.tedxportsaidadmin.Utitltes.RecyclerItemTouchHelper;
import com.bloomers.tedxportsaidadmin.Utitltes.other.LinearLayoutManagerEXT;
import com.bloomers.tedxportsaidadmin.model.question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionsFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    ArrayList<question> quesitions = new ArrayList();
    String parent;
    QuestionsAdapter questionsAdapter;
    Button save;
    Button number;
    Button name;
    private boolean saveEqt = false;


    public static QuestionsFragment newInstance(String parent) {
        QuestionsFragment questionsFragment = new QuestionsFragment();
        questionsFragment.parent = parent;
        return questionsFragment;
    }

    private View root;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        root = inflater.inflate(R.layout.fragment_questions, container, false);
        number = root.findViewById(R.id.number);
        name = root.findViewById(R.id.name);
        final RecyclerView questions_recycler = root.findViewById(R.id.questions_recycler);

        questions_recycler.setLayoutManager(new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false));
        FirebaseDatabase.getInstance().getReference().child("speaker_questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot snapshot = dataSnapshot.child(parent.replace(".",""));

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {


                    if (filterCurse((String) dataSnapshot1.getValue())){
                        quesitions.add(new question(dataSnapshot1.getKey(), (String) dataSnapshot1.getValue()));
                    }




                }

                number.setText(String.valueOf(quesitions.size()) + " Questions");
                name.setText(parent);

                questions_recycler.setItemAnimator(new DefaultItemAnimator());
                questionsAdapter = new QuestionsAdapter(getActivity(), quesitions);
                questions_recycler.setAdapter(questionsAdapter);
                ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, QuestionsFragment.this);
                new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(questions_recycler);


                if (!dataSnapshot.hasChild(parent.replace(".",""))) {
                    root.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        save = root.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isStoragePermissionGranted(getActivity(), 500)) {
                    saveNow();
                }


            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        return root;
    }


    private Boolean filterCurse(String string){
        ArrayList<String> curses = new ArrayList();
        curses.add("سكس");
        curses.add("كسم");
        curses.add("كسمك");
        curses.add("كسمكو");
        curses.add("متناك");
        curses.add("منيكه");
        curses.add("منيكة");
        curses.add("بضان");
        curses.add("خول");
        curses.add("شرموطة");
        curses.add("شرموطة");
        curses.add("شرمطه");
        curses.add("شرمطه");
        curses.add("بزاز");
        curses.add("بضان");
        curses.add("علق");
        curses.add("زبر");
        curses.add("عرص");
        curses.add("معرص");
        curses.add("شرموط");
        curses.add("علوقيه");
        curses.add("علوقية");
        curses.add("لبوه" );

        for (String curse : curses){
             if (string.contains(curse)){
                 return  false;
             }
        }

        return true;
    }

    private void saveNow() {
        if (quesitions.size() == 0) {
            MainActivity.showCustomToast(getActivity(), "No Questions To save !", null, false);
        } else {
            writeToFile();
        }
    }


    public boolean isStoragePermissionGranted(@NonNull Activity context, int permissionNum) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {

                if (permissionNum != 55154) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionNum);

                }
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;

        }
    }
    private String formQuestion(String question, int postion) {
        return postion + ".   " + question;
    }


    private void writeToFile() {
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TEDxQuestions";
            if (!new File(file_path).exists()) {
                new File(file_path).mkdir();
            }
            File save = new File(file_path, parent + " " + "Questions" + ".doc");

            FileWriter fileWriter = new FileWriter(save);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


            for (int i = 0; i < quesitions.size(); i++) {
                bufferedWriter.newLine();
                bufferedWriter.write(formQuestion(quesitions.get(i).getQuestion(), i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();


            scanFile(getContext(),save);

            MainActivity.showCustomToast(getActivity(), "Questions Saved please check TEDxQuestions folder", null, true);
        } catch (IOException e) {

            MainActivity.showCustomToast(getActivity(), "Questions Save failed", null, false);
            Log.e("Exception", "File write failed: " + e.toString());
        } finally {

        }
    }


    public void scanFile(Context context, File f) {
        Uri contentUri = Uri.fromFile(f);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }



    @Override
    public void onResume() {
        super.onResume();
        if (saveEqt) {
            saveEqt = false;
            saveNow();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveEqt = true;

            } else {
                MainActivity.showCustomToast(getActivity(), "Please accept permissions", null, false);
            }
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof QuestionsAdapter.SingleItemRowHolder) {
            // get the removed item name to display it in snack bar
            final String key = quesitions.get(viewHolder.getAdapterPosition()).getKey();
            final String name = quesitions.get(viewHolder.getAdapterPosition()).getQuestion();
            FirebaseDatabase.getInstance().getReference().child("speaker_questions").child(parent).child(key).setValue(null);

            // backup of removed item for undo purpose
            final question deletedItem = quesitions.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            questionsAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(root, "تم ازاله السؤال", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("speaker_questions").child(parent).child(key).setValue(name);
                    // undo is selected, restore the deleted item
                    questionsAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}

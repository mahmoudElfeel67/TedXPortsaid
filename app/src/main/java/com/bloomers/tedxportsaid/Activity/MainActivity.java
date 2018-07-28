package com.bloomers.tedxportsaid.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.CustomView.CountDownView;
import com.bloomers.tedxportsaid.CustomView.PagerSlidingTabStrip;
import com.bloomers.tedxportsaid.Dialog.AskSpeakerDialog;
import com.bloomers.tedxportsaid.Dialog.RandomDialog;
import com.bloomers.tedxportsaid.Fragment.AboutUsFragment;
import com.bloomers.tedxportsaid.Fragment.ArticleFragment;
import com.bloomers.tedxportsaid.Fragment.ScheduleFragment;
import com.bloomers.tedxportsaid.Fragment.SpeakerFragment;
import com.bloomers.tedxportsaid.Fragment.TeamFragment;
import com.bloomers.tedxportsaid.Fragment.VideosFragment;
import com.bloomers.tedxportsaid.Manager.TabPageIndicatorAdapter;
import com.bloomers.tedxportsaid.Model.Schedule;
import com.bloomers.tedxportsaid.Model.Speaker;
import com.bloomers.tedxportsaid.Model.TeamMember;
import com.bloomers.tedxportsaid.Model.event_date;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.ints;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.bloomers.tedxportsaid.Utitltes.other.delay;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scale_view) ImageView scale_view;
    @BindView(R.id.team_bg) ImageView team_bg;
    @BindView(R.id.team_image) ImageView team_image;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.team_info) TextView team_info;
    @BindView(R.id.display_container) LinearLayout display_container;
    @BindView(R.id.count_down) CountDownView countDownView;
    private int[] originalPos;
    private ViewPager pager;
    private int current_position = 0;
    private event_date eventDate;
    public static boolean isVideos = false;


    private void createMember(final int number, final String name, final String team, final String desc, @RawRes final int drawable) {

        InputStream inputStream = getResources().openRawResource(drawable);

        final StorageReference storage = FirebaseStorage.getInstance().getReference().child("Team").child(number + ".jpg");
        final UploadTask uploadTask = storage.putStream(inputStream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        TeamMember member = new TeamMember(name.trim().toUpperCase(), team.trim(), desc.trim(), uri.toString().replace("https://firebasestorage.googleapis.com", ""));

                        FirebaseDatabase.getInstance().getReference().child("Team").child(number + "").setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    showCustomToast(MainActivity.this, "SUCCESS", null, true);

                                } else {
                                    showCustomToast(MainActivity.this, "ERROR", null, true);
                                }

                            }
                        });
                    }
                });


                // ...
            }
        });

    }

    private void createSpeaker(final int number, final String name, final String topic, final String desc, @RawRes final int drawable) {

        InputStream inputStream = getResources().openRawResource(drawable);

        final StorageReference storage = FirebaseStorage.getInstance().getReference().child("Speakers").child(number + ".jpg");
        final UploadTask uploadTask = storage.putStream(inputStream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        Speaker member = new Speaker(name.trim().toUpperCase(), topic.trim(), desc.trim(), uri.toString().replace("https://firebasestorage.googleapis.com", ""));

                        FirebaseDatabase.getInstance().getReference().child("Speakers").child(number + "").setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    showCustomToast(MainActivity.this, "SUCCESS", null, true);

                                } else {
                                    showCustomToast(MainActivity.this, "ERROR", null, true);
                                }

                            }
                        });
                    }
                });


                // ...
            }
        });

    }


    private void createScheduleMemeber(String date, String name, int number) {
        Schedule member = new Schedule(date.trim().toUpperCase(), name.trim().toUpperCase());

        FirebaseDatabase.getInstance().getReference().child("schedule").child(number + "").setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    showCustomToast(MainActivity.this, "SUCCESS", null, true);

                } else {
                    showCustomToast(MainActivity.this, "ERROR", null, true);
                }

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pager = findViewById(R.id.pager);
        if (AppController.getInstance().isThereInternet(this)) {
            FirebaseDatabase.getInstance().getReference().child("isThereVideos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        isVideos = (boolean) dataSnapshot.getValue();
                    }

                    setViewPagerAdapter();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    setViewPagerAdapter();
                }
            });
        } else {
            setViewPagerAdapter();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (current_position) {
                    case 0:

                        if (isVideos) {
                            if (VideosFragment.videos != null && VideosFragment.videos.size() != 0) {
                                HeavilyUsed.callSaveDialog(MainActivity.this, RandomDialog.newInstance(false, VideosFragment.videos), null);
                            } else {
                                MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_videos_randomize), null, false);
                            }
                        } else {
                            if (ArticleFragment.articles != null && ArticleFragment.articles.size() != 0) {
                                HeavilyUsed.callSaveDialog(MainActivity.this, RandomDialog.newInstance(true, ArticleFragment.articles), null);
                            } else {
                                MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_articles_randomize), null, false);
                            }
                        }


                        break;
                    case 1:
                        if (isVideos) {
                            if (ArticleFragment.articles != null && ArticleFragment.articles.size() != 0) {
                                HeavilyUsed.callSaveDialog(MainActivity.this, RandomDialog.newInstance(true, ArticleFragment.articles), null);
                            } else {
                                MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_articles_randomize), null, false);
                            }

                        } else {
                            if (VideosFragment.videos != null && VideosFragment.videos.size() != 0) {
                                HeavilyUsed.callSaveDialog(MainActivity.this, RandomDialog.newInstance(false, VideosFragment.videos), null);
                            } else {
                                MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_videos_randomize), null, false);
                            }
                        }
                        break;
                    case 2:
                        if (SpeakerFragment.speakers != null && SpeakerFragment.speakers.size() != 0) {
                            HeavilyUsed.callSaveDialog(MainActivity.this, AskSpeakerDialog.newInstance(SpeakerFragment.speakers), null);
                        } else {
                            MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_speakers_randomize), null, false);
                        }


                        break;
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("event_date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    eventDate = dataSnapshot.getValue(event_date.class);


                    Calendar c2 = Calendar.getInstance();
                    c2.set(eventDate.getBefore_year(), eventDate.getBefore_month() - 1, eventDate.getBefore_day(), 0, 0, 0);
                    if (System.currentTimeMillis() > c2.getTimeInMillis()) {
                        countDownView.setStartDuration(eventTime(eventDate));
                        countDownView.start();
                        countDownView.setVisibility(View.VISIBLE);


                        countDownView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                countDownView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                Shader textShader = new LinearGradient(0, 0, countDownView.getWidth(), countDownView.getHeight(),
                                        new int[]{getResources().getColor(R.color.gradient_startColor),
                                                getResources().getColor(R.color.gradient_endColor)},
                                        new float[]{0.50F, 1}, Shader.TileMode.CLAMP);
                                countDownView.getTextPaint().setShader(textShader);
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        display_container.setPivotX(0);
        display_container.setPivotY(0);
        scale_view.animate().x(5).y(5).scaleX(0).scaleY(0).setDuration(0).start();
        scale_view.animate().alpha(0).setStartDelay(500).setDuration(500).start();
        display_container.animate().x(5).y(5).scaleX(.20F).scaleY(.20F).alpha(0).setDuration(0).start();

        if (AppController.getInstance().isArabic(this)) {
            fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    callTeamPreview(countDownView, new TeamMember(), 200);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dissmisTeamMember(300);
                        }
                    }, 200);
                }
            });
        }
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ca302c")));

        //createSpeakers();
      //  creatMemebers();

    }

    private void setViewPagerAdapter() {

        if (isVideos) {
            fab.setImageResource(R.drawable.shuffle);
        } else {
            fab.setImageResource(R.drawable.question_mark);
        }

        PagerSlidingTabStrip tab = findViewById(R.id.indicator);
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        if (isVideos) {
            fragmentArrayList.add(VideosFragment.newInstance());
            fragmentArrayList.add(ArticleFragment.newInstance());
        } else {
            fragmentArrayList.add(ArticleFragment.newInstance());
            fragmentArrayList.add(VideosFragment.newInstance());
        }

        fragmentArrayList.add(SpeakerFragment.newInstance());
        fragmentArrayList.add(ScheduleFragment.newInstance());
        fragmentArrayList.add(AboutUsFragment.newInstance(new TeamFragment.onCLick() {
            @Override
            public void onClick(View view, TeamMember teamMember) {
                callTeamPreview(view, teamMember, 1100);
            }
        }));
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager(), fragmentArrayList, MainActivity.this, isVideos);
        tab.setOnItemSelected(new PagerSlidingTabStrip.OnItemSelected() {
            @Override
            public void onSelectPostion(int postion) {
                current_position = postion;
                if (AppController.getInstance().isArabic(MainActivity.this)) {
                    switch (postion) {
                        case 0:
                            current_position = 4;
                            break;
                        case 1:
                            current_position = 3;
                            break;
                        case 2:
                            current_position = 2;
                            break;
                        case 3:
                            current_position = 1;
                            break;
                        case 4:
                            current_position = 0;
                            break;
                    }
                }
                switch (current_position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
                        if (isVideos) {
                            fab.setImageResource(R.drawable.shuffle);
                        } else {
                            fab.setImageResource(R.drawable.question_mark);
                        }

                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
                        if (isVideos) {
                            fab.setImageResource(R.drawable.question_mark);

                        } else {
                            fab.setImageResource(R.drawable.shuffle);
                        }
                        break;
                    case 2:
                        fab.setVisibility(View.VISIBLE);
                        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
                        fab.setImageResource(R.drawable.ask);
                        break;
                    case 3:
                        fab.animate().scaleX(0).scaleY(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                fab.setVisibility(View.GONE);
                            }
                        }).setInterpolator(new FastOutSlowInInterpolator()).start();
                        break;
                    case 4:
                        fab.animate().scaleX(0).scaleY(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                fab.setVisibility(View.GONE);
                            }
                        }).setInterpolator(new FastOutSlowInInterpolator()).start();
                        break;
                }

            }
        });

        pager.setAdapter(adapter);
        if (AppController.getInstance().isArabic(this)) {
            pager.setCurrentItem(fragmentArrayList.size() - 1);
        }
        tab.setViewPager(pager);
    }

    /*private void creatMemebers() {
        String mediaMember = "Media Member";
        String marketingMember = "Marketing Member";
        String talksMember = "Talks Member";
        String frmember = "FR Member";
        String hrMember = "HR Member";
        String logisticsMember = "Logistics Member";

        //UPPER BOARD 0
        createMember(1, "Ahmed Ghonim", "President", "", R.raw.ahmedghonim);
        createMember(2, "Mahmoud Lebleb", "Vice President", "TEDxPortSaid Vice President,\n" +
                "\n" +
                "Alkoshk bookstores founder and founder of Fan men El-Sharea (art from streets) initiative. I helped and participated in organizing cultural and folk events like (Sufi night - Masar Egbary music concert and 1989 wenta talea play). I'm passionate about spreading culture and arts, and that was my incentive to organize TEDxPortSaid event.\n", R.raw.mahmoudlebleb);

        createMember(3, "OLA SOLTAN", "OC Director", "A foreign procurement assistant in one of the leading international entity in the east part. I've joined TEDx since 2012 as a logistics member and then become a logistics team leader. TED has influenced me to be a better person and to look forward to the future and never give up.", R.raw.olasoltan);
        createMember(4,"MuHAMMAD ElTFahany", "Talks Director", "A journalist at Akhbarelyoum newspaper, former reporter at Dmc channel, a freelance editor and copywriter. I'm passionate about literature and sports.", R.raw.muhammadeltfahany);

        createMember(5, "Ahmed lebleb", "Media And Marketing Director", "A manager of Oscar studios, a freelancer, and former head of media at Masar Egbary Concert 2015, with 8 year experience in photography, retouching and graphic design. I’m passionate about visual arts, marketing, writing and music. ", R.raw.ahmedlebleb);
        createMember(6,"TASNEEM ELBAWAB", "HR Director", "TEDx was my turning point of shifting my career from a traditional employer to a wedding planner. I always derive my passion from TEDx because it has the power of changing people in a positive way.", R.raw.tasneemelbawab);
        createMember(7,"Hamed Hemdan", "Talks Executive Director", "A chemist. He works in pharmaceutical sales field. He joined TEDxPortSaid first event as media coordinator and Second event as a member of talks team.", R.raw.hamedhemdan);


        //MARKETING & MEDIA 9
       createMember(8,"Ahmed Abd El-MA'Boud", "Marketing Co head", "A freshly graduate dentist from the British University in Egypt. He has 5 years experience of student activities, including Public relations, Marketing, Research, & Logistics. He is a content creator and a creative writer, with the passion of poetry. He's a skillful person with the ability of multitasking and communication.", R.raw.ahmedabdel_maboud);

        createMember(9,"Ahmed ElKadey", mediaMember, "20 year old photographer and graphic designer trying to mix between creativity and branding, when I’m not designing I would be studying electrical engineering", R.raw.ahmedelkadey);
        createMember(10,"Salma el Gabry", marketingMember, "Mentor of teachers of English at Berlitz language Center, member and founder of the Anglo freaks team that helps people to learn English in fun and unique way, one of my goals is to help people to be better and develop themselves", R.raw.salmaelgabry);

        createMember(11,"Reem mohsen", marketingMember, "She has joined TEDx driven by passion of writing, gained from love of reading and with experience in copy-writing. She has volunteered in order to develop skill in marketing and to seek a change in her community to become a better one.", R.raw.reemmohsen);
        createMember(12,"AHLAM MANSY", mediaMember, "A voice-over, a script writer and a senior photographer who believes in the power of ideas and words. She joined TEDx looking forward to making a change through spreading ideas,especially throw the media.", R.raw.ahmlamalmansy);
        createMember(13,"Mariam Elsehraway", marketingMember, "An engineering student and a blogger with huge interest of art. She's interested in writing, photographing and has her own studio on Instagram. She joined TEDx believing in ‘we learn by doing’ concept, to learn new things and have an exciting experience.", R.raw.mariamelsehrawy);

        createMember(14,"SHEHAB AYMAN", mediaMember, "19 years old. Studying Political Science at AUC. An official videographer of the AUC residential life Office, a freelance videographer and filmmaker. He joined TEDx for the huge inspiration it shares and its large contribution of sharing knowledge and motivational content.", R.raw.shehabayman);
        createMember(15,"MAHMOUD ELFEEL", mediaMember, "A creative mobile app developer with 3 year experience in android and ios app developing. He is the developer of (AlaWad3k) App with the first live filter camera in the middle east with over 100k downloads and 50k active users. Also participated in other apps like (Khomasy and video screenshot and download) apps.", R.raw.mahmoudelfeel);
        createMember(16,"Karim Hamdy", mediaMember, "A graphic designer who is a specialist in designing logos and calligraphy for 4 years. I’m interested in Animation and media fields.", R.raw.karimhamdy);
        createMember(17,"Rowan mohsen", mediaMember, "High school student and a photographer with 4 year experience. She applied for TEDx believing in the idea and to gain more experience.", R.raw.rowanmohsen);

        createMember(18,"Monica William", marketingMember, "A social media specialist and blogger. work in social media agency. I have 3 years of experience in student activity including marketing, public speaker. I am an author and have 2 books. I am a content creator I join TEDx to give and get new experience and to have communication with different people.", R.raw.monicawilliam);
        createMember(19,"Ziad ElMahlawy", marketingMember, "“If you’re not making someone else’s life better, than you are wasting your time. Your life will become better by making other people’s lives better.” – Will Smith\n" +
                "I am a marketing-passionate who has a marketing agency I developed myself from online and self learning, I began with TED talks and other educational sites so I have a big loyality to TED.\n" +
                "As a result my dream was shaped to join TEDx team and help to spread main purpose of TED.", R.raw.ziadelmahlawy);



        //TALKS 21
        createMember(20,"Shahdenda Shalaby", talksMember, "Moved from working in a Multi International Co. to Banking field with a Diploma in Art and Science of Neuro Linguistic Programming, INLPTA IN, March 2018. \n" +
                "A Certified Life Coach/ NLP Practitioner, INLPTA to be in December, 2018.\n" +
                "Passionate about human development & self actualization, Aspiring to be a motivational speaker and a writer raising human awareness and consciousness and empowering one another to prosper & evolve.", R.raw.shanhndashalaby);
        createMember(21,"ZENA SAMY", talksMember, "20 years old, interested in humans right, gender equality and giving session about women empowerment and how to defend herself against harassment and rape.", R.raw.zenasamy);
        createMember(22,"NOURAN ABu ELGOUD", talksMember, "Senior student in the Faculty of Pharmacy. joined development organizations since 2013, and she is the president of AYB-PSU franchise. She also participated in the student union of Port Said University.", R.raw.nouranabuelgoud);
        createMember(23,"REEM GABR", talksMember, "A 21 years old woman that want to put her fingerprint in the world. She studies English and had 2 different job experiences as a teacher and a sales representative. She Volunteered as a committee head at Interact Club. She has a passion towards writing and learning. Interested in nature and in increasing the sense of humanity.", R.raw.reemgabr);
        createMember(24,"MOHANAD AHMED", talksMember, "A pharmaceutical student, a volunteer in tactic club and AIESEC. He believes in volunteer work because helping people makes life full of experience, as our existence on earth is very short, so make it worth.", R.raw.mohanedahmed);
        createMember(25,"ARWA ELGHADBAN", talksMember, "Strongly believe in TEDx not only because it has the power to add to personal lives, but to gather the people around various ideas that are discussed, shared and thought of openly. I believe in this process’s ability to create a powerful societal base essential for social change.", R.raw.arwaelghadban);
        createMember(26,"Salama SHAAban", talksMember, "24 years old. She studied Business Administration at Faculty of Commerce - English section and started her career in First Abu Dhabi bank. She's a former member of AIESEC and joined TEDx because of her passion in the volunteer field. She has experience in event organization and interested in Violin, Modern Arts and Design", R.raw.salmashaban);


        //Logistics 30
        createMember(27,"OMNIA GABR", "Logistics Head", "I have joined TEDx 5 years ago and from that day my mindset had been totally changed. I realized the possibility of changing the world with few words but it’s mandatory to have faith in what you are doing.", R.raw.omniagabr);
        createMember(28,"HADEER OSAMA", "Logistics Vice Head", "Joined TEDx in 2012. I believe that each one has a great story to tell, to listen to and to learn from, because we don’t have the ability to live everyone’s experience but we can learn from them by listening to their stories. ", R.raw.hadeerosama);
        createMember(29,"Menna Dorgham", logisticsMember, "21 years old, an arabic calligrapher and arabesque artist. I believe in the power of the word and its impact", R.raw.mennadorgham);
        createMember(30,"Abd Elrahman", logisticsMember, "23 years old, I graduated from Faculty of Commerce - English section - Accounting department in 2016. I work as a project coordinator at Berlitz company.", R.raw.abdelrahman);
        createMember(31,"Doaa hassan", logisticsMember, "I’m working in Unifreight global logistics forwarding agency as operation and documentation team leader. I like sharing experience with my colleagues and interested in gaining more information about science, business and global issues.", R.raw.doaahassan);
        createMember(32,"Noha Gamal", logisticsMember, "20 years old. Student at Faculty Of Arts - English Department. and a former member of Student Union.", R.raw.nohagamal);
        createMember(33,"NADA ELTRBily", logisticsMember, "24 years old, she works as a medical representative. Since 2012, she has participated in many volunteer and developmental organizations, have learned a lot and gained much experience.", R.raw.nadaeltrbily);
        createMember(34,"Mohamed Hassabala", logisticsMember, "21 years old. I have worked in Alkoshk Bookstore for 6 months.", R.raw.mohamedhassbala);
        createMember(35,"Nadeen m elsbaa", logisticsMember, "A senior medical student. My hobbies are drawing and crafting. I've volunteered in different fields and organizations.", R.raw.naddenelsabaa);
        createMember(36,"Ezz eissia", logisticsMember, "20 years old, studying Petroleum at T.A.S school.\n" +
                "I've spent the last 3 years in volunteer work and gained a lot of experience. My ultimate ambition is to Leave my name with a big impact.", R.raw.ezzeissa);


        //HR 40
        createMember(37,"Rana Maher Lamiey", hrMember, "25 years old, I Graduated from Faculty of Science. Volunteer work allow me to discover my capabilities. I believe in the power of word, a simple word can change people's lives, that's why I joined TEDx.", R.raw.ranamaherlamiey);
        createMember(38,"Shrouk Ayman", hrMember, "21 years old, she's studying Business Administration at Faculty of Commerce - English section. She has worked in HR field in different multinational companies and also in volunteer work.", R.raw.shroukaymman);
        createMember(39,"Alaa Naguib", hrMember, "", R.raw.alaanaguib);
        createMember(40,"Rovan Adel", hrMember, "A student at faculty of commerce English section. I am a very ambitious, competetive and enthusiastic. I am interested in self learning\n" +
                "I am proud to be involved in such entity that aims to exchange successful ideas.", R.raw.rovanadel);
        createMember(41,"NADA HASSAN", hrMember, "22 years old, she studies pharmacy, Believing that the best way of learning is hand-on experience, she started to work as a pharmacist and train at pharmaceutical companies.", R.raw.nadahassan);
        createMember(42,"Ahmed adel", hrMember, "A mechanical engineer with background in science, researches, rockets and plane engines. And he dreams of working at NASA.", R.raw.ahmedadel);
        createMember(43,"REEM MEDHAT", hrMember, "18 year-old dreamer, and an Engineering student. She loves science and reading especially inspirational books.", R.raw.reemmehat);
        createMember(44,"Shahd Mohamed Belal",hrMember,"I’m 20 years old. This will be the first time to working in a team. I hope to do my best to depvelop my self and to be a member of this great organization",R.raw.shadbelalelwehidy);

        //FR 49
        createMember(45,"Shoroq Shams", frmember, "A fresh graduate accountant from faculty of Commerce, English section.I believe in volunteer work so I have 4 years Experience of volunteering activities , Strongly believe in TEDx cause it adds a lot to people's lives", R.raw.shoroqshams);
        createMember(46,"Menna Badreya", frmember, "A senior at faculty of Arts, English department. I mostly care about family, friends and starving for excellence. My motto is \"only the strongest will survive\" I'm interested in marketing, sales and anything related to business. I love learning languages and hope to travel abroad.", R.raw.mennabadreya);
        createMember(47,"Gamal Maged", frmember, "An ambitious business student who has been looking to improving himself for three years by working in FR teams and NGOs. Also interested in photography and playing the guitar\n" +
                "Joining TEDx would give me an opportunity to reach full potential and to meet new people.", R.raw.gamalmaged);
        createMember(48,"Reem Mohamed", frmember, "I am 21 years old , studying chemical engineering , Passionate in helping people and volunteering. I have been volunteer for 5 years and I intend to continue until the world become a better place.\n", R.raw.reemmohamed);
        createMember(49,"Ahmed Ramadan", frmember, "I am 24 years old, Working as a Power Electrician for Suez Canal Authority, Ship Transit Department. Graduated from the faculty of Commerce - English section, Accounting Department. A Sophomore at the faculty of Science and applied mathematics department. I am passionate about mathematical sciences, finance, economics and music. Interested in how things work and spreading the scientific awareness to help our country become a developed one.", R.raw.ahmedramadan);

    }

    private void createSpeakers() {

        createSpeaker(1, "Amr Wahba", "", "عمرو وهبه هو ممثل ومؤلف كوميدي مصري مبدع. كان عمرو ضمن فريق الكتابة لبرامج ساخرة عديدة منها \"أبلة فاهيتا وأبو حفيظة والبلاتوه\". وقدم فيديوهات مليئة بالكوميديا تحت عنوان \"نجوم بتحب مصر\" و\"الموظف المثالى\". كما انضم عمرو إلى فريق عمل برنامج \"Saturday Night Live بالعربى\" فى موسميه الثالث والرابع ، وقد قام بدور لامع ملحوظ في مسلسل كلبش الجزء الثاني هذا العام والذي أظهر مواهبه وأثبت جدارته على التمثيل.", R.raw.amrwahb);
        createSpeaker(2, "Adham Hamshary", "", "هو مهندس مصري ومسافر حول العالم. بدأ مسيرة سفره في عام ٢٠٠٩ حيث سافر إلى 45 دولة حتى الآن. كما أنه أحد المؤثرين على مواقع التواصل الاجتماعي، يملك قناةً خاصة على \"اليوتيوب\" تحت إسم \"أدهم حول العالم\"، يقدم من خلالها نصائح حول أفضل الأماكن للسياحة لأصحاب الميزانية المحدودة، ونصائح حول تحضيرات السفر، ويوثق كذلك رحلاته من خلال نقل ثقافات الدول التي يزورها. دائماً ما يقول همشري أن \"السفر ليس فقط للأغنياء\".\n" +
                "\n" +
                "An Egyptian Engineer, his passion is travelling and knowing other cultures. His journey began in 2009, as he travelled to 45 countries so far. He owns a channel on YouTube with the name of \"Adham around the world\". He offers tips on the best touristic sites for limited budget holders, tips on travel preparations, as well as documenting his travels through knowing the cultures of the places he visit. He said \"Traveling is not only for the rich\"\n", R.raw.adhamhamshry);
        createSpeaker(3, "Raghda EzzelDin", "", "شابه مصرية اتبعت قلبها لأعماق البحر. إكتشفت رغدة شغفها تجاه الغوص الحر من سنتين فقامت بالعمل على نشر هذه الرياضة في مصر ومن ثم قررت مشاركة تجربتها مع الناس. كما عملت رغدة على توفير ورش عمل ومعسكرات للغوص الحر لتخبر من حولها عن علاقة التركيز الذهني والغوص والصلة الوطيدة بينهم. تقول رغدة \"الغوص هو الشئ الذي يجعلني حرة تماماً لذلك أتمنى أن يتبع الجميع ما يحبه حتى يكونوا أحرار\".\n" +
                "\n" +
                "A young Egyptian woman who followed her heart to the depth of the sea. She discovered her passion for free-diving two years ago and decided to spread the sport around Egypt to share her experience with everyone. She organizes free-diving camps and workshops to teach people free-diving and mindfulness. She said \"Free-diving set me free and I wish for everyone to follow their heart so they free\".\n", R.raw.raghdaezz);
        createSpeaker(4, "Amna Elshandaweely", "", "آمنه هي مصممة أزياء مصرية والرئيس التنفيذي لعلامة التصاميم التجارية “Amna Elshandaweely”. تتميز تصاميمها بأنها تقوم بشكل كامل على محاكاة أزياء\n" +
                "قبائل لمناطق معينة. من الفيوم لنيروبي وحتى واحة سيوة، قامت بالتعرف على الثقافات\n" +
                "المتعددة والأزياء الرسمية التي تُعرف بها تلك الثقافات. مجموعات آمنه تقف ضد حالة\n" +
                "المجتمع الراهنة وتحارب قضايا متعددة مثل التمييز العنصري وعدم المساواة بين الجنسين\n" +
                "والاختلافات بين الأجيال وأزمه الهوية. كما شاركت أيضاً آمنه في برنامج Project Runway Middle East.\n" +
                "\n" +
                "كل يوم هنعلن عن إحدي متحدثينا .. تابعونا وهيكون ليكوا فرصة في دعوة مجانية.\n" +
                "Hurry up and apply now for Early Bird Registration , Only 24 hours left !\n" +
                "http://bit.ly/2up2EVX\n" +
                "\n" +
                "We are proud to announce TEDxPortSaid 2018-Enlighten third speaker\n" +
                "Amna Abdelrahman\n" +
                "Amna is a Fashion Designer and CEO of “Amna Elshandaweely” Designs.\n" +
                "Amna's collections stand against the status quo of society, and it fights issues like discrimination, gender inequality, generation differences and identity crisis. “Each of my collection is based on abcity and its people or tribes,” she explains. From Fayoum, to Nairobi,\n" +
                "to Siwa, Abdelrahman delves deep into each of the cultures' mysticism and symbolism to raise their status while creating wearable fashion. “They were looking for designers who have identity, and they saw that in me,” she says.", R.raw.amnaelshan);

        createSpeaker(5, " Ahmed Mourad", "", "هو كاتب ومؤلف مصري. يعد مراد من مؤلفين قصص الإثارة القلائل في الوطن العربي. إلتحق بالمعهد العالي للسينما ليدرس التصوير السينمائي، وتخرّج عام ٢٠٠١ بترتيب الأول على القسم، ونال مشروع تخرجه \"الهائمون - الثلاث ورقات” - وفي اليوم السابع - عدة جوائز للأفلام القصيرة في مهرجانات بإنجلترا وفرنسا وأوكرانيا. أثناء عمله في التصوير الفوتوغرافي في عام ٢٠٠٧، كتب روايته الأولى “فيرتيجو” والتي حصلت على الجائزة الإيطالية “Premio per la Cultura Mediterranea” في عام ٢٠١٣. تُرجمت الرواية إلى ثلاث لغات مختلفة من قبل دور نشر متعددة. كتب مراد أيضاً “تراب الماس” و “الفيل الأزرق” و”١٩١٩” و”أرض الإله” و”موسم صيد الغزلان”. من أهم الجوائز التي حصل عليها مراد كانت جائزة الدولة للتفوق في الثقافة وجائزة البوكر العربية ٢٠١٤.\n" +
                "is an Egyptian author of thriller fiction, and one of the few mystery/suspense writers in the Arab World. Mourad studied cinematography at the prestigious Cinema Institute in Cairo. His graduation project \"The wandering-three papers\", and on the seventh day, received short film awards at festivals in England, France and Ukraine.While working in photography in the year of 2007, he wrote his first novel \"Virtego\" which won the Italian prize, “Premio per la Cultura Mediterranea” in 2013.The novel was translated into 3 languages by different publishing houses, in 2011. He also wrote \"Diamond Dust\", \"The Blue Elephant\", \"1919\" and \"The God's land\". Mourad also won several awards, however, the ultimate one was the state award for Excellence in Culture , beside the Arabic Poker award in 2014.\n", R.raw.ahmedmourad);
        createSpeaker(6, "Mr. Wahid Morsy", "", "يعد من أهم صناع التليسكوبات في مصر. فهو الوحيد الذى يقوم بتصنيع التليسكوب بجميع أجزائه يدويا وبخامات محلية. كما يقوم بضبطه ومعايرته طبقا للمستويات العالمية. قام بتجهيز عدة ورش عمل في جامعات مصرية، كالقاهرة، وعين شمس، والزقازيق. ورشة المخيم الفلكي العربي بسلطنة عمان كانت من إحدى الورش التي تم تجهيزها بالخارج. يشرح أ/ وحيد للطلبة كيفية صناعة التليسكوب، ودائماً ما يقوم بمشاركتهم حيث كان يأتيهم بمجموع قطع التليسكوب ويتركهم ليقوموا بتركيبه. قام بتجربة تصنيع تليسكوب من صفائح الكانز الفارغة وأضاف \"عشان أقولهم بس إن أي مواد ممكن نستخدمها”.\n" +
                "\n" +
                "\n" +
                "One of the most prominent Telescope makers is Egypt. He is considered the only one to be able to create a telescope with local equipments. The telescope maker was a guest at many Egyptian universities, such as Cairo, Ain Shams, and Zagazig. He was also invited to several conferences abroad for his knowledge. The thing Mr Wahid always cares about is sharing his experience with his audience. Mr. Wahid is unstoppable. He once created a telescope from empty canes. \"I want to tell people that we can use any material” says Mr. Wahid.\n", R.raw.waheedmrsy);
        createSpeaker(7, " Yasmine Nazmy", "", "هي خبيرة تغذية وكاتبة كتاب \"Happy Belly\" واللي بيحتوي على عدة وصفات غذائية صحية بالكامل وهي أيضاً صاحبة علامة \"KAJU\" التجارية. يقوم عمل ياسمين على مساعدة الناس من خلال تحسين إدراكهم لذاتهم عن طريق التغذية حيث أنها تؤمن بأن الجسد الصحيح وحُب الذات مصدره الرئيسي هو التمسُك بالطعام الصحي. قامت ياسمين بصناعة وسيلة علاجية أطلقت عليها \"AFAA\" والتي تحتوي على مجموعة تمرينات ذهنية تساعد الفرد على تأسيس إدراكه لذاته وتطوير مهارة معرفة الطعام الصحي. \n" +
                "\n" +
                "The author of \"Happy Belly\" and owner of the health food brand \"KAJU\". She is certified in Nutrition Consulting and she helps people improve their self-perception, as she believes a positive body shape and self-esteem is the first step required to have a healthy food relationship. \n", R.raw.yasminnazmy);
        createSpeaker(8, " Omar Hegazy", "", "Egyptian swimmer and adventurous. Omar Hegazy is the first handicapped to fully cross Jordan al-Aqaba. Hegazy founded “Support Egypt in Your Own Way” initiative. He also participated in the UK’s biggest open-water swimming event, the Great North Swim, where he completed 5 KM in 2 hours and 17 minutes.\n", R.raw.omarhegaszy);

        createSpeaker(9, " Hesham Sallam", "", "اد هشام سلام مدير مركز جامعة المنصورة للحفريات الفقارية فريق بحثي من الطلاب في عام ٢٠١٤ استخرج فيها هيكلاً عظميا لديناصور من الواحات الداخلة وفِي عام ٢٠١٦ نشرت نتائج هذا الإكتشاف في مجلة الناتشر العالمية وسمي الديناصور Mansourasaurus وهو جنس ونوع جديد من الديناصورات العشبية. واعتبر اكتشاف mansourasaurus إنجازاً ضخماً من قبل علماء الحفريات.\n" +
                "\n" +
                "The director of the Mansoura University Vertebrate Paleontology center (MUVP), he led a student research team in 2014 to extract a dinosaur skeleton from the \"Dakhla Oasis\". In 2016, the results of this discovery were published in the \" Nature: Evolution and Ecology\" international magazine, the dinosaur is named Mansourasaurus, a new genus and species of herbivorous dinosaurs. The discovery of Mansourasaurus is considered a huge achievement by the paleontologists around the world.\n", R.raw.heshamsallem);

        createSpeaker(10, " Kirolos Bahgat", "", "يرلس هو طبيب جراح مصري وكاتب في مجال علم النفس. نُشِرَ له عدة كتب منها \"اللا مُكترث\" ويناقش داخله مجموعة من المقالات من نواحي عديدة أهمها الجانب النفسي. كما صدر له كتاب فلسفة التحول \"بيضة مان\"، وكتاب \"التجربة الفكرية لروح أمه\". الرائع في كتاباته أنه يستخدم لغة الناس \"العامية\" ويضيف إليها لمسته الطريفة، مما جعل الناس أكثر رغبة في التعرف على علم النفس من خلاله. \n" +
                "\n" +
                "An Egyptian surgeon and a writer in psychology. He has published several books including \"Nonchalant\" in which he discusses series of articles in many aspects, especially the psychological field. He also published his book tackling The Philosophy of Transformation \"Egg Man\". His writings are known to be catchy as he uses the common language \"Colloquial\". His funny way makes people more willing to learn about psychology as well. \n", R.raw.kirlos);

        createSpeaker(11, " Aya Tarek", "", "هي رسامة وفنانة شارع مصرية ولدت بالإسكندرية،وتعتبر آيه طارق من رواد حركة فن الشارع في مصر حيث بدأت الرسم على حوائط الإسكندرية ولفتت الأنظار لفنها المميز والغير تقليدي منذ عام ٢٠٠٨. تحتوي أعمالها على مجموعة مثيرة من المشاريع الفنية المتنوعة. شاركت في عدة مهرجانات وأحداث في بلاد مختلفة منها القاهرة، ولبنان، وساو باولو بالبرازيل، وبورتلاند بأمريكا، وچينيڤ بسويسرا، حتى فرانكفورت بألمانيا. \n" +
                "كما شاركت أيضا في الفيلم المستقل \"ميكروفون\" للمخرج أحمد عبدالله عام ٢٠١١، حيث كانت أول فنانة شارع على الإطلاق تُمثل هذ النوع من الفن.\n" +
                "\n" +
                "is an Egyptian painter and street artist born in Alexandria. Aya is considered a prodigy of the Egyptian street art movement. She started painting on the walls of Alexandria streets since 2008, and that’s when people started paying attention to her unique and unconventional type of art. Her portfolio includes astonishing and diverse art projects. She participated in various events and festivals in different parts of the world such as Cairo, Lebanon, São Pauloin Brazil, Portland in America, Geneva in Switzerland and Frankfurt in Germany. Aya was also featured in the independent movie directed by Ahmed Abdullah “Microphone” in 2011, where she was the first street artist to represent that nontraditional art in Egypt.\n", R.raw.ayatarek);

        createSpeaker(12, "Ahmed Zahran \n", "", "الشريك المؤسس والرئيس التنفيذي لشركة KarmSolar، وهي شركة مساهمة مصرية تأسست عام ٢٠١١، تعمل في مجال الأنظمة الشمسية. حاصل على درجة الماجستير في الإقتصاد من SOAS جامعة لندن وعلى درجة البكالوريوس في إدارة الأعمال والتمويل من الجامعة الأمريكية بالقاهرة وشهادة في ريادة الأعمال الإجتماعية من كلية الدراسات العليا في الأعمال التجارية في ستانفورد. \n" +
                "يمتلك زهران عشر سنوات من الخبرة في مجال الطاقة المتجددة وتجارة الكربون في العمل في Shell في المملكة المتحدة وتونس، وفي العمل أيضاً في Egyptian Kuwaiti Holding في مصر كمدير تطوير الأعمال في مجال الطاقة المتجددة وآليات التنمية النظيفة. \n" +
                "زهران مسئول حاليا عن الإشراف على استراتيجيات النمو التجارية والتقنية لشركة KarmSolar.\n" +
                "\n" +
                "The Co-founder and CEO of KarmSolar, an Egyptian joint stock company established in 2011, working in the field of off-grid and on-grid solar innovations. He holds a MSc. in Economics from SOAS, University of London, a BA. in Business Administration & Finance from the AUC and a certificate in social entrepreneurship from the Stanford Graduate School of Business. \n" +
                "Zahran has 10 years of experience in renewable energy and carbon trad-ing working at Shell in the UK and Tunisia, and working in Egyptian Kuwaiti Holding in Egypt as a business development manager for renewable energy and clean development mechanisms, initiating the company's carbon trading and renewable energy business. Zahran is currently responsible for guiding, coordinating and overseeing the commercial and technical growth strategies of KarmSolar.\n", R.raw.ahmed_zahran);

        createSpeaker(13, " Ben Ewig", "", "أسس المدرب \"بن\" CrossFit Proactive في بورسعيد فى ٢٠١٤ لنشر اللياقة الحقيقية في المدينة. طوال مسيرته التدريبية ، استفاد من خبرته التعليمية والرياضية إلى جانب إبداعه في تدريب لاعبيه الرياضيين. حصل على درجة الماجستير في علم وظائف الأعضاء من جامعة أيوا. صارع في الكلية، وركض في سباقات الماراثون في الجامعة. درس فى الجامعة لمدة ثلاث سنوات (علم التشريح ، علم وظائف الأعضاء ، ودروس رياضية مختلفة). قام بتدريب الرياضيين بشكل احترافي منذ ٢٠٠٥ ولديه فوق العشر سنوات من الخبرة في تحسين أداء الرياضيين فى مختلف المفاهيم الرياضية.\n" +
                "\n" +
                "He Founded CrossFit Proactive in Port Said in 2014 with the heart to spread real fitness in the city. Throughout his coaching career he has drawn on his educational and sports background along with his own creativity to coach his athletes. \n" +
                "He has a master degree in exercise physiology from the University of Iowa. He wrestled in college, and ran marathons in university. He taught at the university level for three years (anatomy, physiology, and various sports classes). He has been training athletes professionally since 2005\n" +
                "He has over 10 years of experience enhancing athletes’ performance of various sports backgrounds.\n", R.raw.ben);
        createSpeaker(14,"","","ﺘﺸـﺮت ﻗـﺼﺔ دﻳـﻨﺎ ﻣـﻮﺳـﻰ اﻟـﻄﻔﻠﺔ ذات اﻟﺴـﺒﻌﺔ ﻋﺸـﺮ ﻋـﺎﻣـﴼ ﻛـﺎﻟـﻨﺎر ﻓـﻲ اﻟﻬﺸـﻴﻢ .دﻳـﻨﺎ ﻫـﻲ اﻟـﻄﻔﻠﺔ اﻟﺘﻲ اﻛﺘﺸﻔﺖ ﺗﻜﻮﻳﻦ ﻛﻴﻤﻴﺎﺋﻲ ﻹﻧـﻘﺎذ اﻷرواح .أﺿـﺎﻓـﺖ دﻳـﻨﺎ\" ﻛـﻨﺖ ﻗـﺪ ﺑـﺪأت ﻓـﻲ اﻟـﺒﺤﺚ ﻋـﻦ ﻛـﻴﻔﻴﺔ وﻗـﻒ اﻟـﻨﺰﻳـﻒ واﻟـﺬي ﻳـﻌﺘﺒﺮ اﻟﺴـﺒﺐ اﻟـﺮﺋﻴﺴـﻲ اﻟـﺜﺎﻧـﻲ ﻟـﻠﻮﻓـﺎة ﻓـﻲ اﻟﻤﺴـﺘﺸﻔﻴﺎت . \"ﻳـﻘﻮم اﻟـﻤﺼﻞ ﺑـﺈﻳـﻘﺎف ﻧـﺰﻳـﻒ اﻟـﺪم ﻓـﻲ ﻣﺎـﻻ ﺛـﻮان .ﻛـﻤﺎ ﺣـﺼﻠﺖ ﺑـﺎﻟـﻔﻌﻞ ﻋـﻠﻲ ﺑـﺮاءة إﺧـﺘﺮاع ﻓـﻲ اﻟـﻮﻻﻳـﺎت اﻻﻣـﺮﻳـﻜﻴﺔ .وﻛـﺬﻟـﻚ ﺗـﻢ ١٠ ﻳـﺘﻌﺪي ﺗـﻜﺮﻳـﻤﻬﺎ ﻣـﻦ ﻗـﺒﻞ ﺟـﻮﺟـﻞ وﻧـﺎﺳـﺎ .إﻫـﺘﻤﺎم دﻳـﻨﺎ اﻷول ﻟـﻴﺲ اﻟـﻤﺎدة ﺑـﻞ ﺣـﺼﻮل ﻛـﻞ اﻟﺒﺸـﺮ ﻋـﻠﻰ .ﺣﻖ اﻟﻌﻼج اﻟﻤﺘﻄﻮر\n" +
                "\n" +
                "Her story went viral this year, as the entire country shared the tale of the 17-year-old who discovered a chemical composition to save lives. “I had started doing research on how to stop blood loss because it is the second leading cause of death in hospitals,” says the New York based scientist and entrepreneur. Her product, Hemostat V-Seal, stops bleeding of lethal wounds in between seven and ten seconds, and has already obtained the patent in the USA, as well as earned her prizes by Google and NASA. Striving to commercialize it soon, the teenage prodigy doesn’t think about proﬁt.\n",R.raw.dinamousad);


    }

    private void createSchedule() {
        createScheduleMemeber("9:30", "ENTRANCE", 0);
        createScheduleMemeber("10:30", "WELCOME SPEECH", 1);
        createScheduleMemeber("11:00", "TALK : KERLOS BAHGAT", 2);
        createScheduleMemeber("11:30", "TALK : AYA TAREK", 3);
        createScheduleMemeber("12:00", "JUMMAH PRAYER BREAK", 4);
        createScheduleMemeber("1:00", "TALK : HESHAM SALLAM", 5);
        createScheduleMemeber("1:30", "TALK : RAGHDA EZZ ELDIN", 6);
        createScheduleMemeber("2:00", "PERFORMANCE : ABUZEID", 7);
        createScheduleMemeber("2:30", "TALK : AHMED MOURAD", 8);
        createScheduleMemeber("3:00", "TALK : YASMINE NAZMY", 9);
        createScheduleMemeber("3:30", "BREAK", 10);
        createScheduleMemeber("4:30", "TALK : MR. WAHID MORSY", 11);
        createScheduleMemeber("5:00", "TALK : ADHAM HAMSHARY", 12);
        createScheduleMemeber("5:30", "TALK : DEENA MOUSA", 13);
        createScheduleMemeber("6:00", "TALK : OMAR HEGAZY", 14);
        createScheduleMemeber("6:30", "BREAK", 15);
        createScheduleMemeber("7:30", "TALK : AMNA ELSHANDAWEELY", 16);
        createScheduleMemeber("8:00", "PERFORMANCE : STATE OF HARMONY", 17);
        createScheduleMemeber("8:30", "TALK : AHMED ZAHRAN", 18);
        createScheduleMemeber("9:00", "TALK BEN EWIG", 19);
        createScheduleMemeber("9:30", "CLOSING", 20);


    }*/

    private long eventTime(event_date eventDate) {
        Calendar c2 = Calendar.getInstance();
        c2.set(eventDate.getYear(), eventDate.getMonth() - 1, eventDate.getDay(), eventDate.getHour(), eventDate.getMinute(), 0);
        if (System.currentTimeMillis() > c2.getTimeInMillis()) {
            countDownView.setVisibility(View.GONE);
        }
        return c2.getTimeInMillis() - System.currentTimeMillis();
    }

    @OnClick(R.id.count_down)
    void onClick(View view) {


        switch (view.getId()) {
            case R.id.count_down:
                String day;
                switch (countDownView.getDay(eventTime(eventDate))) {
                    case 1:
                        day = " يوم ";
                        break;
                    case 2:
                        day = " يومين ";
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        day = countDownView.getDay(eventTime(eventDate)) + " أيام ";
                        break;
                    default:
                        day = countDownView.getDay(eventTime(eventDate)) + " يوم ";
                        break;
                }

                String hour;
                switch (countDownView.getHour(eventTime(eventDate))) {
                    case 1:
                        hour = " ساعه ";
                        break;
                    case 2:
                        hour = " ساعتين ";
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        hour = countDownView.getHour(eventTime(eventDate)) + " ساعات ";
                        break;
                    default:
                        hour = countDownView.getHour(eventTime(eventDate)) + " ساعه ";
                        break;
                }

                MainActivity.showCustomToast(MainActivity.this, "لسه " + day + " و " + hour + "مستعديين !!", null, true, 5000);
                if (AppController.getInstance().isArabic(this)) {
                    pager.setCurrentItem(1, true);
                } else {
                    pager.setCurrentItem(3, true);
                }
                break;
        }

    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void callTeamPreview(View view, TeamMember teamMember, int duration) {
        fab.animate().scaleX(0).scaleY(0).setInterpolator(new FastOutSlowInInterpolator()).start();
        TextView team_info = findViewById(R.id.team_info);
        team_info.setText(teamMember.getDesc());
        scale_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        originalPos = new int[2];
        view.getLocationInWindow(originalPos);
        int x = originalPos[0];
        int y = originalPos[1] - ints.dp2px(23, MainActivity.this);

        originalPos[1] = y;
        scale_view.setAlpha(1F);
        scale_view.setVisibility(View.VISIBLE);
        scale_view.setScaleX(.20F);
        scale_view.setScaleY(.20F);
        scale_view.setX(x);
        scale_view.setY(y);


        display_container.setPivotX(0);
        display_container.setPivotY(0);

        display_container.setX(x);
        display_container.setY(y);

        display_container.setScaleX(.20F);
        display_container.setScaleY(.20F);

        display_container.requestLayout();


        GlideApp.with(MainActivity.this)
                .load("https://firebasestorage.googleapis.com" + teamMember.getProfile_url())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(team_image);


        if (duration == 200) {
            display_container.setVisibility(View.INVISIBLE);
            scale_view.setVisibility(View.INVISIBLE);
            team_bg.setVisibility(View.INVISIBLE);

            scale_view.animate().setStartDelay(0).scaleX(20).scaleY(20).setDuration(duration - 100).start();

            display_container.animate().x(0).y(0).scaleY(1).scaleX(1).setDuration(duration).start();
            team_bg.animate().setStartDelay(500).start();


        } else {
            display_container.setVisibility(View.VISIBLE);
            scale_view.setVisibility(View.VISIBLE);
            team_bg.setVisibility(View.VISIBLE);
            scale_view.animate().setStartDelay(0).scaleX(20).scaleY(20).setDuration(duration - 100).start();

            display_container.animate().x(0).y(0).scaleY(1).scaleX(1).setDuration(duration).alpha(1).start();
            team_bg.animate().setStartDelay(500).alpha(0.50F).start();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        if (countDownView != null) {
            countDownView.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (originalPos != null) {
            dissmisTeamMember(1000);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription( getString(R.string.app_name), bm, AppController.easyColor(MainActivity.this, R.color.black));
            setTaskDescription(taskDesc);
        }

    }

    private void dissmisTeamMember(int duration) {
        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
        display_container.setPivotX(0);
        display_container.setPivotY(0);
        scale_view.animate().x(originalPos[0]).y(originalPos[1]).scaleX(0).scaleY(0).setDuration(duration - 100).start();
        scale_view.animate().alpha(0).setStartDelay(500).setDuration(500).start();
        display_container.animate().x(originalPos[0] + ints.px2dp(80, MainActivity.this)).y(originalPos[1] + ints.px2dp(80, MainActivity.this)).scaleX(.20F).scaleY(.20F).alpha(0).setDuration(duration - 200).start();
        team_bg.animate().alpha(0).start();
        originalPos = null;
    }

    public static void showCustomToast(final Activity activity, String text, ViewGroup viewGroup, boolean success) {
        showCustomToast(activity, text, viewGroup, success, 2000);
    }

    private static void showCustomToast(final Activity activity, String text, ViewGroup viewGroup, boolean success, int period) {
        final ViewGroup rootLayout;

        if (viewGroup == null) {
            rootLayout = activity.findViewById(android.R.id.content);
        } else {
            rootLayout = viewGroup;
        }

        final View view = View.inflate(activity, R.layout.toast_view, null);
        TextView messageText = view.findViewById(R.id.message);
        final CardView cardView = view.findViewById(R.id.card);
        if (!success) {
            cardView.setCardBackgroundColor(AppController.easyColor(activity, R.color.colorAccent));
            ((ImageView) view.findViewById(R.id.exes)).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            messageText.setTextColor(Color.WHITE);
        }

        messageText.setText(text);
        rootLayout.addView(view);

        cardView.animate().translationY(0).start();

        new delay(cardView, period) {
            @Override
            protected void OnDelayEnded() {
                cardView.animate().translationY(ints.dp2px(110, activity)).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        rootLayout.removeView(view);
                    }
                }).start();
            }
        };

    }

}

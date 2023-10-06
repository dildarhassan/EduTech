package com.example.edutech.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.edutech.Fragment_Activity.FragmentChallenge;
import com.example.edutech.Fragment_Activity.FragmentHome;
import com.example.edutech.Fragment_Activity.FragmentMyCourses;
import com.example.edutech.R;
import com.example.edutech.databinding.NavHeaderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NavigationButtons extends AppCompatActivity implements View.OnClickListener {



    private LinearLayout home_layout,profile_layout,challenge_layout,my_courses_layout;
    private ImageView homeImage,profileImage,challengeImage,my_coursesImage;
    private TextView homeTxt,profileTxt,challengeTxt,my_coursesTxt,tvLogoutUser;

    private static DatabaseReference mDatabase;

    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;

    NavigationView navigationView;

    private String userId;

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_buttoms);
        //initializing items in activity;
        sharedPreferences= getSharedPreferences("MY_FILE", Context.MODE_PRIVATE);
        userId=sharedPreferences.getString("USERID","NA");

        initActivityItems();

        home_layout.setOnClickListener(this);

        profile_layout.setOnClickListener(this);

        challenge_layout.setOnClickListener(this);

        my_courses_layout.setOnClickListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_logout: AlertDialog.Builder dialog=new AlertDialog.Builder(NavigationButtons.this)
                                .setTitle("Do You Want To Log Out").
                                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(NavigationButtons.this, LogInActivity.class));
                                        SharedPreferences sharedPreferences=getBaseContext().getSharedPreferences("MY_FILE",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor myFile=sharedPreferences.edit();
                                        myFile.clear().apply();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setCancelable(true);
                        dialog.show();

                }
                return true;
            }
        });

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homelayout:initHomeLayoutItems();
                break;
            case R.id.challenge_layout:initChallengeLayoutItems();
                break;
            case R.id.my_courses_layout:initMyCoursesLayoutItems();
                break;
            case R.id.profile_layout:initProfileLayoutItems();
                break;
        }
    }
    private void initActivityItems(){
        home_layout = findViewById(R.id.homelayout);
        profile_layout = findViewById(R.id.profile_layout);
        challenge_layout = findViewById(R.id.challenge_layout);
        my_courses_layout = findViewById(R.id.my_courses_layout);

        homeImage = findViewById(R.id.homeImage);
        profileImage = findViewById(R.id.profileImage);
        challengeImage = findViewById(R.id.challengeImage);
        my_coursesImage = findViewById(R.id.my_coursesImage);

        homeTxt = findViewById(R.id.homeTxt);
        profileTxt = findViewById(R.id.profileTxt);
        challengeTxt = findViewById(R.id.challengeTxt);
        my_coursesTxt = findViewById(R.id.my_coursesTxt);

        homeImage.setImageResource(R.drawable.ic_home_selected);
        home_layout.setBackgroundResource(R.drawable.rounde_back_home_100);

        drawerLayout=findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        tvLogoutUser=findViewById(R.id.nav_logout);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new FragmentHome()).commit();
    }
    private void initHomeLayoutItems() {
        //check if home is already selected or not
        if (selectedTab != 1) {
            //unselect other tabs except home tab
            profileTxt.setVisibility(View.GONE);
            challengeTxt.setVisibility(View.GONE);
            my_coursesTxt.setVisibility(View.GONE);

            profileImage.setImageResource(R.drawable.ic_profile);
            challengeImage.setImageResource(R.drawable.ic_challenge);
            my_coursesImage.setImageResource(R.drawable.ic_my_courses);

            profile_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            challenge_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            my_courses_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            //selecte home Tab
            homeTxt.setVisibility(View.VISIBLE);
            homeImage.setImageResource(R.drawable.ic_home_selected);
            home_layout.setBackgroundResource(R.drawable.rounde_back_home_100);

            //Create animation
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            home_layout.startAnimation(scaleAnimation);

            //set 1st tab as selected tab
            selectedTab = 1;

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentHome()).commit();

        }
    }
    private void initProfileLayoutItems() {
        //check if profile is already selected or not
        if(selectedTab !=2){
            //unselect other tabs except home tab
            homeTxt.setVisibility(View.GONE);
            challengeTxt.setVisibility(View.GONE);
            my_coursesTxt.setVisibility(View.GONE);

            homeImage.setImageResource(R.drawable.ic_home);
            challengeImage.setImageResource(R.drawable.ic_challenge);
            my_coursesImage.setImageResource(R.drawable.ic_my_courses);

            home_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            challenge_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            my_courses_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            //selected profile Tab
            profileTxt.setVisibility(View.VISIBLE);
            profileImage.setImageResource(R.drawable.ic_profile_selected);
            profile_layout.setBackgroundResource(R.drawable.rounde_back_home_100);

            //Create animation
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);

            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            profile_layout.startAnimation(scaleAnimation);

            //set 1st tab as selected tab
            selectedTab = 2;

            initItems();
              setUser(userId);
        }
    }
    private void initChallengeLayoutItems (){
        //check if challenges is already selected or not
        if (selectedTab != 3) {
            //unselect other tabs except home tab
            homeTxt.setVisibility(View.GONE);
            profileTxt.setVisibility(View.GONE);
            my_coursesTxt.setVisibility(View.GONE);

            homeImage.setImageResource(R.drawable.ic_home);
            profileImage.setImageResource(R.drawable.ic_profile);
            my_coursesImage.setImageResource(R.drawable.ic_my_courses);

            home_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profile_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            my_courses_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            //selected profile Tab
            challengeTxt.setVisibility(View.VISIBLE);
            challengeImage.setImageResource(R.drawable.ic_challenge_selected);
            challenge_layout.setBackgroundResource(R.drawable.rounde_back_home_100);

            //Create animation
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            challenge_layout.startAnimation(scaleAnimation);

            //set 1st tab as selected tab
            selectedTab = 3;

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentChallenge()).commit();
        }
    }
    private void initMyCoursesLayoutItems(){
        //check if my_courses is already selected or not
        if(selectedTab !=4){
            //unselect other tabs except home tab
            homeTxt.setVisibility(View.GONE);
            profileTxt.setVisibility(View.GONE);
            challengeTxt.setVisibility(View.GONE);

            homeImage.setImageResource(R.drawable.ic_home);
            profileImage.setImageResource(R.drawable.ic_profile);
            challengeImage.setImageResource(R.drawable.ic_challenge);

            home_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profile_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            challenge_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            //selected profile Tab
            my_coursesTxt.setVisibility(View.VISIBLE);
            my_coursesImage.setImageResource(R.drawable.ic_my_courses_selected);
            my_courses_layout.setBackgroundResource(R.drawable.rounde_back_home_100);

            //Create animation
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);

            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            my_courses_layout.startAnimation(scaleAnimation);

            //set 1st tab as selected tab
            selectedTab = 4;

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new FragmentMyCourses()).commit();
            popWindow();
        }

    }


    private void initItems(){
//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,profile_layout,R.string.open_nav,R.string.close_nav);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();


        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);

        } else {
            drawerLayout.closeDrawer(GravityCompat.END);
            drawerLayout.closeDrawers();

        }

    }


    public void setUser(String userId){
        mDatabase = FirebaseDatabase.getInstance().getReference("USERS");
        mDatabase.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    DataSnapshot dataSnapshot=task.getResult();
                    String Name=String.valueOf(dataSnapshot.child("name").getValue());
                    String email=String.valueOf(dataSnapshot.child("email").getValue());

                    View headerView = navigationView.getHeaderView(0);
                    TextView navUsername = (TextView) headerView.findViewById(R.id.tv_userName);
                    navUsername.setText(Name);


                    TextView UserEmail = (TextView) headerView.findViewById(R.id.tv_userEmail);
                    UserEmail.setText(email);
                }else {

                }

            }
        });

        //navUsername.setText(Name);
//        tvLogoutUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder alertDialoge=new AlertDialog.Builder(getParent())
//                        .setTitle("Do You Want To Log Out")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                FirebaseAuth.getInstance().signOut();
//                                startActivity(new Intent(NavigationButtons.this, LogInActivity.class));
//                                SharedPreferences sharedPreferences=getParent().getSharedPreferences("MY_FILE",Context.MODE_PRIVATE);
//                                SharedPreferences.Editor myFile=sharedPreferences.edit();
//                                myFile.clear().apply();
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        }).setCancelable(true);
//                alertDialoge.show();
//            }
//        });
    }

    private void popWindow(){
//        final Dialog dialog=new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.activity_pop_up_window);
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);

        sharedPreferences=getSharedPreferences("MY_FILE",MODE_PRIVATE);
        SharedPreferences.Editor myFile=sharedPreferences.edit();

        if (!sharedPreferences.getBoolean("Pressed", false)) {
            ActivityPopUpWindow activityPopUpWindow = new ActivityPopUpWindow(this);
            activityPopUpWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activityPopUpWindow.show();
            activityPopUpWindow.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            activityPopUpWindow.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            activityPopUpWindow.getWindow().setGravity(Gravity.BOTTOM);
            myFile.putBoolean("Pressed",true).apply();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }
}
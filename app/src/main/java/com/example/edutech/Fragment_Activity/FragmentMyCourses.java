package com.example.edutech.Fragment_Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.edutech.Activities.ActivityPopUpWindow;
import com.example.edutech.Adapter_Class.PopUpWindowAdapter;
import com.example.edutech.Adapter_Class.videoAdapter;
import com.example.edutech.Model_Activity.Video_model;
import com.example.edutech.R;
import com.example.edutech.databinding.FragmentCoursesBinding;
import com.example.edutech.databinding.FragmentMyCoursesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentMyCourses extends Fragment {

    private FragmentMyCoursesBinding binding;
    SharedPreferences sharedPreferences;
    private ArrayList<Video_model> videoList;

    private videoAdapter videoAdapter;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyCoursesBinding.inflate(getLayoutInflater());
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences=requireActivity().getSharedPreferences("MY_FILE", Context.MODE_PRIVATE);
        binding.tvSubName.setText(sharedPreferences.getString("SubName","NA"));

        initVideo();

        binding.btnSelectSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        });
    }

    void initVideo() {
        videoList = new ArrayList<>();
        videoAdapter = new videoAdapter(videoList, getContext());
        getVideos("JEE");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.menuRvSubList.setLayoutManager(layoutManager);
        binding.menuRvSubList.setAdapter(videoAdapter);
    }

    private void getVideos(String subName) {
        mDatabase = FirebaseDatabase.getInstance().getReference(subName);

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Video_model videoModel = new Video_model(
                                ds.child("name").getValue().toString(),
                                ds.child("video").getValue().toString());
                        videoList.add(videoModel);
                    }
                    videoAdapter.updateList(videoList);

                } else {
                    Toast.makeText(getContext(), "SOMETHING ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initList() {

        ActivityPopUpWindow activityPopUpWindow = new ActivityPopUpWindow(requireContext());
        activityPopUpWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activityPopUpWindow.show();
        activityPopUpWindow.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                binding.tvSubName.setText(sharedPreferences.getString("SubName","NA"));
            }
        });
        activityPopUpWindow.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        activityPopUpWindow.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        activityPopUpWindow.getWindow().setGravity(Gravity.BOTTOM);

    }

}
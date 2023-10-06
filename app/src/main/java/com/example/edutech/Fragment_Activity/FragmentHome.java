package com.example.edutech.Fragment_Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.edutech.Adapter_Class.videoAdapter;
import com.example.edutech.Model_Activity.Video_model;
import com.example.edutech.databinding.FragmentCoursesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private String selectedCourseName;
    FragmentCoursesBinding binding;
    ArrayList<Video_model> videoList;
    ArrayList<CarouselItem> slideList;
    videoAdapter videoAdapter;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentCoursesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSlider();
        initVideo();

    }

    void initVideo() {
        videoList=new ArrayList<>();
        videoAdapter=new videoAdapter(videoList,getContext());
        getVideos();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.menuRvList.setLayoutManager(layoutManager);
        binding.menuRvList .setAdapter(videoAdapter);
    }

    private void getVideos() {
        mDatabase= FirebaseDatabase.getInstance().getReference("JEE");

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot=task.getResult();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Video_model videoModel =new Video_model(
                                ds.child("name").getValue().toString(),
                                ds.child("video").getValue().toString());
                        videoList.add(videoModel);
                    }
                    videoAdapter.updateList(videoList);

                }else {
                    Toast.makeText(getContext(), "SOMETHING ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void initSlider() {
        slideList=new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("SLIDER");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        CarouselItem carouselItem = new CarouselItem(ds.child("image").getValue().toString());
                        slideList.add(carouselItem);
                    }
                    binding.carousel.addData(slideList);
                } else {
                    Toast.makeText(getContext(), "SOMETHING ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        videoAdapter.stopPlaying();
    }
}
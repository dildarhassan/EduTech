package com.example.edutech.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.edutech.Adapter_Class.PopUpWindowAdapter;
import com.example.edutech.Model_Activity.Courses_model;
import com.example.edutech.R;
import com.example.edutech.databinding.ActivityPopUpWindowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ActivityPopUpWindow extends Dialog {
    ActivityPopUpWindowBinding binding;
    private DatabaseReference mDatabase;
    private Context context;

    PopUpWindowAdapter popUpWindowAdapter;

    private ArrayList<Courses_model> selectedSub;

    public ActivityPopUpWindow(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPopUpWindowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("MY_FILE", Context.MODE_PRIVATE);
        String subName=sharedPreferences.getString("SubName","NA");
        initRecycleItems("NEET");


    }

    private void initRecycleItems(String subName){
        selectedSub=new ArrayList<>();
        binding.RvRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        popUpWindowAdapter=new PopUpWindowAdapter(selectedSub,getContext());

        binding.RvRecycleView.setAdapter(popUpWindowAdapter);
        initPopUpWindowItems(subName);
    }
    private void initPopUpWindowItems(String subName){
        mDatabase = FirebaseDatabase.getInstance().getReference(subName);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Courses_model courses_model = new Courses_model( ds.child("name").getValue().toString(),ds.child("image").getValue().toString());
                        selectedSub.add(courses_model);
                    }
                    popUpWindowAdapter.updateList(selectedSub);
                } else {
                    Toast.makeText(getContext(), "SOMETHING ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
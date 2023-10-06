package com.example.edutech.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.edutech.Adapter_Class.SelectCoursesAdapter;
import com.example.edutech.Model_Activity.Courses_model;
import com.example.edutech.R;
import com.example.edutech.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private DatabaseReference mDatabase;
    ArrayList<Courses_model> courses;
    SelectCoursesAdapter course_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);



        initCategories();
    }


    void initCategories() {
        courses=new ArrayList<>();
        course_adapter=new SelectCoursesAdapter(courses,this);
        getCategories();

        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setAdapter(course_adapter);
    }
    private void getCategories() {
        mDatabase = FirebaseDatabase.getInstance().getReference("COURSES");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Courses_model courses_model = new Courses_model( ds.child("name").getValue().toString(),ds.child("image").getValue().toString());
                        courses.add(courses_model);
                    }
                    course_adapter.updateList(courses);
                } else {
                    Toast.makeText(getBaseContext(), "SOMETHING ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
package com.example.edutech.Adapter_Class;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.edutech.Activities.MainActivity;
import com.example.edutech.Activities.NavigationButtons;
import com.example.edutech.Model_Activity.Courses_model;
import com.example.edutech.R;
import com.example.edutech.databinding.CoursesViewBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class SelectCoursesAdapter extends RecyclerView.Adapter<SelectCoursesAdapter.MyViewHolder> {
    private ArrayList<Courses_model> courses;
    Context context;

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Courses_model> courses){
        this.courses=courses;
        notifyDataSetChanged();

    }


    public SelectCoursesAdapter(ArrayList<Courses_model> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_view,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Courses_model courses_model=courses.get(position);

        Glide.with(context).load(courses_model.getCourseImageUrl()).into(holder.binding.courseImage);
        holder.binding.courseName.setText(courses_model.getCourseName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NavigationButtons.class);
                intent.putExtra("courseName",courses_model.getCourseName());
                context.startActivity(intent);
                SharedPreferences sharedPreferences=context.getSharedPreferences("MY_FILE",MODE_PRIVATE);
                SharedPreferences.Editor myFile=sharedPreferences.edit();
                myFile.putString("course_name",courses_model.getCourseName());
                myFile.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CoursesViewBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CoursesViewBinding.bind(itemView);

        }

    }

}


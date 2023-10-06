package com.example.edutech.Adapter_Class;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.edutech.Activities.NavigationButtons;
import com.example.edutech.Model_Activity.Courses_model;
import com.example.edutech.R;
import com.example.edutech.databinding.SubjectViewBinding;
import com.example.edutech.databinding.VideoViewBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;

public class PopUpWindowAdapter extends RecyclerView.Adapter<PopUpWindowAdapter.My_ViewHolder> {
    ArrayList<Courses_model> SelectedSub;
    Context context;


    public PopUpWindowAdapter(ArrayList<Courses_model> selectedSub, Context context) {
        SelectedSub = selectedSub;
        this.context = context;
    }
    @SuppressLint("NotifyDataSetChanged")
    public  void updateList(ArrayList<Courses_model> selectedSub){
        this.SelectedSub=selectedSub;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PopUpWindowAdapter.My_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_view,parent,false);
        return new My_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopUpWindowAdapter.My_ViewHolder holder, int position) {

        Courses_model courses_model=SelectedSub.get(position);

        Glide.with(context).load(courses_model.getCourseImageUrl()).into(holder.binding.subPhoto);
        holder.binding.subName.setText(courses_model.getCourseName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=context.getSharedPreferences("MY_FILE",MODE_PRIVATE);
                SharedPreferences.Editor myFile=sharedPreferences.edit();
                myFile.putString("SubName",courses_model.getCourseName()).apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return SelectedSub.size();
    }
    public class My_ViewHolder extends RecyclerView.ViewHolder {
        protected SubjectViewBinding binding;
        public My_ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SubjectViewBinding.bind(itemView);
        }
    }

}

package com.example.campusevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/* Class written by Christopher Park */

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.AttendeeHolder> {

    private ArrayList<Student> students;

    public AttendeeAdapter(ArrayList<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public AttendeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.eachattendee, parent, false);
        return new AttendeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeHolder holder, int position) {
        if(!students.isEmpty()) {
            Student student = students.get(position);
            holder.student.setText(student.name);
            holder.info.setText(student.email);
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class AttendeeHolder extends RecyclerView.ViewHolder {

        TextView student;
        TextView info;

        public AttendeeHolder(@NonNull View itemView) {
            super(itemView);
            student = itemView.findViewById(R.id.attendeeName);
            info = itemView.findViewById(R.id.attendeeInfo);
        }

    }
}

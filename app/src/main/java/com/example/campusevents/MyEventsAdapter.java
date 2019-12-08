package com.example.campusevents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyEventsHolder> {

    private ArrayList<Events> events;
    private final OnItemClickListener listener;

    public MyEventsAdapter(ArrayList<Events> events, OnItemClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyEventsHolder extends RecyclerView.ViewHolder {

        TextView eventName;

        public MyEventsHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(final Events event, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(event);
                }
            });
        }
    }
}

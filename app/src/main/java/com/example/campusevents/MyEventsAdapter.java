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
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.event_row, parent, false);
        return new MyEventsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventsHolder holder, int position) {
        if(!events.isEmpty()) {
            Events event = events.get(position);

            TextView eventName = holder.eventName;
            TextView time = holder.time;
            TextView date = holder.date;

            eventName.setText(event.name);
            time.setText(event.time);
            date.setText(event.date);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyEventsHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView time;
        TextView date;

        public MyEventsHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.rowName);
            time = itemView.findViewById(R.id.rowTime);
            date = itemView.findViewById(R.id.rowDate);
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

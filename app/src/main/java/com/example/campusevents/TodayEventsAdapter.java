package com.example.campusevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/* Class written by Christopher Park */

public class TodayEventsAdapter extends RecyclerView.Adapter<TodayEventsAdapter.TodayEventsHolder> {

    private ArrayList<Events> events;
    private final OnItemClickListener listener;

    public TodayEventsAdapter(ArrayList<Events> events, OnItemClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodayEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.event_row, parent, false);
        return new TodayEventsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayEventsHolder holder, int position) {
        if(!events.isEmpty()) {
            Events event = events.get(position);

            TextView eventName = holder.eventName;
            TextView loc = holder.location;

            eventName.setText(event.name);
            loc.setText("Time: " + event.time + "\nLocation: " + event.location);

            holder.bind(event, listener);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class TodayEventsHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView location;

        public TodayEventsHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.rowName);
            location = itemView.findViewById(R.id.rowTime);
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

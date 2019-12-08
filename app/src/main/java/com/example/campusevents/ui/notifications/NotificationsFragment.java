package com.example.campusevents.ui.notifications;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusevents.CreateEvent;
import com.example.campusevents.R;

import org.w3c.dom.Text;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    RecyclerView myEvents;
    Button addEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myevents, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);

        addEvent = (Button) root.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEvent.class);
                startActivity(intent);
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400, ActionBar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addEvent.setLayoutParams(params);
        addEvent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        myEvents = (RecyclerView) root.findViewById(R.id.myEvents);
        myEvents.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 650));
        return root;
    }
}
package com.example.campusevents.ui.home;

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

import com.example.campusevents.MyAccount;
import com.example.campusevents.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView todaysEvents;
    Button account;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);

        account = (Button) root.findViewById(R.id.button3);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAccount.class);
                startActivity(intent);
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(450, ActionBar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        account.setLayoutParams(params);
        account.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        todaysEvents = (RecyclerView) root.findViewById(R.id.todaysEvents);
        todaysEvents.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 650));

        return root;
    }
}
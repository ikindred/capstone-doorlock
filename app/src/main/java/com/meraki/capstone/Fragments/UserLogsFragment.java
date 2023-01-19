package com.meraki.capstone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meraki.capstone.R;
import com.meraki.capstone.RecyclerTouchListener;
import com.meraki.capstone.RecyclerviewAdapter_LogAlarmTime;
import com.meraki.capstone.RecyclerviewAdapter_LogUnlockTime;

import java.util.ArrayList;
import java.util.List;

public class UserLogsFragment extends Fragment {


    View view;
    private static final String TAG = "logger_UserLogsFragment";

    public static RecyclerView recycler_unlocktime, recycler_alarmtime;
    public static RecyclerviewAdapter_LogUnlockTime recyclerviewAdapter_logUnlockTime;
    public static RecyclerviewAdapter_LogAlarmTime recyclerviewAdapter_logAlarmTime;
    public static RecyclerTouchListener touchListener;


    public static List taskList = new ArrayList<>();
    public static List taskList2 = new ArrayList<>();

    RelativeLayout rel_withData, rel_withoutData;

    public UserLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_logs, container, false);

        rel_withData = (RelativeLayout) view.findViewById(R.id.rel_withData);
        rel_withoutData = (RelativeLayout) view.findViewById(R.id.rel_withoutData);

        String[] mode = {"unlocktime", "alarmtime"};

        recycler_unlocktime = view.findViewById(R.id.recycler_unlocktime);
        recycler_unlocktime.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewAdapter_logUnlockTime = new RecyclerviewAdapter_LogUnlockTime(getActivity());
        taskList = new ArrayList<>();
        touchListener = new RecyclerTouchListener(getActivity(), recycler_unlocktime);


        recycler_alarmtime = view.findViewById(R.id.recycler_alarmtime);
        recycler_alarmtime.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewAdapter_logAlarmTime = new RecyclerviewAdapter_LogAlarmTime(getActivity());
        taskList2 = new ArrayList<>();

        Spinner spin = (Spinner) view.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getContext(), ""+ i, Toast.LENGTH_SHORT).show();
                if (i == 0) {

                    if (recycler_unlocktime.getVisibility() == View.GONE) {
                        recycler_unlocktime.setVisibility(View.VISIBLE);
                        recycler_alarmtime.setVisibility(View.GONE);
                    }
                } else {
                    if (recycler_alarmtime.getVisibility() == View.GONE) {
                        recycler_alarmtime.setVisibility(View.VISIBLE);
                        recycler_unlocktime.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mode);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        try {


            DatabaseReference databaseReference_unlocktime = FirebaseDatabase.getInstance().getReference().child("Logs").child("unlocktime");
            databaseReference_unlocktime.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!taskList.isEmpty()) {
                        taskList.clear();
                    }
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        Log.w(TAG, "snapshot: " + postSnapshot.getKey());
//                        Log.w(TAG, "snapshot: " + postSnapshot.getValue());

                        taskList.add(postSnapshot.getValue());

                        recyclerviewAdapter_logUnlockTime.setUsersList(taskList);
                        recycler_unlocktime.setAdapter(recyclerviewAdapter_logUnlockTime);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "onCancelled: " + error.getMessage());
                }
            });

            DatabaseReference databaseReference_alarmtime = FirebaseDatabase.getInstance().getReference().child("Logs").child("alarmtime");
            databaseReference_alarmtime.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!taskList2.isEmpty()) {
                        taskList2.clear();
                    }

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        Log.w(TAG, "snapshot: " + postSnapshot.getKey());
//                        Log.w(TAG, "snapshot: " + postSnapshot.getValue());

                        taskList2.add(postSnapshot.getValue());

                        recyclerviewAdapter_logAlarmTime.setUsersList(taskList2);
                        recycler_alarmtime.setAdapter(recyclerviewAdapter_logAlarmTime);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "onCancelled: " + error.getMessage());
                }
            });


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Logs");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.w(TAG, "snapshot: " + snapshot.getValue());

                    if( snapshot.getValue().toString().equalsIgnoreCase("NA")){
                        rel_withoutData.setVisibility(View.VISIBLE);
                        rel_withData.setVisibility(View.GONE);
                    }else{
                        rel_withData.setVisibility(View.VISIBLE);
                        rel_withoutData.setVisibility(View.GONE);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        } catch (Exception e) {
            Log.w(TAG, "Exception: " + e.getMessage());
        }

        return view;
    }


}
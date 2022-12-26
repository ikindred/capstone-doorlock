package com.meraki.capstone.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meraki.capstone.FB_Users;
import com.meraki.capstone.R;
import com.meraki.capstone.RecyclerTouchListener;
import com.meraki.capstone.RecyclerviewAdapter_UserInfo;
import com.meraki.capstone.ViewDialog_AddUser;

import java.util.ArrayList;
import java.util.List;


public class UserListFragment extends Fragment {

    public UserListFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "logger_UserListFragment";

    public static RecyclerView recyclerView;
    public static RecyclerviewAdapter_UserInfo recyclerviewAdapter_userInfo;
    public static RecyclerTouchListener touchListener;

    static ViewDialog_AddUser viewDialog_addUser;
    View view;
    public static List<FB_Users> taskList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);


        recyclerView = view.findViewById(R.id.recycler_user);
        viewDialog_addUser = new ViewDialog_AddUser(getActivity());
        viewDialog_addUser.initiateDialog();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewAdapter_userInfo = new RecyclerviewAdapter_UserInfo(getActivity());
        taskList = new ArrayList<>();
        touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        recyclerView.addOnItemTouchListener(touchListener);

        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {

                if(taskList.get(position).getName().equals("NA")){

                    viewDialog_addUser.showDialog();
                }else{
//edit dialog
                }

            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }

        });

        try {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Valid");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(!taskList.isEmpty()){
                        taskList.clear();
                    }
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                      //  Log.w(TAG, "snapshot: " + postSnapshot.getKey());

                        final FB_Users fb_users = postSnapshot.getValue(FB_Users.class);


                        fb_users.setId(postSnapshot.getKey());
                        taskList.add(fb_users);

                        recyclerviewAdapter_userInfo.setUsersList(taskList);
                        recyclerView.setAdapter(recyclerviewAdapter_userInfo);
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
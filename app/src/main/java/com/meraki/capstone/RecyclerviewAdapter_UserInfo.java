package com.meraki.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerviewAdapter_UserInfo extends RecyclerView.Adapter<RecyclerviewAdapter_UserInfo.MyViewHolder> {

    private Context mContext;
    private static List<FB_Users> usersList;

    private static final String TAG = "logger_RAUsersList"; // Log

    FB_Users FB_Users;
    public static CountDownTimer countDownTimer;
    public static Boolean countDownTimer_running = false;
    View view;

    public RecyclerviewAdapter_UserInfo(Context context) {
        mContext = context;
        usersList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_user_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FB_Users = usersList.get(position);


        if (FB_Users.getKeycode().equals("NA") || FB_Users.getName().equals("NA")) {
            holder.relData.setVisibility(View.GONE);
            holder.relAddData.setVisibility(View.VISIBLE);

        } else {

            holder.relAddData.setVisibility(View.GONE);
            holder.relData.setVisibility(View.VISIBLE);

            holder.usersKey.setText("Key: "+ FB_Users.getKeycode());
            holder.usersName.setText(FB_Users.getName());


            if (FB_Users.isMaster() == false) {
                holder.radioMaster.setChecked(false);
                holder.masterStatus.setVisibility(View.GONE);
            } else {
                holder.radioMaster.setChecked(true);
                holder.masterStatus.setVisibility(View.VISIBLE);
                //Toast.makeText(mContext, FB_Users.getName() + " is the Master", Toast.LENGTH_SHORT).show();
            }

            Log.i(TAG, FB_Users.getId());
            holder.radioMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    setMasterStatus(position);

//                    if (FB_Users.isMaster()) {
//                        holder.radioMaster.setChecked(false);
//
//                    }
//                    else {
//                        holder.radioMaster.setChecked(true);
//
//                    }


                }
            });


        }


    }


    public void setMasterStatus(int position) {
        try {


            for (int a = 0; a < 5; a++) {
                FB_Users = usersList.get(a);
                if (a == position) {
                    FirebaseDatabase.getInstance().getReference().child("Valid").child(FB_Users.getId()).child("master").setValue(true);

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Valid").child(FB_Users.getId()).child("master").setValue(false);

                }
            }


        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setUsersList(List<FB_Users> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public static TextView usersKey, usersName, masterStatus;
        public static RadioButton radioMaster;
        public static RelativeLayout relData, relAddData;

        public MyViewHolder(View itemView) {
            super(itemView);
            usersKey = itemView.findViewById(R.id.usersKey);
            usersName = itemView.findViewById(R.id.usersName);
            masterStatus = itemView.findViewById(R.id.masterStatus);
            radioMaster = itemView.findViewById(R.id.radioMaster);

            relData = itemView.findViewById(R.id.rel_data);
            relAddData = itemView.findViewById(R.id.rel_addData);

        }


    }
}
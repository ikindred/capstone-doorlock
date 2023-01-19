package com.meraki.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapter_LogAlarmTime extends RecyclerView.Adapter<RecyclerviewAdapter_LogAlarmTime.MyViewHolder> {

    private Context mContext;
    private static List usersList;

    private static final String TAG = "logger_RALogsList"; // Log

    View view;

    public RecyclerviewAdapter_LogAlarmTime(Context context) {
        mContext = context;
        usersList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_log_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // usersList.get(position);



        String[] data = usersList.get(position).toString().split("#");

        if(data[0].trim().equalsIgnoreCase("Door Alarm")){
            holder.usersKey.setText(data[0].trim());
            holder.usersKey.setTextColor(Color.RED);
        }else{
            holder.usersKey.setText("Key: "+data[0].trim());
        }

        holder.usersDate.setText(data[1].trim());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setUsersList(List usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public static TextView usersKey, usersDate;


        public MyViewHolder(View itemView) {
            super(itemView);
            usersKey = itemView.findViewById(R.id.usersKey);
            usersDate = itemView.findViewById(R.id.usersDate);
        }


    }
}
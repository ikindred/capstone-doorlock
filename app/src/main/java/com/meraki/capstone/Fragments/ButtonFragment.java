package com.meraki.capstone.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meraki.capstone.R;
import com.skyfishjy.library.RippleBackground;

import java.util.concurrent.TimeUnit;


public class ButtonFragment extends Fragment {

    public ButtonFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "logger_ButtonFragment";
    View view;
    Button buttonLock;
    ImageView imageLock;
    private DatabaseReference dbRef;
    ValueEventListener mSendEventListner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_button, container, false);
        imageLock = (ImageView) view.findViewById(R.id.image_lock);
        buttonLock(view);


        try {


            dbRef = FirebaseDatabase.getInstance().getReference().child("Lock").child("status");

            ValueEventListener valueEventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        if(dataSnapshot.getValue().toString().equalsIgnoreCase("true")){
                            Toast.makeText(getContext(), "Door locked", Toast.LENGTH_SHORT).show();
                            imageLock.setImageResource(R.drawable.padlock);
                            Log.d(TAG,  "Door locked");
                        }else{
                            Toast.makeText(getContext(), "Door unlocked", Toast.LENGTH_SHORT).show();
                            imageLock.setImageResource(R.drawable.lock_open_alt);
                            Log.d(TAG,  "Door unlocked");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //
                }
            };
            dbRef.addValueEventListener(valueEventListener);
            mSendEventListner = valueEventListener;


        }catch (Exception e){
        }
        return view;
    }


    private void buttonLock(View view) {
      //  final RippleBackground rippleBackground=(RippleBackground)view.findViewById(R.id.content);
        buttonLock = (Button) view.findViewById(R.id.button_lock);
        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                   // rippleBackground.startRippleAnimation();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Lock").child("status");
                    databaseReference.setValue(false, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                           // rippleBackground.stopRippleAnimation();
                        }

                    });
                }catch (Exception e){
                    Log.d(TAG,  e.getMessage());
                }


            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getContext(), "onDestroyView", Toast.LENGTH_SHORT).show();
        if (mSendEventListner != null) {
            dbRef.removeEventListener(mSendEventListner);
        }    }
}
package com.meraki.capstone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ViewDialog_AddUser {

    Activity activity;
    public static Dialog dialog;
    private static final String TAG = "kyra_Loading"; // Log
    ImageView close;

    EditText edittext_name;
    TextView keyvalue;
    RelativeLayout addKey;
    Button adduser;

    long dateInLong = 0;

    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog_AddUser(Activity activity) {
        this.activity = activity;

    }

    public void initiateDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_adduser);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        close = (ImageView) dialog.findViewById(R.id.close);
        edittext_name = (EditText) dialog.findViewById(R.id.edittext_name);
        adduser = (Button) dialog.findViewById(R.id.button_adduser);
        keyvalue = (TextView) dialog.findViewById(R.id.keyvalue);
        addKey = (RelativeLayout) dialog.findViewById(R.id.addKey);

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, edittext_name.getText().toString());
                Log.i(TAG, "" + dateInLong);

                if (!edittext_name.getText().toString().isEmpty() && dateInLong != 0) {

                  //  DatabaseAddTour(edittext_name.getText().toString(), ("" + dateInLong));
                } else {
                    Toast.makeText(activity, "Please fill-up the blanks", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                clear();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        addKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    // rippleBackground.startRippleAnimation();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RegisterData").child("scan");
                    databaseReference.setValue(true, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            keyvalue.setText("If the Device Light turns Red you can tap your id to scan");
                        }

                    });
                }catch (Exception e){
                    Log.d(TAG,  e.getMessage());
                }


            }
        });
    }


    public void showDialog() {
        try {
            dialog.show();

        } catch (Exception ex) {
            Log.e(TAG, "xception: " + ex.getMessage());
        }
    }

    public void hideDialog() {
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, 100);

        } catch (Exception ex) {
            Log.e(TAG, "xception: " + ex.getMessage());
        }


    }

//    private void DatabaseAddTour(String name, String addKey) {
//
//        try {
//
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//            String key = databaseReference.child("Valid").child(uID)
//                    .child("tours").push().getKey();
//
//            FB_Users fb_tours = new FB_Users(key, name, addKey);
//
//            databaseReference.child("AccountsTB").child(uID)
//                    .child("tours").child(key).setValue(fb_tours, new DatabaseReference.CompletionListener() {
//
//                @Override
//                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//
//                    databaseReference.child("ToursTB").child(uID).child(key).setValue(fb_tours, new DatabaseReference.CompletionListener() {
//
//                        @Override
//                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                           // loading.hideDialog("");
//                            hideDialog();
//                            PublicVariables.tourID = key;
//                            PublicVariables.tourName = fb_tours.getName();
//                            PublicVariables.tourdatetime = fb_tours.getDatetime();
//
//                            clear();
//
//                            Intent intent = new Intent(activity, ActivityNavigation.class);
//                            activity.startActivity(intent);
//                            activity.finish();
//
//                        }
//                    });
//                }
//            });
//
//        } catch (Exception e) {
//            Toast.makeText(activity, "Please try again...", Toast.LENGTH_SHORT).show();
//            loading.hideDialog("");
//        }
//
//
//    }

    private void clear() {
        edittext_name.setText("");

        keyvalue.setText("Add RFID");
    }

}
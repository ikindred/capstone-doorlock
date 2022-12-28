package com.meraki.capstone;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewDialog_EditUser {

    Activity activity;
    public static Dialog dialog;
    private static final String TAG = "logger_AddUser"; // Log
    ImageView close;

    EditText edittext_name;
    TextView keyvalue;
    RelativeLayout addKey;
    Button edituser, deleteuser, button_setMaster;
    private DatabaseReference dbRef;

    ValueEventListener mSendEventListner;
    String ScannedKeyCode = "NA";
    boolean masterStatus = false;
    String UserID = "", ID, Name;
    ImageView imgSignal;

    ProgressDialog dialogLoading;
    public static List<FB_Users> taskList = new ArrayList<>();
    int myPosition = 0;
    FB_Users FB_Users;

    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog_EditUser(Activity activity) {
        this.activity = activity;

    }

    public void initiateDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_edituser);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        taskList = new ArrayList<>();
        close = (ImageView) dialog.findViewById(R.id.close);
        edittext_name = (EditText) dialog.findViewById(R.id.edittext_name);
        edituser = (Button) dialog.findViewById(R.id.button_edituser);
        deleteuser = (Button) dialog.findViewById(R.id.button_deleteuser);
        button_setMaster = (Button) dialog.findViewById(R.id.button_setMaster);
        keyvalue = (TextView) dialog.findViewById(R.id.keyvalue);
        addKey = (RelativeLayout) dialog.findViewById(R.id.addKey);
        imgSignal = (ImageView) dialog.findViewById(R.id.imgSignal);


        deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, edittext_name.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("Valid").child(UserID).child("keycode").setValue("NA");
                FirebaseDatabase.getInstance().getReference().child("Valid").child(UserID).child("name").setValue("NA");
                FirebaseDatabase.getInstance().getReference().child("Valid").child(UserID).child("master").setValue(false);
                hideDialog();
            }
        });

        button_setMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int a = 0; a < 5; a++) {
                    FB_Users = taskList.get(a);
                    if (a == myPosition) {
                        FirebaseDatabase.getInstance().getReference().child("Valid").child(FB_Users.getId()).child("master").setValue(true);

                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Valid").child(FB_Users.getId()).child("master").setValue(false);

                    }
                }
                hideDialog();

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                clear();
                FirebaseDatabase.getInstance().getReference().child("RegUser").child("newkeycode").setValue("NA");
                FirebaseDatabase.getInstance().getReference().child("RegUser").child("newuser").setValue(false);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        edituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, edittext_name.getText().toString());


                if (!edittext_name.getText().toString().isEmpty()) {

                    if (!edittext_name.getText().toString().equalsIgnoreCase("NA")) {
                        if (!ScannedKeyCode.equalsIgnoreCase("NA")) {

                            DatabaseEditUser(edittext_name.getText().toString(), ScannedKeyCode);
                        } else {
                            Toast.makeText(activity, "Please click 'Update RFID' to update your ID", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "'NA' is not valid", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(activity, "Please add your name", Toast.LENGTH_SHORT).show();
                }

            }
        });


        try {


            dbRef = FirebaseDatabase.getInstance().getReference().child("RegUser");
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        final FB_RegisterData fb_registerData = dataSnapshot.getValue(FB_RegisterData.class);

                        if (!fb_registerData.getNewkeycode().equals("NA") && fb_registerData.isNewuser() == false) {

                            Log.d(TAG, "scanned keycode =" + fb_registerData.getNewkeycode());
                            ScannedKeyCode = fb_registerData.getNewkeycode();
                            imgSignal.setColorFilter(Color.argb(255, 67, 179, 36));
                            keyvalue.setText("Your ID: " + ScannedKeyCode);
                        //    Toast.makeText(activity, "Your ID: " + ScannedKeyCode, Toast.LENGTH_SHORT).show();
                            addKey.setEnabled(true);
                        } else if (fb_registerData.getNewkeycode().equals("NA") && fb_registerData.isNewuser() == false) {

                            keyvalue.setText("Scan RFID");
                            addKey.setEnabled(true);
                            imgSignal.setColorFilter(Color.argb(255, 175, 175, 175));
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


        } catch (Exception e) {

        }


//        if (mSendEventListner != null) {
//            dbRef.removeEventListener(mSendEventListner);
//        }

        addKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addKey.setEnabled(false);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RegUser").child("newuser");
                    databaseReference.setValue(true, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            keyvalue.setText("Scan your ID");
                            imgSignal.setColorFilter(Color.argb(255, 250, 140, 80));
                            FirebaseDatabase.getInstance().getReference().child("RegUser").child("newuser").setValue(true);
                        }

                    });
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }


            }
        });
    }


    public void showDialog(String SelectedUserID, String SelectedID, String SelectedName, boolean Selectedmaster, List<FB_Users> PasstaskList, int position) {

        try {

            FirebaseDatabase.getInstance().getReference().child("RegUser").child("newkeycode").setValue("NA");
            FirebaseDatabase.getInstance().getReference().child("RegUser").child("newuser").setValue(false);

            UserID = SelectedUserID;
            taskList = PasstaskList;
            myPosition = position;

            ScannedKeyCode = SelectedID;
            edittext_name.setText(SelectedName);
            masterStatus = Selectedmaster;

            if (masterStatus == true) {
                button_setMaster.setEnabled(false);
                button_setMaster.setText("Already set as Master");
            } else {
                button_setMaster.setEnabled(true);
                button_setMaster.setText("Set as Master");
            }

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
                        FirebaseDatabase.getInstance().getReference().child("RegUser").child("newkeycode").setValue("NA");
                        FirebaseDatabase.getInstance().getReference().child("RegUser").child("newuser").setValue(false);
                        dialog.dismiss();
                    }
                }
            }, 100);

        } catch (Exception ex) {
            Log.e(TAG, "xception: " + ex.getMessage());
        }


    }

    private void DatabaseEditUser(String name, String addKey) {

        try {

            dialogLoading = ProgressDialog.show(dialog.getContext(), "",
                    "Loading. Please wait...", true);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            FB_Users fb_users = new FB_Users(addKey, name, null, masterStatus);
            dialogLoading.show();
            databaseReference.child("Valid").child(UserID).setValue(fb_users, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    dialogLoading.dismiss();
                    clear();
                    hideDialog();
                }
            });

        } catch (Exception e) {
            dialogLoading.dismiss();
            Toast.makeText(activity, "Please try again...", Toast.LENGTH_SHORT).show();

        }


    }

    private void clear() {
        edittext_name.setText("");

        keyvalue.setText("Update RFID");
    }

}
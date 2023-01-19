package com.meraki.capstone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPageAdapter myViewPageAdapter;

    LinearLayout linAuth;
    EditText edittext_code;
    Button button_authenticate;
    private static final String TAG = "logger_MainActivity";
    public static List taskList2 = new ArrayList<>();

    String lastLog = "";

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);


        linAuth = (LinearLayout) findViewById(R.id.lin_auth);
        edittext_code = (EditText) findViewById(R.id.edittext_code);
        button_authenticate = (Button) findViewById(R.id.button_authenticate);

        SharedPreferences mysharedRef = getSharedPreferences("CapstoneSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = mysharedRef.edit();

        dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);

        try {
            dialog.show();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Init");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    final FB_Auth fb_auth = snapshot.getValue(FB_Auth.class);

                    String code = mysharedRef.getString("code", "");
                    dialog.dismiss();
                    if (code.equals(fb_auth.getUnitcode()) && fb_auth.isUsed() == true) {
                        InitFragment();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

            button_authenticate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!edittext_code.getText().toString().isEmpty()) {
                        dialog.show();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Init");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                final FB_Auth fb_auth = snapshot.getValue(FB_Auth.class);

                                if (edittext_code.getText().toString().equals(fb_auth.getUnitcode())) {
                                    myEdit.putString("code", edittext_code.getText().toString());

                                    myEdit.commit();
                                    FirebaseDatabase.getInstance().getReference().child("Init").child("used").setValue(true);

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    FB_Users fb_users = new FB_Users("Mobile App", "Mobile App", null, false);

                                    databaseReference.child("Valid").child("user5").setValue(fb_users, new DatabaseReference.CompletionListener() {

                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                            dialog.dismiss();
                                            hideKeyboard(MainActivity.this);
                                            InitFragment();
                                        }
                                    });


                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Wrong Code", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(MainActivity.this, "Please enter unitcode", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        } catch (Exception e) {
            Log.w(TAG, "Exception: " + e.getMessage());
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


    }

    private void InitFragment() {

        myViewPageAdapter = new MyViewPageAdapter(this);
        viewPager2.setAdapter(myViewPageAdapter);

        linAuth.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager2.setVisibility(View.VISIBLE);
        taskList2 = new ArrayList<>();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

//        DatabaseReference databaseReference_alarmtime = FirebaseDatabase.getInstance().getReference().child("Logs").child("alarmtime");
//        databaseReference_alarmtime.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (!taskList2.isEmpty()) {
//                    taskList2.clear();
//                }
//
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//
//                    taskList2.add(postSnapshot.getValue());
//
//                }
//
//                if (taskList2.get(taskList2.size() - 1).toString().contains("Door Alarm")) {
//
//                    if (lastLog.isEmpty()) {
//                        lastLog = taskList2.get(taskList2.size() - 1).toString();
//                        Log.w(TAG, "last_log: " + lastLog);
//
//                    } else if (!lastLog.isEmpty() && !lastLog.equals(taskList2.get(taskList2.size() - 1).toString())) {
//                        lastLog = taskList2.get(taskList2.size() - 1).toString();
//                        Log.e(TAG, "notify here: " + lastLog);
//
//                        String split[] = lastLog.split("#");
//                        showNotification_message("Door Alarm", split[1]);
//
//                    }
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(TAG, "onCancelled: " + error.getMessage());
//            }
//        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public void showNotification_message(String title, String body) {
//
//        try {
//            Log.i(TAG, "showNotification_message");
//
//
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//            int notificationId = 0070;
//            String channelId = "meraki-7";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, channelId)
//                    .setSmallIcon(R.mipmap.ic_launcher_round)
//                    .setContentTitle(title)
//                    .setAutoCancel(true)
//                    .setContentText(body)
//                    .setSound(alarmSound)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setContentIntent(pendingIntent);
//
//            androidx.core.app.TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
//            stackBuilder.addNextIntent(intent);
//
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(notificationId, mBuilder.build());
//
//        } catch (Exception e) {
//            Log.e(TAG, "showNotification_call: " + e.getMessage());
//
//        }
//
//    }

}
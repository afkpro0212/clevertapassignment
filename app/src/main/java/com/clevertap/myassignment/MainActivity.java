package com.clevertap.myassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {





    private void pushProductEventView(){

    }
    public static MainActivity shareInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // clevertapDefaultInstance.pushEvent("ProductViewed1234");
        //Push User Profile First Time Login
        SharedPreferences sharePrefer = getSharedPreferences("MyPreference", MODE_PRIVATE);
        String s1 = sharePrefer.getString("isFirstTimeLogin", "");

        //if this is the firsttime use the app, add the user profile into the Clever server tap
        if(s1==null || s1.equals("")){
            SharedPreferences.Editor myEdit = sharePrefer.edit();
            myEdit.putString("isFirstTimeLogin", "true");
            myEdit.commit();
            pushUserProfileFirstTimeLogin(clevertapDefaultInstance);
        }

        shareInstance =  this;
        Log.d("BUTTONS", "User tapped the Supabutton");

        functionUpdateButtonProfile(clevertapDefaultInstance);
        functionButtonAddEvent(clevertapDefaultInstance);

        CleverTapAPI.createNotificationChannel(getApplicationContext(),"HungLeID","HungLeChannel","Your Channel Description",NotificationManager.IMPORTANCE_MAX,true);

        // Creating a Notification Channel With Sound Support
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(Task<String> task) {
                        if (!task.isSuccessful()) {
                            //   Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println("TOKEN " + token);

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void pushUserProfileFirstTimeLogin(CleverTapAPI clevertapDefaultInstance){
        try{
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", "Jack le");    // String
            profileUpdate.put("Email", "hung6789@gmail.com"); // Email address of the user
            profileUpdate.put("Phone", "+84918828891");   // Phone (with the country code, starting with +)
            profileUpdate.put("Gender", "M");             // Can be either M or F
            profileUpdate.put("DOB", new Date());
            profileUpdate.put("MSG-push", true);
            clevertapDefaultInstance.onUserLogin(profileUpdate);
        }
        catch (Exception ex){

        }
    }

    private void functionButtonAddEvent(CleverTapAPI clevertapDefaultInstance)
    {
        Button button = (Button) findViewById(R.id.pushButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try{
                    HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                    prodViewedAction.put("ProductID", "1");
                    prodViewedAction.put("ProductImage", "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg");
                    prodViewedAction.put("ProductName", "CleverTap");
                    prodViewedAction.put("DateView", new java.util.Date());
                    clevertapDefaultInstance.pushEvent("HungLeProductViewed", prodViewedAction);
                    Toast toast = Toast.makeText(shareInstance, "Add Event Successfully", Toast.LENGTH_LONG);
                    toast.show();
                    //Log.d("BUTTONS", "User tapped the Supabutton");
                }
                catch (Exception ex){
                    Toast toast = Toast.makeText(shareInstance, "Add Event Error", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
    }



    private void functionUpdateButtonProfile(CleverTapAPI clevertapDefaultInstance){
        Button buttonUpdateUSerProfile = (Button) findViewById(R.id.updateUserProfileButton);
        buttonUpdateUSerProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Add user login into
                try
                {
                    String emailAddress = "";
                    EditText  editText =  (EditText) findViewById(R.id.editText);
                    HashMap<String, Object> profileUpdate = new HashMap<String, Object>();// String or number
                    profileUpdate.put("Email", editText.getText().toString().toLowerCase());
                    profileUpdate.put("MSG-push", true);
                    clevertapDefaultInstance.pushProfile(profileUpdate);
                    Toast toast = Toast.makeText(shareInstance, "Update Profile Successfully", Toast.LENGTH_LONG);
                    toast.show();
                    Log.d("BUTTONS", "pushUserProfileButton tapped the Supabutton");
                }
                catch (Exception ex){
                    Toast toast = Toast.makeText(shareInstance, "Update Profile Error", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
    }
}
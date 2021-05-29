package com.example.icecreamapplication;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/*
public class DataBaseHandler {
    private DatabaseReference reff;
    private final String TAG = "databaseHandler";
    private int temp;
    public DataBaseHandler() {
        reff = FirebaseDatabase.getInstance().getReference();

    }

    public void uploadDataToDb() {
//        reff.child("test1").push().setValue(new DateClass(1900, 1, 1));//write data to db
        for(int i=0;i<50;i++) {
            reff.child("users").child(String.valueOf(i)).setValue(new User("a", "a","1/1/1911", false));
        }
        Log.d(TAG, "connection successes");
    }
   public  void getNumberOfUsers(){
        final int[] returnValue =new int [1];
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                DateClass dateClass;
                Log.d(TAG,"before: "+temp);
               temp = Integer.parseInt(String.valueOf(dataSnapshot.child("users").getChildrenCount()));
               returnValue[0] = Integer.parseInt(String.valueOf(dataSnapshot.child("users").getChildrenCount()));
               Log.d(TAG+"hh",String.valueOf(dataSnapshot.child("users").getChildrenCount()));
//                        Log.d("Database", "as "+dataSnapshot.child("users").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        Log.d(TAG,String.valueOf(temp));
    }
    public  void getData(){
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DateClass dateClass;
                Log.d(TAG, "as "+dataSnapshot.child("users").getChildrenCount());
//                        Log.d("Database", "as "+dataSnapshot.child("users").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }
}*/

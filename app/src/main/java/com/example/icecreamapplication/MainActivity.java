package com.example.icecreamapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button buttonRead,buttonWrite;
    String TAG= "Main Activity";
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonRead = (Button) findViewById(R.id.button);
        buttonWrite = (Button) findViewById(R.id.button1);
        reff = FirebaseDatabase.getInstance().getReference();
        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                reff.child("test1").push().setValue(new DateClass(1900,1,1));//write data to db
                reff.child("users").child("0").setValue(new User("a","1/1/1910",false));
                Toast.makeText(MainActivity.this, "connection successes", Toast.LENGTH_SHORT).show();
            }
        });
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Database", "as "+dataSnapshot.child("test").getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }
        });

    }
}
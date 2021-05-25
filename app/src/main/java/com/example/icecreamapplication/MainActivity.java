package com.example.icecreamapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    String TAG= "Main Activity";
    User userClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userClass = (User) getIntent().getSerializableExtra("USER_CLASS");

    }
}
package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class LogInActivity extends AppCompatActivity {
    TextView tvPassword, tvEmail, tvRegister;
    Button btLogIn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String TAG = "LogInActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        tvPassword= (TextView) findViewById(R.id.tv_password);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        btLogIn= (Button) findViewById(R.id.log_bt_login);
        tvRegister = (TextView) findViewById(R.id.bt_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  String.valueOf(tvEmail.getText());
                String password = String.valueOf(tvPassword.getText());
                password = password.replaceAll("\\s+","");//delete all spaces in the password
                if(email.length()>0&&password.length()>0) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser userLogedIn = FirebaseAuth.getInstance().getCurrentUser();

                            DocumentReference documentReference = firestore.collection("users").document(userLogedIn.getUid());
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = new User(documentSnapshot.getData());
                                    DocumentReference documentReference = firestore.collection("orders").document(userLogedIn.getUid());
                                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot1) {
                                            user.setMapOfOrders(documentSnapshot1.getData());
                                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                            intent.putExtra("USER_CLASS", user);
                                            Log.d("pass data", user.toString());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(LogInActivity.this, "failed 2 log in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(LogInActivity.this, "failed 2 log in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(LogInActivity.this, "please enter email/ password" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
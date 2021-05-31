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
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        tvPassword = (TextView) findViewById(R.id.tv_password);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        btLogIn = (Button) findViewById(R.id.log_bt_login);
        tvRegister = (TextView) findViewById(R.id.bt_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(tvEmail.getText());
                String password = String.valueOf(tvPassword.getText());
                password = password.replaceAll("\\s+", "");//delete all spaces in the password
                if (email.length() > 0 && password.length() > 0) {
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
                                            Intent intent;
                                            if (user.isAdmin()) {
//                                                user.setMapOfOrders();
                                                intent = new Intent(LogInActivity.this, AdminActivity.class);
//                                                intent.putExtra("USER_CLASS", user);
                                                order(intent,user);
//                                                startActivity(intent);

                                            } else {
                                                intent = new Intent(LogInActivity.this, MainActivity.class);
                                                user.setOrderClasses(user.getListOfOrdersFromList());
                                                intent.putExtra("USER_CLASS", user);
                                                Log.d("pass data", user.toString());
                                                startActivity(intent);
                                            }

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
                } else {
                    Toast.makeText(LogInActivity.this, "please enter email/ password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void order(Intent intent,User user) {
        firestore.collection("orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<OrderClass>l = new ArrayList<>();
                List<UserIdentifier> orders = new ArrayList<>();
                for (int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                    List<OrderClass>temp = user.getListOfOrdersFromMap(queryDocumentSnapshots.getDocuments().get(i).getData());
                    for (int y=0;y<temp.size();y++){
                        temp.get(y).setUserId(queryDocumentSnapshots.getDocuments().get(i).getId());
                    }
                    orders.add(new UserIdentifier(temp,queryDocumentSnapshots.getDocuments().get(i).getId()));
                    l.addAll(temp);
                }
                user.setListOfOrders(orders);
                user.setOrderClasses(l);
                intent.putExtra("USER_CLASS", user);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG, "we failed:" + e.getMessage());
            }
        });


    }
}
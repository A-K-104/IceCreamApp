package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
        tvPassword = findViewById(R.id.tv_password);
        tvEmail = findViewById(R.id.tv_email);
        tvRegister = findViewById(R.id.bt_register);
        btLogIn = findViewById(R.id.log_bt_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        /**
         * first we set the loading indicator
         * then we get the info from the text view.
         * then we pass the data to db to log in.
         * we check if user is admin (different screen and data)
         * then we pull all the data to userClass (user data, and orders)
         * and move to class
         */
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog loadingIndicator = new ProgressDialog(LogInActivity.this);
                loadingIndicator.setMessage("logging in");
                loadingIndicator.show();
                String email = String.valueOf(tvEmail.getText());
                String password = String.valueOf(tvPassword.getText());
                password = password.replaceAll("\\s+", "");//delete all spaces in the password
                if (email.length() > 0 && password.length() > 0) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
                            DocumentReference documentReference = firestore.collection("users").document(userLoggedIn.getUid());
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User tempUser = new User(documentSnapshot.getData());
                                    DocumentReference documentReference = firestore.collection("orders").document(userLoggedIn.getUid());
                                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot1) {
                                            tempUser.setMapOfOrders(documentSnapshot1.getData());
                                            Intent intent;
                                            if (tempUser.isAdmin()) {
                                                intent = new Intent(LogInActivity.this, AdminActivity.class);
                                                adminLogIn(intent, tempUser);
                                            } else {
                                                intent = new Intent(LogInActivity.this, MainActivity.class);
                                                tempUser.getListOfOrders().add(new OrderIdentifier(tempUser.getListOfOrdersFromMap()));
                                                intent.putExtra("USER_CLASS", tempUser);
                                                Log.d("pass data", tempUser.toString());
                                                startActivity(intent);
                                            }
                                            loadingIndicator.cancel();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(LogInActivity.this, "failed 2 log in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            loadingIndicator.cancel();
                                        }
                                    });
                                    ;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(LogInActivity.this, "failed 2 log in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    loadingIndicator.cancel();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(LogInActivity.this, "failed 2 log in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingIndicator.cancel();
                        }
                    });
                } else {
                    Toast.makeText(LogInActivity.this, "please enter email/ password", Toast.LENGTH_SHORT).show();
                    loadingIndicator.cancel();
                }

            }
        });
        /**
         * move to resistor activity
         */
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * in this function we get all the data of orders and we convert them to
     * list<OrderIdentifier> were we push data and id of users.
     * @param intent for admin screen
     * @param user get the user
     */
    public void adminLogIn(Intent intent, User user) {
        firestore.collection("orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<OrderIdentifier> orders = new ArrayList<>();
                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                    orders.add(new OrderIdentifier(user.getListOfOrdersFromMap(queryDocumentSnapshots.getDocuments().get(i).getData()),
                            queryDocumentSnapshots.getDocuments().get(i).getId()));
                }
                user.setListOfOrders(orders);
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
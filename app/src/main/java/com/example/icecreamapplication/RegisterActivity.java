package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {
    TextView firstNameTv, lastNameTv, passwordTv, loginTv, emailTv;
    DatePicker dateOfBirthDP;
    Button registerBt;
    Switch genderSw;
    TextView tvPasswordText, tvFirstNameText, tvLastNameText, tvEmailText;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstNameTv = findViewById(R.id.reg_name_et);
        lastNameTv = findViewById(R.id.reg_last_name_et);
        passwordTv = findViewById(R.id.reg_password_et);
        emailTv = findViewById(R.id.reg_email_et);
        tvPasswordText = findViewById(R.id.reg_password_tv);
        tvFirstNameText = findViewById(R.id.reg_name_tv);
        tvLastNameText = findViewById(R.id.reg_last_name_tv);
        tvEmailText = findViewById(R.id.reg_email_tv);
        loginTv = findViewById(R.id.reg_tv_login);
        dateOfBirthDP = findViewById(R.id.reg_date_of_birth_picker);
        registerBt = findViewById(R.id.reg_bt_resistor);
        genderSw = findViewById(R.id.reg_gender_sw);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        /**
         * button that returns us to the previous activity
         */
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * register button
         * it is testing the values of input if ok will save the values and move activity
         * else will show message and mak in red the issue
         */
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * first we rest all text color to black
                 */
                firstNameTv.setTextColor(Color.BLACK);
                tvFirstNameText.setTextColor(Color.BLACK);
                passwordTv.setTextColor(Color.BLACK);
                tvPasswordText.setTextColor(Color.BLACK);
                lastNameTv.setTextColor(Color.BLACK);
                tvLastNameText.setTextColor(Color.BLACK);
                tvEmailText.setTextColor(Color.BLACK);
                emailTv.setTextColor(Color.BLACK);
                if ((!String.valueOf(firstNameTv.getText()).equals("")) && //if the user name isn't empty
                        ((String.valueOf(passwordTv.getText()).length() > 5)) &&//if the password is longer then 5
                        (String.valueOf(lastNameTv.getText())).length() > 1 &&
                        (String.valueOf(emailTv.getText())).length() > 1) {//if the last name value
                    /**
                     * create the new User from the data in textBox's
                     * email, password, firstName, lastName, dateOfBirth, gender (as bool)
                     * then we upload the data to db
                     * and last it will start new activity
                     */
                    ProgressDialog loadingIndicator = new ProgressDialog(RegisterActivity.this);//loading animation
                    loadingIndicator.setMessage("creating user");
                    loadingIndicator.show();
                    firebaseAuth.createUserWithEmailAndPassword(String.valueOf(emailTv.getText()), String.valueOf(passwordTv.getText())).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            User user = new User(String.valueOf(firstNameTv.getText()), String.valueOf(lastNameTv.getText()),
                                    dateOfBirthDP.getDayOfMonth() + "/" + dateOfBirthDP.getMonth() + "/" + dateOfBirthDP.getYear(),
                                    genderSw.isChecked()
                            );
                            FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
                            DocumentReference documentReference = firestore.collection("users").document(userLoggedIn.getUid());
                            documentReference.set(user.getUserMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.putExtra("USER_CLASS", user);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "failed 2 upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            loadingIndicator.cancel();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            /**
                             * if the email is in use/false we mark it red
                             */
                            Toast.makeText(RegisterActivity.this, "failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            if (e.getMessage().contains("email")) {
                                emailTv.setTextColor(Color.RED);
                                tvEmailText.setTextColor(Color.RED);
                                Toast.makeText(RegisterActivity.this, "Failed to register, need email", Toast.LENGTH_SHORT).show();
                            }
                            loadingIndicator.cancel();
                        }
                    });


                } else {
                    /**
                     * we are retesting all data by itself and then mark the wrong one
                     */

                    if (String.valueOf(firstNameTv.getText()).equals("")) {
                        firstNameTv.setTextColor(Color.RED);
                        tvFirstNameText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, need user name", Toast.LENGTH_SHORT).show();
                    }
                    if (String.valueOf(lastNameTv.getText()).length() <= 1) {
                        lastNameTv.setTextColor(Color.RED);
                        tvLastNameText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, need last name", Toast.LENGTH_SHORT).show();
                    }
                    if (String.valueOf(emailTv.getText()).length() <= 1) {
                        emailTv.setTextColor(Color.RED);
                        tvEmailText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, need email", Toast.LENGTH_SHORT).show();
                    }
                    if (String.valueOf(passwordTv.getText()).length() <= 5) {
                        passwordTv.setTextColor(Color.RED);
                        tvPasswordText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, password to short at list 6 letters", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
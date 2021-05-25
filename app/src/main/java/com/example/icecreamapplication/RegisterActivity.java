package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    TextView firstNameTv, lastNameTv,passwordTv,loginTv,emailTv;
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
        firstNameTv = (TextView) findViewById(R.id.reg_name_et);
        lastNameTv = (TextView) findViewById(R.id.reg_last_name_et);
        passwordTv = (TextView) findViewById(R.id.reg_password_et);
        emailTv = (TextView) findViewById(R.id.reg_email_et);
        dateOfBirthDP = (DatePicker) findViewById(R.id.reg_date_of_birth_picker);
        registerBt = (Button) findViewById(R.id.reg_bt_resistor);
        genderSw = (Switch) findViewById(R.id.reg_gender_sw);
        tvPasswordText= (TextView) findViewById(R.id.reg_password_tv);
        tvFirstNameText = (TextView) findViewById(R.id.reg_name_tv);
        tvLastNameText = (TextView) findViewById(R.id.reg_last_name_tv);
        tvEmailText = (TextView) findViewById(R.id.reg_email_tv);
        loginTv= (TextView) findViewById(R.id.reg_tv_login);
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
                firstNameTv.setTextColor(Color.BLACK);
                tvFirstNameText.setTextColor(Color.BLACK);
                passwordTv.setTextColor(Color.BLACK);
                tvPasswordText.setTextColor(Color.BLACK);
                lastNameTv.setTextColor(Color.BLACK);
                tvLastNameText.setTextColor(Color.BLACK);
                tvEmailText.setTextColor(Color.BLACK);
                emailTv.setTextColor(Color.BLACK);
                if ((!String.valueOf(firstNameTv.getText()).equals("")) && //if the user name isn't empty
                        ((String.valueOf(passwordTv.getText()).length() >5)) &&//if the password is longer then 5
                        (String.valueOf(lastNameTv.getText())).length()>1&&
                        (String.valueOf(emailTv.getText())).length()>1){//if the last name value
                    /**
                     * create the new User from the data in textBox's
                     * userName, weight, height, dateOfBirth, gender (as bool), zero point of counting steps
                     * then we upload the data to db
                     * and last it will start new activity
                     */firebaseAuth.createUserWithEmailAndPassword(String.valueOf(emailTv.getText()),String.valueOf(passwordTv.getText())).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            User user = new User(String.valueOf(firstNameTv.getText()), String.valueOf(lastNameTv.getText()),
                                    dateOfBirthDP.getDayOfMonth() + "/" + dateOfBirthDP.getMonth() + "/" + dateOfBirthDP.getYear(),
                                    genderSw.isChecked()
                            );
                            FirebaseUser userLogedIn = FirebaseAuth.getInstance().getCurrentUser();
                            DocumentReference documentReference= firestore.collection("user").document(userLogedIn.getUid());
                            documentReference.set(user.getUserMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

//                    databaseHandler.createNewRowOfData(userClass,String.valueOf(passwordTv.getText()));
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    intent.putExtra("USER_CLASS", user);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(RegisterActivity.this,"failed 2 upload data: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(RegisterActivity.this,"failed: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            if (e.getMessage().contains("email")){
                                emailTv.setTextColor(Color.RED);
                                tvEmailText.setTextColor(Color.RED);
                                Toast.makeText(RegisterActivity.this, "Failed to register, need email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    /**
                     * first we rest all text to black
                     * then we are retesting all data by itself and then mark the wrong one
                     */

                    if (String.valueOf(firstNameTv.getText()).equals("")) {
                        firstNameTv.setTextColor(Color.RED);
                        tvFirstNameText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, need user name", Toast.LENGTH_SHORT).show();
                    }
                    if (String.valueOf(lastNameTv.getText()).length()<=1) {
                        lastNameTv.setTextColor(Color.RED);
                        tvLastNameText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, need last name", Toast.LENGTH_SHORT).show();
                    }
                    if (String.valueOf(emailTv.getText()).length()<=1) {
                        emailTv.setTextColor(Color.RED);
                        tvEmailText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, need email", Toast.LENGTH_SHORT).show();
                    }
                    if (String.valueOf(passwordTv.getText()).length()<=5) {
                        passwordTv.setTextColor(Color.RED);
                        tvPasswordText.setTextColor(Color.RED);
                        Toast.makeText(RegisterActivity.this, "Failed to register, password to short at list 6 letters", Toast.LENGTH_SHORT).show();
                    }
//                    if (databaseHandler.returnUserIdByUserName(String.valueOf(userNameTv.getText())) != null) {
//                        userNameTv.setTextColor(Color.RED);
//                        tvUserNameText.setTextColor(Color.RED);
//                        Toast.makeText(RegisterActivity.this, "Failed to register, user name already in use", Toast.LENGTH_SHORT).show();
//                    }

                }
            }
        });
    }

    /**
     * this function test if a string value is parsable
     * @param value the string that you want to test
     * @return if it parsable it will return the number else null
     */
    public Double parseIntOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
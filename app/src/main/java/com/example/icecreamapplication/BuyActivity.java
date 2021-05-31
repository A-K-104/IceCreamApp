package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BuyActivity extends AppCompatActivity {
    Button btBuyVanilla,btBuyCookieAndCream,btBuyBlueberryCheesecake;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    User userClass;
    String TAG ="BuyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        userClass = (User) getIntent().getSerializableExtra("USER_CLASS");
        btBuyVanilla = (Button) findViewById(R.id.bt_buy_vanilla);
        btBuyCookieAndCream = (Button) findViewById(R.id.bt_buy_cookie_and_cream);
        btBuyBlueberryCheesecake = (Button) findViewById(R.id.bt_buy_blueberry_cheesecake);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btBuyVanilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass orderClass = new OrderClass("vanilla",userClass.getFirstName()+" "+userClass.getLastName());
                order(orderClass);
            }
        });
        btBuyBlueberryCheesecake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass orderClass = new OrderClass("blueberryCheesecake",userClass.getFirstName()+" "+userClass.getLastName());
                order(orderClass);
            }
        });
        btBuyCookieAndCream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass orderClass = new OrderClass("cookieAndCream",userClass.getFirstName()+" "+userClass.getLastName());
                order(orderClass);
            }
        });
    }
    public void order(OrderClass orderClass) {
        Map<String, Object> map = userClass.getMapOfOrders();
        ProgressDialog loadingIndicator = new ProgressDialog(BuyActivity.this);
        loadingIndicator.setMessage("creating order");
        loadingIndicator.show();
        if (map == null)
            map = new HashMap<>();
        map.put("order" + (map.size() + 1), orderClass);
        userClass.addSingleOrderClass(orderClass);
        FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = firestore.collection("orders").document(userLoggedIn.getUid());
        userClass.setMapOfOrders(map);
        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(BuyActivity.this, "order received successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BuyActivity.this,MainActivity.class);
                intent.putExtra("USER_CLASS", userClass);
                startActivity(intent);
                loadingIndicator.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(BuyActivity.this, "failed 2 upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingIndicator.cancel();
            }
        });
    }
}
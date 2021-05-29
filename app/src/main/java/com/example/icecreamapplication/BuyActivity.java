package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                OrderClass orderClass = new OrderClass("vanilla");
                order(orderClass);
            }
        });
        btBuyBlueberryCheesecake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass orderClass = new OrderClass("blueberryCheesecake");
                order(orderClass);
            }
        });
        btBuyCookieAndCream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass orderClass = new OrderClass("cookieAndCream");
                order(orderClass);
            }
        });
    }
    public void order(OrderClass orderClass){
        Map<String,Object> map = userClass.getMapOfOrders();
        if(map==null)
            map = new HashMap<>();
            map.put("order"+(map.size()+1),orderClass);
        FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference= firestore.collection("orders").document(userLoggedIn.getUid());
        userClass.setMapOfOrders(map);
        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

//                    databaseHandler.createNewRowOfData(userClass,String.valueOf(passwordTv.getText()));
                Log.d("sdsssssssssssssssssssss","success");

                firestore.collection("orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(Iterator<QueryDocumentSnapshot> i = queryDocumentSnapshots.iterator(); i.hasNext();){
                            Map<String,Object> userMap = i.next().getData();
                            Log.d("sdsssssssssssssssssssss","we are at:"+userMap.toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("ggggggggggg","we failed:"+e.getMessage());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(BuyActivity.this,"failed 2 upload data: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
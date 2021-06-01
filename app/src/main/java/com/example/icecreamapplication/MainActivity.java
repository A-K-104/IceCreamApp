package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    String TAG = "Main Activity";
    Button btNext, btPrevious, btBuy, btAbout;
    TextView lastOrderFlavor, lastOrderStatus, lastOrderDate;
    User userClass;
    int position = 0;
    List<OrderClass> listOfOrders;
    FirebaseFirestore firestore;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userClass = (User) getIntent().getSerializableExtra("USER_CLASS");
        btNext = (Button) findViewById(R.id.next_order_bt);
        btPrevious = (Button) findViewById(R.id.previous_order_bt);
        lastOrderFlavor = (TextView) findViewById(R.id.tv_last_order_flavor);
        lastOrderStatus = (TextView) findViewById(R.id.tv_last_order_status);
        lastOrderDate = (TextView) findViewById(R.id.tv_last_order_date);
        btBuy = (Button) findViewById(R.id.buy_bt);
        btAbout = (Button) findViewById(R.id.about_bt);
        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh);
        listOfOrders = userClass.getOrderClasses();
        firestore = FirebaseFirestore.getInstance();
        position = listOfOrders.size() - 1;
        if (listOfOrders != null && listOfOrders.toString() != "[]") {
            if (listOfOrders.get(position).getDateOfOrder() != null) {
                updateScreen();
            }
        }
        /**
         * moves to about activity
         */
        btAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                intent.putExtra("USER_CLASS", userClass);
                startActivity(intent);
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < listOfOrders.size() - 1) {
                    position++;
                } else {
                    Toast.makeText(MainActivity.this, "you are at the last order", Toast.LENGTH_SHORT).show();
                }
                if (listOfOrders != null) {
                    updateScreen();
                }
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position >= 1) {
                    position--;
                } else {
                    Toast.makeText(MainActivity.this, "you are at the first order", Toast.LENGTH_SHORT).show();
                }
                if (listOfOrders != null) {
                    updateScreen();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirebaseUser userLogedIn = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference documentReference = firestore.collection("orders").document(userLogedIn.getUid());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot1) {
                        userClass.setMapOfOrders(documentSnapshot1.getData());
                        userClass.setOrderClasses(userClass.getListOfOrdersFromList());
                        listOfOrders = userClass.getOrderClasses();
                        updateScreen();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MainActivity.this, "failed to update screen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void updateScreen() {
        lastOrderFlavor.setText(listOfOrders.get(position).getFlavor());
        lastOrderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
        lastOrderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
    }
}
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
        btNext = findViewById(R.id.next_order_bt);
        btPrevious = findViewById(R.id.previous_order_bt);
        btBuy = findViewById(R.id.buy_bt);
        btAbout = findViewById(R.id.about_bt);
        lastOrderFlavor = findViewById(R.id.tv_last_order_flavor);
        lastOrderStatus = findViewById(R.id.tv_last_order_status);
        lastOrderDate = findViewById(R.id.tv_last_order_date);
        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh);
        firestore = FirebaseFirestore.getInstance();
        /**
         * we get the last order and display it
         */
        if (userClass.getListOfOrders() != null&&userClass.getListOfOrders().size()>0) {
            listOfOrders = userClass.getListOfOrders().get(0).getOrderClasses();
            position = listOfOrders.size() - 1;
            if (listOfOrders.toString() != "[]") {
                if (listOfOrders.get(position).getDateOfOrder() != null) {
                    updateScreen();
                }
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
        /**
         * moves to buy activity
         */
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                intent.putExtra("USER_CLASS", userClass);
                startActivity(intent);
            }
        });
        /**
         * moves to next order if exist
         */
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listOfOrders != null) {
                    if (position < listOfOrders.size() - 1) {
                        position++;
                    } else {
                        Toast.makeText(MainActivity.this, "you are at the last order", Toast.LENGTH_SHORT).show();
                    }
                    if (listOfOrders != null) {
                        updateScreen();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "you don't have any orders", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         * moves to previous order if exist
         */
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listOfOrders != null) {
                    if (position >= 1) {
                        position--;
                    } else {
                        Toast.makeText(MainActivity.this, "you are at the first order", Toast.LENGTH_SHORT).show();
                    }
                    if (listOfOrders != null) {
                        updateScreen();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "you don't have any orders", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         * updating the most updated data from db and refresh the screen
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference documentReference = firestore.collection("orders").document(userLoggedIn.getUid());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot1) {
                        userClass.setMapOfOrders(documentSnapshot1.getData());
                        userClass.getListOfOrders().get(0).setOrderClasses(userClass.getListOfOrdersFromMap());
                        listOfOrders = userClass.getListOfOrders().get(0).getOrderClasses();
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

    /**
     * refresh screen function
     */
    public void updateScreen() {
        lastOrderFlavor.setText(listOfOrders.get(position).getFlavor());
        lastOrderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
        lastOrderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
    }
}
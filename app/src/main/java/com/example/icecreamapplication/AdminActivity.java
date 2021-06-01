package com.example.icecreamapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    User userClass;
    Button btNext, btPrevious, btNextStatus;
    TextView orderFlavor, orderStatus, orderDate, orderName;
    int position = 0, positionInOrder = 0;
    List<OrderIdentifier> orderIdentifierList;
    String TAG = "AdminActivity";
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userClass = (User) getIntent().getSerializableExtra("USER_CLASS");
        btNextStatus = findViewById(R.id.admin_next_status_bt);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btPrevious = findViewById(R.id.admin_previous_order_bt);
        btNext = findViewById(R.id.admin_next_order_bt);
        orderDate =  findViewById(R.id.tv_admin_order_date);
        orderStatus = findViewById(R.id.tv_admin_order_status);
        orderFlavor = findViewById(R.id.tv_admin_order_flavor);
        orderName = findViewById(R.id.tv_admin_order_name);
        swipeRefreshLayout = findViewById(R.id.admin_swipe_refresh);
        orderIdentifierList = userClass.getListOfOrders();
        if (orderIdentifierList.size() > 0) {
            position = orderIdentifierList.size() - 1;
            while (position > 0) {
                if (orderIdentifierList.get(position).getOrderClasses().toString() == "[]") {
                    position--;
                } else {
                    break;
                }
            }
            positionInOrder = orderIdentifierList.get(position).getOrderClasses().size() - 1;
            updateScreen();
        }
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((position < orderIdentifierList.size() - 1 && orderIdentifierList.get(position + 1).getOrderClasses().toString() != "[]") || positionInOrder < orderIdentifierList.get(position).getOrderClasses().size() - 1) {
                    if (positionInOrder < orderIdentifierList.get(position).getOrderClasses().size() - 1) {
                        positionInOrder++;
                    } else {
                        position++;
                        positionInOrder = 0;
                    }
                    updateScreen();
                } else {
                    Toast.makeText(AdminActivity.this, "you are at the last order", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((position >= 1 && orderIdentifierList.get(position - 1).getOrderClasses().toString() != "[]") || positionInOrder >= 1) {
                    if (positionInOrder >= 1) {
                        positionInOrder--;
                    } else {
                        position--;
                        positionInOrder = orderIdentifierList.get(position).getOrderClasses().size() - 1;
                    }
                    updateScreen();
                } else {
                    Toast.makeText(AdminActivity.this, "you are at the first order", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btNextStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass tempOrder = orderIdentifierList.get(position).getOrderClasses().get(positionInOrder);
                if (tempOrder.getStatusOfOrder() < 3) {
                    ProgressDialog loadingIndicator = new ProgressDialog(AdminActivity.this);
                    loadingIndicator.setMessage("updating order");
                    loadingIndicator.show();
                    Map<String, Object> map = new HashMap<>();
                    tempOrder.setStatusOfOrder(tempOrder.getStatusOfOrder() + 1);
                    orderIdentifierList.get(position).getOrderClasses().set(positionInOrder, tempOrder);
                    for (int i = 0; i < orderIdentifierList.get(position).getOrderClasses().size(); i++) {
                        map.put("order" + (i + 1), orderIdentifierList.get(position).getOrderClasses().get(i));
                    }

                    FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference documentReference = firestore.collection("orders").document(orderIdentifierList.get(position).getUserId());
                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            orderStatus.setText(orderIdentifierList.get(position).getOrderClasses().get(positionInOrder).getStatusOfOrderString());
                            Toast.makeText(AdminActivity.this, "order updated successfully ", Toast.LENGTH_SHORT).show();
                            loadingIndicator.cancel();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(AdminActivity.this, "failed 2 upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingIndicator.cancel();
                        }

                    });
                } else {
                    Toast.makeText(AdminActivity.this, "the order already arrived", Toast.LENGTH_SHORT).show();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestore.collection("orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<OrderIdentifier> orders = new ArrayList<>();
                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                            orders.add(new OrderIdentifier(userClass.getListOfOrdersFromMap(queryDocumentSnapshots.getDocuments().get(i).getData()),
                                    queryDocumentSnapshots.getDocuments().get(i).getId()));
                        }
                        userClass.setListOfOrders(orders);
                        orderIdentifierList = userClass.getListOfOrders();
                        updateScreen();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "we failed:" + e.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });
    }
    public void updateScreen() {
        orderFlavor.setText(orderIdentifierList.get(position).getOrderClasses().get(positionInOrder).getFlavor());
        orderStatus.setText(orderIdentifierList.get(position).getOrderClasses().get(positionInOrder).getStatusOfOrderString());
        orderDate.setText(orderIdentifierList.get(position).getOrderClasses().get(positionInOrder).getDateOfOrder().getDate());
        orderName.setText(orderIdentifierList.get(position).getOrderClasses().get(positionInOrder).getNameOfOrder());
    }
}
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    User userClass;
    Button btNext,btPrevious, btNextStatus;
    TextView  orderFlavor,orderStatus,orderDate,orderName;
    int position=0,positionInOrder = 0;
    List<OrderClass> listOfOrders;
    List<UserIdentifier> userIdentifierList;
    String TAG= "AdminActivity";
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userClass = (User) getIntent().getSerializableExtra("USER_CLASS");
        btNextStatus = (Button) findViewById(R.id.admin_next_status_bt);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btPrevious = (Button) findViewById(R.id.admin_previous_order_bt);
        btNext = (Button) findViewById(R.id.admin_next_order_bt);
        orderDate = (TextView) findViewById(R.id.tv_admin_order_date);
        orderStatus = (TextView) findViewById(R.id.tv_admin_order_status);
        orderFlavor = (TextView) findViewById(R.id.tv_admin_order_flavor);
        orderName = (TextView) findViewById(R.id.tv_admin_order_name);
        userIdentifierList= userClass.getListOfOrders();
        listOfOrders = userClass.getOrderClasses();
        if(userIdentifierList.size()>0){
            Log.d(TAG,userIdentifierList.toString());
//            position = listOfOrders.size()-1;
            position= userIdentifierList.size()-1;
            positionInOrder = userIdentifierList.get(position).getOrderClasses().size()-1;
//            orderFlavor.setText(listOfOrders.get(position).getFlavor());
//            orderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
//            orderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
//            orderName.setText(listOfOrders.get(position).getNameOfOrder());
            orderFlavor.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getFlavor());
            orderStatus.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getStatusOfOrderString());
            orderDate.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getDateOfOrder().getDate());
            orderName.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getNameOfOrder());
        }
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < userIdentifierList.size() - 1||positionInOrder < userIdentifierList.get(position).getOrderClasses().size()-1) {
                    if(positionInOrder < userIdentifierList.get(position).getOrderClasses().size()-1){
                        positionInOrder++;
                    }
                    else {
                        position++;
                        positionInOrder=0;
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "you are at the last order", Toast.LENGTH_SHORT).show();
                }
                if (listOfOrders.size() > 0) {
                    orderFlavor.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getFlavor());
                    orderStatus.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getStatusOfOrderString());
                    orderDate.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getDateOfOrder().getDate());
                    orderName.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getNameOfOrder());
                }
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position >= 1||positionInOrder>=1) {
//                    position--;
                    if(positionInOrder>=1){
                        positionInOrder--;
                    }
                    else {
                        position--;
                        positionInOrder = userIdentifierList.get(position).getOrderClasses().size()-1;
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "you are at the first order", Toast.LENGTH_SHORT).show();
                }
                if (listOfOrders.size() > 0) {
                    orderFlavor.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getFlavor());
                    orderStatus.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getStatusOfOrderString());
                    orderDate.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getDateOfOrder().getDate());
                    orderName.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getNameOfOrder());
                }
            }
        });
        btNextStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass tempOrder = userIdentifierList.get(position).getOrderClasses().get(positionInOrder);
                if (tempOrder.getStatusOfOrder() < 3) {
                    Map<String, Object> map = new HashMap<>();
                    tempOrder.setStatusOfOrder(tempOrder.getStatusOfOrder() + 1);
                    userIdentifierList.get(position).getOrderClasses().set(positionInOrder, tempOrder);
                    for(int i=0;i<userIdentifierList.get(position).getOrderClasses().size();i++) {
                        map.put("order" +( i + 1),userIdentifierList.get(position).getOrderClasses().get(i) );
                    }
                    FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference documentReference = firestore.collection("orders").document(userIdentifierList.get(position).getUserId());
                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                          orderStatus.setText(userIdentifierList.get(position).getOrderClasses().get(positionInOrder).getStatusOfOrderString());
                        Toast.makeText(AdminActivity.this, "order updated successfully ", Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(AdminActivity.this, "failed 2 upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }
                else {
                    Toast.makeText(AdminActivity.this, "the order already arrived", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public int getMaxPosition(List<UserIdentifier>orders){
        int counter=0;
        for (int i =0; i<orders.size();i++){
            counter += orders.get(i).getOrderClasses().size();
        }
        return counter;
    }
}
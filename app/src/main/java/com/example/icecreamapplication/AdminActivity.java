package com.example.icecreamapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    User userClass;
    Button btNext,btPrevious, btNextStatus;
    TextView  orderFlavor,orderStatus,orderDate;
    int position=0;
    List<OrderClass> listOfOrders;
    String TAG= "AdminActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userClass = (User) getIntent().getSerializableExtra("USER_CLASS");
        btNextStatus = (Button) findViewById(R.id.admin_next_status_bt);
        btPrevious = (Button) findViewById(R.id.admin_previous_order_bt);
        btNext = (Button) findViewById(R.id.admin_next_order_bt);
        orderDate = (TextView) findViewById(R.id.tv_admin_order_date);
        orderStatus = (TextView) findViewById(R.id.tv_admin_order_status);
        orderFlavor = (TextView) findViewById(R.id.tv_admin_order_flavor);
        listOfOrders = userClass.getOrderClasses();
//        listOfOrders = userClass.getListOfOrders();
        if(listOfOrders.size()>0){
            position = listOfOrders.size()-1;
            orderFlavor.setText(listOfOrders.get(position).getFlavor());
            orderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
            orderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
        }
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, listOfOrders.size() + "/" + position);
                if (position < listOfOrders.size() - 1) {
                    position++;
                } else {
                    Toast.makeText(AdminActivity.this, "you are at the last order", Toast.LENGTH_SHORT).show();
                }
                if (listOfOrders.size() > 0) {
                    orderFlavor.setText(listOfOrders.get(position).getFlavor());
                    orderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
                    orderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
//                Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
//                intent.putExtra("USER_CLASS", userClass);
//                startActivity(intent);
                }
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position >= 1) {
                    position--;
                } else {
                    Toast.makeText(AdminActivity.this, "you are at the first order", Toast.LENGTH_SHORT).show();
                }
                if (listOfOrders.size() > 0) {
                    orderFlavor.setText(listOfOrders.get(position).getFlavor());
                    orderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
                    orderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
//                Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
//                intent.putExtra("USER_CLASS", userClass);
//                startActivity(intent);
                }
            }
        });
        btNextStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClass temp = listOfOrders.get(position);
                if (temp.getStatusOfOrder() < 3) {
                    temp.setStatusOfOrder(temp.getStatusOfOrder() + 1);
                    listOfOrders.set(position, temp);
                    orderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
                }
                else {
                    Toast.makeText(AdminActivity.this,"the order already arrived",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
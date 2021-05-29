package com.example.icecreamapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    String TAG= "Main Activity";
    Button btNext,btPrevious,btBuy,btAbout;
    TextView lastOrderFlavor,lastOrderStatus,lastOrderDate;
    User userClass;
    int position=0;
    List<OrderClass> listOfOrders;
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
        listOfOrders = userClass.a();
        position = listOfOrders.size()-1;
        if(userClass.getLastOrderClass()!=null&&userClass.getLastOrderClass().getDateOfOrder()!=null){
            lastOrderFlavor.setText(listOfOrders.get(position).getFlavor());
            lastOrderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
            lastOrderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
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
                if(position<listOfOrders.size()-1){
                    position++;
                }
                else {
                    Toast.makeText(MainActivity.this,"you are at the last order",Toast.LENGTH_SHORT).show();
                }
                lastOrderFlavor.setText(listOfOrders.get(position).getFlavor());
                lastOrderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
                lastOrderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
//                Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
//                intent.putExtra("USER_CLASS", userClass);
//                startActivity(intent);
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position>1){
                    position--;
                }
                else {
                    Toast.makeText(MainActivity.this,"you are at the first order",Toast.LENGTH_SHORT).show();
                }
                lastOrderFlavor.setText(listOfOrders.get(position).getFlavor());
                lastOrderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
                lastOrderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
//                Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
//                intent.putExtra("USER_CLASS", userClass);
//                startActivity(intent);
            }
        });
    }
}
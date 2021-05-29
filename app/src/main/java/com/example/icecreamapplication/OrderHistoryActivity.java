package com.example.icecreamapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    Button nextBt;
    TextView historyOrderFlavor,historyOrderStatus,historyOrderDate;
    int position=0;
    User userClass;
    List<OrderClass> listOfOrders;
    String TAG = "OrderHistory";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        userClass = (User) getIntent().getSerializableExtra("USER_CLASS");
        nextBt = (Button) findViewById(R.id.next_bt);
        historyOrderDate = (TextView) findViewById(R.id.tv_history_order_date);
        historyOrderFlavor = (TextView) findViewById(R.id.tv_history_order_flavor);
        historyOrderStatus = (TextView) findViewById(R.id.tv_history_order_status);
        listOfOrders = userClass.a();
        if(userClass.getLastOrderClass()!=null){
            historyOrderFlavor.setText(listOfOrders.get(position).getFlavor());
            historyOrderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
            historyOrderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
        }
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                Log.d(TAG,"next position: "+position+", value: "+listOfOrders.get(position).getFlavor());
                if(userClass.getLastOrderClass()!=null){
                    historyOrderFlavor.setText(listOfOrders.get(position).getFlavor());
                    historyOrderStatus.setText(listOfOrders.get(position).getStatusOfOrderString());
                    historyOrderDate.setText(listOfOrders.get(position).getDateOfOrder().getDate());
                }
            }
        });
    }
}
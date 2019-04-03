package com.example.randomrestaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends Activity {
    private TextView RandomRestaurantID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();
        String result = intent.getStringExtra("RandomRestaurant");
        RandomRestaurantID = (TextView) findViewById(R.id.RandomRestaurantID);
        RandomRestaurantID.setText(result);
    }
}

package com.example.randomrestaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TryNewRestaurantActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_new_restaurant);
    }

    public void onClickTry(View view){
        startActivity(new Intent(this, ResultsActivity.class));
    }
}

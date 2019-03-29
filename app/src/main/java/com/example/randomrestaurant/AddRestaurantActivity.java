package com.example.randomrestaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddRestaurantActivity extends Activity {

//    FavoritesListActivity my_faves_list = new FavoritesListActivity();

    private EditText editRestaurant;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
//        editRestaurant = (EditText) findViewById(R.id.editRestaurant);
//        String toAdd = editRestaurant.getText().toString().trim();
//        my_faves_list.addRestaurant(toAdd );
    }

    public void gotoAdd(View view){
        // Second view when click the button of the main view
        startActivity(new Intent(this, FavoritesListActivity.class));
    }
}

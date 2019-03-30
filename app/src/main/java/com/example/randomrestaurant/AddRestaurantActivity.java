package com.example.randomrestaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddRestaurantActivity extends AppCompatActivity {
    //view objects
    EditText editTextName;
    Spinner spinnerRestaurant;
    Button buttonAddRestaurant;
    ListView listViewRestaurants;

    //a list to store all the restaurant from firebase database
    List<String> restaurants;

    //our database reference object
    DatabaseReference databaseRestaurants;
//    Spinner spinnerRestaurant;
//    EditText editRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_restaurant);

        //getting the reference of artists node
        databaseRestaurants = FirebaseDatabase.getInstance().getReference("restaurants");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerRestaurant = (Spinner) findViewById(R.id.spinnerRestaurant);
        listViewRestaurants = (ListView) findViewById(R.id.listViewRestaurants);
        buttonAddRestaurant = (Button) findViewById(R.id.buttonAddRestaurant);

        //list to store artists
        restaurants = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addRestaurant();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseRestaurants.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                restaurants.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    String restaurant = postSnapshot.getValue().toString();
                    //adding artist to the list
                    restaurants.add(restaurant);
                }

                //creating adapter
                RestaurantList restaurantAdapter = new RestaurantList(AddRestaurantActivity.this, restaurants);
                //attaching adapter to the listview
                listViewRestaurants.setAdapter(restaurantAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
     * This method is saving a new artist to the
     * Firebase Realtime Database
     * */
    private void addRestaurant() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseRestaurants.push().getKey();


            //Saving the Artist
            databaseRestaurants.child(id).setValue(name);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Restaurant added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}





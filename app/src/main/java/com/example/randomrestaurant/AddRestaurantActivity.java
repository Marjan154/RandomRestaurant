package com.example.randomrestaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    ArrayList<String> data;



    //our database reference object
    DatabaseReference databaseRestaurants;
    DatabaseReference databaseUsers;
//    Spinner spinnerRestaurant;
//    EditText editRestaurant;
private static final String TAG = "ADD RESTAURANT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_restaurant);

        //getting the reference of restaurants node
        databaseRestaurants = FirebaseDatabase.getInstance().getReference("restaurants");
        databaseUsers = FirebaseDatabase.getInstance().getReference("/");
        data=new ArrayList<>();


        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerRestaurant = (Spinner) findViewById(R.id.spinnerRestaurant);
        listViewRestaurants = (ListView) findViewById(R.id.listViewRestaurants);
        buttonAddRestaurant = (Button) findViewById(R.id.buttonAddRestaurant);

        //list to store restaurants
        restaurants = new ArrayList<>();




        //adding an onclicklistener to button
        buttonAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addrestaurant()
                //the method is defined below
                //this method is actually performing the write operation
                addRestaurant();
            }
        });

        listViewRestaurants.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String restaurant = restaurants.get(i);
                showDeleteDialog(restaurant);
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        Log.d(TAG , "we in on start");
        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("/").child("/"+uid).child("/favoritesList");
//        final String uemail = user.getEmail();
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous restaurant list
                restaurants.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    getting restaurant
                    //if (postSnapshot.getKey().equals(uid)) {
                        String r = postSnapshot.getValue().toString();
                        restaurants.add(r);
                        Log.d(TAG , r);
                        RestaurantList restaurantAdapter = new RestaurantList(AddRestaurantActivity.this, restaurants);
                        listViewRestaurants.setAdapter(restaurantAdapter);
                    //}

//                    String restaurant = postSnapshot.getValue().toString();
                    //
//                    restaurants.add(restaurant);
                }

                //creating adapter

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
     * This method is saving a new restaurant to the
     * Firebase Realtime Database
     * */
    private void addRestaurant() {
        //getting the values to save
        final String name = editTextName.getText().toString().trim();
        data.clear();
        data.add(name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final String uemail = user.getEmail();
//        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("/");
        final DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("/").child("/"+uid).child("/favoritesList");

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our restaurant
            //String id = databaseRestaurants.push().getKey();

           
            databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ArrayList<String> currData=new ArrayList<>();
                            currData.add(postSnapshot.getValue().toString());
                            //String id  = databaseUsers.push().getKey();
                            databaseUser.removeValue();
                            data.addAll(currData);
                            databaseUser.setValue(data);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            editTextName.setText("");

            Toast.makeText(this, "Restaurant added", Toast.LENGTH_LONG).show();
        }else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
//-------------------------------------------------------------------
    private void showDeleteDialog(final String restaurantName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerRestaurant= (Spinner) dialogView.findViewById(R.id.spinnerRestaurant);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteRestaurant);

        dialogBuilder.setTitle(restaurantName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteRestaurant(restaurantName);
                b.dismiss();

            }
        });
    }

    private boolean deleteRestaurant(final String name) {
//        //getting the specified restaurant reference
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("restaurants").child(name);
//        //removing restaurant
//        dR.removeValue();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("/").child("/"+uid).child("/favoritesList");
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.getValue().toString().equals(name)){
                        String toDelete = postSnapshot.getKey();
                        DatabaseReference dR = databaseUser.child(toDelete);
                        dR.removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return true;
    }
}





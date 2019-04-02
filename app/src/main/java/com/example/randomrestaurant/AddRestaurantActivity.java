package com.example.randomrestaurant;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class AddRestaurantActivity extends AppCompatActivity {
    //view objects
    EditText editTextName;
    Spinner spinnerRestaurant;
    Button buttonAddRestaurant;
    Button buttonFindRandomList;
    ListView listViewRestaurants;


    List<String> restaurants;
    ArrayList<String> data;


    DatabaseReference databaseRestaurants;
    DatabaseReference databaseUsers;

    private static final String TAG = "ADD RESTAURANT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_restaurant);

        databaseRestaurants = FirebaseDatabase.getInstance().getReference("restaurants");
        databaseUsers = FirebaseDatabase.getInstance().getReference("/");
        data=new ArrayList<>();


        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerRestaurant = (Spinner) findViewById(R.id.spinnerRestaurant);
        listViewRestaurants = (ListView) findViewById(R.id.listViewRestaurants);
        buttonAddRestaurant = (Button) findViewById(R.id.buttonAddRestaurant);
        buttonFindRandomList = (Button) findViewById(R.id.buttonFindRandomList);
        restaurants = new ArrayList<>();

        buttonAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        buttonFindRandomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                findRandom();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("/").child("/"+uid).child("/favoritesList");
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                restaurants.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String r = postSnapshot.getValue().toString();
                        restaurants.add(r);
                        RestaurantList restaurantAdapter = new RestaurantList(AddRestaurantActivity.this, restaurants);
                        listViewRestaurants.setAdapter(restaurantAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void addRestaurant() {
        final String name = editTextName.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        if (!TextUtils.isEmpty(name)) {

            FirebaseDatabase.getInstance().getReference("/").child("/"+uid).child("/favoritesList").child("/"+name).setValue(name);
            editTextName.setText("");
            Toast.makeText(this, "Restaurant added", Toast.LENGTH_LONG).show();
        }
        else {
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("/").child("/"+uid).child("/favoritesList").child(name);
        dR.removeValue();
        return true;
    }

    private void findRandom() {
        if(!restaurants.isEmpty()){
            int range = restaurants.size();
            int rand = (int)(Math.random() * range);
            String randomRestaurant = restaurants.get(rand);
            Intent randomIntent = new Intent(AddRestaurantActivity.this, ResultsActivity.class);
            randomIntent.putExtra("RandomRestaurant", randomRestaurant);
            startActivity(randomIntent);
        }
    }


}





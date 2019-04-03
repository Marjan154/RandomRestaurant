package com.example.randomrestaurant;

import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesListActivity extends AppCompatActivity {
    private ListView listView;

    private List<String> restaurantList;
    private ArrayAdapter<String> adapter;
    ListView listViewRestaurants;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        listViewRestaurants = (ListView) findViewById(R.id.listViewRestaurants);

        restaurantList = new ArrayList<>();
        restaurantList.add("Dominos");
        restaurantList.add("Bareburger");
        restaurantList.add("Red Lobster");
        restaurantList.add("McDonalds");

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, restaurantList);

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                restaurantList.remove(position);
                adapter.notifyDataSetChanged();

                return false;
            }
        });
    }

    public void addRestaurant(String restaurant){
        restaurantList.add(restaurant);
    }


}

package com.example.randomrestaurant;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RestaurantList extends ArrayAdapter<String>  {

        private Activity context;
        List<String> restaurants;

        public RestaurantList(Activity context, List<String> restaurants) {
            super(context, R.layout.list_layout, restaurants);
            this.context = context;
            this.restaurants = restaurants;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
            String name = restaurants.get(position);
            textViewName.setText(name);

            return listViewItem;
        }
    }

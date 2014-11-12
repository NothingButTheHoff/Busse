package com.pefi.Busse;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import org.json.JSONArray;

/**
 * Created by pererikfinstad on 11/11/14.
 */
public class SearchResultActivity extends Activity {
    public static final String TAG = "SearchResultActivity";

    APIInterface api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        api = new APIInterface();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            search(query);

        }

        api.setMyTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setMyTaskComplete(JSONArray json) {
                //Log.d(TAG, json.);


            }
        });

    }



    public void search(String query){

        api.execute("Place/GetPlaces/" + query);
    }



}
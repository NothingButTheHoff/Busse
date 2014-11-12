package com.pefi.Busse;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pererikfinstad on 11/11/14.
 */
public class SearchResultActivity extends Activity {
    public static final String TAG = "SearchResultActivity";

    APIInterface api;
    TextView places;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);



        places = (TextView) findViewById(R.id.places);
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
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < json.length(); i++){
                    try {
                        JSONObject jo = json.getJSONObject(i);
                        sb.append(jo.getString("Name") +  "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                places.setText(sb);
                System.out.println(sb.toString());


            }
        });

    }



    public void search(String query){

        api.execute("Place/GetPlaces/" + query);
    }



}
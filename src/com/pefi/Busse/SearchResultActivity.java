package com.pefi.Busse;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;

/**
 * Created by pererikfinstad on 11/11/14.
 */
public class SearchResultActivity extends Activity implements OnItemClickListener {
    public static final String TAG = "SearchResultActivity";

    APIInterface api;
    TextView places;
    ListView list;
    List<Stop> rowItem;
    OnItemClickListener listener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        api = new APIInterface();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            query = query.replaceAll("\\s+","");
            search(query);

        }

        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {

                if (json != null && json.length() > 0) {

                    rowItem = new ArrayList<Stop>();

                    for (int i = 0; i < json.length(); i++)
                        try {
                            JSONObject jo = json.getJSONObject(i);


                            Stop item = new Stop(jo.getString("Name"), jo.getString("District"), jo.getInt("ID"), jo.getInt("Zone"));
                            rowItem.add(item);

                            list = (ListView) findViewById(R.id.stopList);
                            CustomBaseAdapter adapter = new CustomBaseAdapter(getBaseContext(), rowItem);
                            list.setAdapter(adapter);
                            //list.setOnItemClickListener();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                } else {
                    places.setText("No results");
                }

            }
        });

    }


    /**
     * Method for performing the search to the API
     *
     * @param query String containing the word to be searched for
     */
    public void search(String query){

        api.execute("Place/GetPlaces/" + query);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Stop stop = rowItem.get(i);
        Toast.makeText(this, stop.getId(), Toast.LENGTH_SHORT).show();
    }
}
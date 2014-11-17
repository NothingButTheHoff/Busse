package com.pefi.Busse;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.OnItemClickListener;

/**
 * Created by pererikfinstad on 11/11/14.
 */
public class SearchResultActivity extends Activity implements OnItemClickListener {
    public static final String TAG = "SearchResultActivity";

    APIInterface api;
    ListView list;
    List<Stop> rowItem;



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
            Log.d(TAG, "Stop to be searched for: " + query);
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

                            if (jo.getString("PlaceType").equals("Stop") ){

                            Stop item = new Stop(jo.getString("Name"), jo.getString("District"), jo.getInt("ID"));
                                rowItem.add(item);

                                list = (ListView) findViewById(R.id.stopList);
                                StopsBaseAdapter adapter = new StopsBaseAdapter(getBaseContext(), rowItem);
                                list.setAdapter(adapter);
                                list.setOnItemClickListener(new OnItemClickListener(){
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Stop stop = rowItem.get(i);
                                        int id = stop.getId();

                                        Intent intent = new Intent(getBaseContext(), LinesActivity.class);
                                        intent.putExtra("stopId", id);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(getBaseContext(), Integer.toString(id) , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                } else {
                    Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}


}// end SearchResultActivity
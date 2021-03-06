package com.pefi.Busse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
    TextView searchPhrase;

    ProgressDialog progress;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        progress = ProgressDialog.show(this, null, getString(R.string.searches), true);

        progress.setCancelable(true);

        api = new APIInterface();

        searchPhrase = (TextView) findViewById(R.id.searchPhrase);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            searchPhrase.setText(getString(R.string.you_searched_for) + " " + query);

            //makes sure the url accepts whitespaces and removed whitespace at end
            query = query.replaceAll("\\s+$", "").replaceAll("\\s+", "%20");
            Log.d(TAG, "Stop to be searched for: " + query);
            search(query);


        }

        setListener();

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



    private void setListener(){
        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {

                progress.dismiss();

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
                                        String stopName = stop.getName();

                                        Intent intent = new Intent(getBaseContext(), LinesActivity.class);
                                        intent.putExtra("stopId", id);
                                        intent.putExtra("stopName", stopName);
                                        startActivity(intent);
                                    }
                                });
                            }
                            else if(i == json.length()-1 && rowItem.size() == 0){
                                Log.d(TAG, Integer.toString(rowItem.size()));
                                list = (ListView) findViewById(R.id.stopList);
                                list.setVisibility(View.GONE);
                                TextView empty = (TextView) findViewById(R.id.stopsEmpty);
                                empty.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                } else {
                    //If the api returns 0 hits
                    list = (ListView) findViewById(R.id.stopList);
                    list.setVisibility(View.GONE);
                    TextView empty = (TextView) findViewById(R.id.stopsEmpty);
                    empty.setVisibility(View.VISIBLE);

                }

            }
        });

    } // end setListener

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}// end SearchResultActivity
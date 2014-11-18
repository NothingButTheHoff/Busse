package com.pefi.Busse;

import android.app.Activity;
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

/**
 * Created by pererikfinstad on 18/11/14.
 */
public class StopsByLineActivity extends Activity implements AdapterView.OnItemClickListener{
    public static final String TAG = "StopsByLineActivity";

    APIInterface api;
    int id;
    ListView list;
    List<Stop> rowItem;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Intent intent = getIntent();
        id = intent.getIntExtra("lineID", -1);
        Toast.makeText(getBaseContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();

        api = new APIInterface();

        api.execute("Line/GetStopsByLineID/" + id);
        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {

                if (json != null && json.length() > 0) {

                    rowItem = new ArrayList<Stop>();

                    for (int i = 0; i < json.length(); i++)
                        try {

                            JSONObject jo = json.getJSONObject(i);

                            //if (jo.getString("PlaceType").equals("Stop") ){

                                Stop item = new Stop(jo.getString("Name"), jo.getString("District"), jo.getInt("ID"));
                                rowItem.add(item);

                                list = (ListView) findViewById(R.id.stopList);
                                StopsBaseAdapter adapter = new StopsBaseAdapter(getBaseContext(), rowItem);
                                list.setAdapter(adapter);
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        //save to shared preferenses
                                        Toast.makeText(getBaseContext(), Integer.toString(id) , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                } else {
                    //If the api returns 0 hits
                    rowItem = new ArrayList<Stop>();
                    Stop item = new Stop(getString(R.string.no_hits));
                    Log.d(TAG, item.getName());
                    rowItem.add(item);

                    list = (ListView) findViewById(R.id.stopList);
                    StopsBaseAdapter adapter = new StopsBaseAdapter(getBaseContext(), rowItem);
                    list.setAdapter(adapter);
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}


}//end StopsByLineActivity
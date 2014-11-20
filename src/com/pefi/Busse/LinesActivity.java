package com.pefi.Busse;

import android.app.ActionBar;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.widget.AdapterView.OnItemClickListener;


/**
 * Created by pererikfinstad on 14/11/14.
 */
public class LinesActivity extends Activity implements OnItemClickListener{
    public static final String TAG = "LinesActivity";

    APIInterface api;
    int id;

    ListView list;
    List<Line> rowItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lines);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(null);

        api = new APIInterface();

        Intent intent = getIntent();
        id = intent.getIntExtra("stopId", -1);
        Toast.makeText(getBaseContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();

        api.execute("Stopvisit/GetDepartures/" + id +"?json=true");


        setListener();
    }





    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}



    private void setListener(){
        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {

                if (json != null && json.length() > 0) {

                    rowItem = new ArrayList<Line>();

                    for (int i = 0; i < json.length(); i++) {
                        System.out.println("Antall kall: " + Integer.toString(i));
                        try {
                            JSONObject jo = json.getJSONObject(i);

                            Log.d(TAG, "JSONOBJECT to assign: " + jo.toString());

                            JSONObject j = jo.getJSONObject("MonitoredVehicleJourney");
                            JSONObject j2 = jo.getJSONObject("Extensions");
                            System.out.println(jo.names());
                            Line item = new Line(j.getString("LineRef"), j.getString("DestinationName"), j2.getString("LineColour"));

                            if (! rowItem.contains(item)){
                                rowItem.add(item);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                    Collections.sort(rowItem, new Comparator<Line>() {
                        @Override public int compare(Line l1, Line l2) {
                            return Integer.parseInt(l1.getName()) - Integer.parseInt(l2.getName()); // Ascending
                        }

                    });
                    //set the adapter
                    list = (ListView) findViewById(R.id.linesList);
                    LinesBaseAdapter adapter = new LinesBaseAdapter(getBaseContext(), rowItem);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //do something
                            Toast.makeText(getBaseContext(), "Yeah!", Toast.LENGTH_LONG).show();
                            Intent intent1 = new Intent(getBaseContext(), StopsByLineActivity.class);
                            intent1.putExtra("lineID", rowItem.get(i).getId() );
                            startActivity(intent1);
                        }
                    });
                }
                else {
                    System.out.println("No data from the API");
                }
            }
        });
    }//end setListener

} // end LinesActivity
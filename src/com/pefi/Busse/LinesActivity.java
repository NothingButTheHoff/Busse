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
    List<Line> rows;


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

                            Line item = new Line(j.getString("LineRef"), j.getString("DestinationName"));

                            if (! rowItem.contains(item)){
                                rowItem.add(item);
                            }
                           

                            list = (ListView) findViewById(R.id.linesList);
                            LinesBaseAdapter adapter = new LinesBaseAdapter(getBaseContext(), rowItem);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //do something
                                    Toast.makeText(getBaseContext(), "YEah", Toast.LENGTH_LONG).show();
                                    Intent intent1 = new Intent(getBaseContext(), StopsByLineActivity.class);
                                    intent1.putExtra("lineID", rowItem.get(i).getId() );
                                    startActivity(intent1);
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    System.out.println("No data from the API");
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}



    public static <Line> boolean containsInstance(List<Line> list, Class<? extends Line> clazz) {
        for (Line line : list) {
            if (clazz.isInstance(line)) {
                return true;
            }
        }
        return false;
    }


    public List<Line> sortItems(List<Line> rows){
        for (int i = 0; i < rows.size(); i ++){
            if (rowItem.size() < 1){
                rowItem.add(rows.get(i));
            }
            else{
                for (int j = 0; j < rowItem.size(); j ++){
                    if (rowItem.get(j).getName().equals(rows.get(i).getName())){
                        break;
                    }
                    else rowItem.add(rows.get(i));
                }

            }
        }

        return rowItem;
    }


} // end LinesActivity
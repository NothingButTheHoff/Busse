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


        api = new APIInterface();

        Intent intent = getIntent();
        id = intent.getIntExtra("stopId", -1);
        Toast.makeText(getBaseContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();

        api.execute("Line/GetLinesByStopID/" + id +"?json=true");


        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {

                if (json != null && json.length() > 0) {

                    rowItem = new ArrayList<Line>();

                    for (int i = 0; i < json.length(); i++)
                        try {
                            JSONObject jo = json.getJSONObject(i);
                            Log.d(TAG, "JSONOBJECT to assign: " + jo.toString());

                            Line item = new Line(jo.getString("Name"), jo.getInt("ID"),jo.getString("Transportation"));
                            rowItem.add(item);

                            list = (ListView) findViewById(R.id.linesList);
                            LinesBaseAdapter adapter = new LinesBaseAdapter(getBaseContext(), rowItem);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //do something
                                    Toast.makeText(getBaseContext(), "YEah", Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                } else {
                    Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}



} // end LinesActivity
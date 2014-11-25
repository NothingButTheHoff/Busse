package com.pefi.Busse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

    ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lines);

        progress = ProgressDialog.show(this, null, getString(R.string.fetches_lines), true);


        ActionBar actionBar = getActionBar();
        actionBar.setIcon(null);

        api = new APIInterface();

        Intent intent = getIntent();
        id = intent.getIntExtra("stopId", -1);

        api.execute("Stopvisit/GetDepartures/" + id +"?json=true");


        setListener();
    }





    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}


    private void setListener(){
        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {
                progress.dismiss();

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
                            showDialog(rowItem.get(i));
                        }
                    });
                }
                else {
                    System.out.println("No data from the API");
                }
            }
        });

    }//end setListener



    public void showDialog(Line l){

        new AlertDialog.Builder(this)
                .setTitle(l.getName() + " " + l.getDestination())
                .setMessage(getString(R.string.add_to_favs))
                .setPositiveButton( getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBHandler db = new DBHandler(getBaseContext());
                        db.insertFavourite(new Favourite(Integer.toString(id), l.getName(), l.getDestination(), 2));

                        Toast.makeText(getBaseContext(), l.getName() + " " + l.getDestination() + " " + getString(R.string.was_added_to_fav), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }



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


} // end LinesActivity
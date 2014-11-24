package com.pefi.Busse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity implements OnItemClickListener{
    private final static String TAG = "MainActivity";

    APIInterface api;

    int stopId, lineNo;
    String destination;
    private List<Favourite> rowItem;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        checkInternetConnection();


        //assign values for the query
        stopId = 3012135;
        lineNo = 30;
        destination = "Bygdøy";

        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(stopId));
        sb.append("-" + Integer.toString(lineNo));
        sb.append("-" + destination);
        Log.d(TAG, String.valueOf(sb));

        api = new APIInterface();
        api.execute("Favourites/GetFavourites?favouritesRequest=" + sb + ",3012135-51-Maridalen");

        long time = System.currentTimeMillis();

        TextView name = (TextView) findViewById(R.id.updated);
        name.setText(getString(R.string.last_updated) + " " + convertTime(time));


        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {
                if (json != null && json.length() > 0) {
                    rowItem = new ArrayList<Favourite>();
                    for (int i = 0; i < json.length(); i++)
                        try {
                            JSONObject jo = json.getJSONObject(i);

                            String lineName = jo.getJSONArray("MonitoredStopVisits").getJSONObject(0).getJSONObject("MonitoredVehicleJourney").getString("PublishedLineName") + " " + jo.getString("Destination");
                            System.out.println(jo.getJSONArray("MonitoredStopVisits").getJSONObject(0).getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedArrivalTime"));
                            String firstArrivaltime  = jo.getJSONArray("MonitoredStopVisits").getJSONObject(0).getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedArrivalTime");
                            String secondArrivaltime = jo.getJSONArray("MonitoredStopVisits").getJSONObject(1).getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedArrivalTime");
                            String thirdArrivaltime  = jo.getJSONArray("MonitoredStopVisits").getJSONObject(2).getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedArrivalTime");

                            Favourite fav = new Favourite(lineName, formatDate(firstArrivaltime), formatDate(secondArrivaltime),formatDate(thirdArrivaltime));


                            rowItem.add(fav);

                            list = (ListView) findViewById(R.id.favouriteList);
                            FavouritesBaseAdapter adapter = new FavouritesBaseAdapter(getBaseContext(), rowItem);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //do something on click
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            }
        });


        //api.setParseJSONObjectCompleteListener(new APIInterface.OnParseJSONObjectComplete() {
        //    @Override
        //    public void setParseJSONObjectComplete(JSONObject jsonObject) {
        //        //do something with the json Object
        //        try {
        //            String n = jsonObject.getString("Destination");
        //            //name.setText(n);
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //});

    }

    /**
     * Method for creating the Action Bar menu and associates the
     * searchable configuration with the SearchView
     *
     * @param menu The Options menu with your menu items
     * @return Return true if you want the menu to be display. If not, return false
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);


        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnection();

    }

    /**
     * Method for checking the internet connection. For debugging only.
     * The method doesn't have any return values, but instead it writes
     * the result to the Log.
     *
     */
    public void checkInternetConnection(){

        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(TAG, "Connected to the internet");
        } else {
            showDialog();
            Log.i(TAG, "Internet is not available");
        }

    }



    public void showDialog(){

        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.internet_required))
        .setMessage(getString(R.string.turn_on_internet))
        .setPositiveButton( getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        })
        .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        })
        .show();

    }

    public static String formatDate(String dateString) {
        Date date;
        String formattedDate = "";
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateString);
            formattedDate = new SimpleDateFormat("HH:mm",Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm:ss");

        return format.format(date);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}


}//end MainActivity



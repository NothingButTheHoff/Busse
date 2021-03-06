package com.pefi.Busse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.widget.AdapterView.OnItemLongClickListener;


public class MainActivity extends Activity implements OnItemLongClickListener{
    private final static String TAG = "MainActivity";

    APIInterface api;

    String firstArrivaltime;
    String secondArrivaltime;
    String thirdArrivaltime;

    ProgressDialog progress;


    private List<Favourite> rowItem;
    private List<Favourite>favourites;
    private ListView list;
    TextView name;
    AlertDialog internetDialog;

    DBHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        checkInternetConnection();

        db = new DBHandler(this);

        favourites = db.getAllFavourites();

        name = (TextView) findViewById(R.id.updated);

        if (favourites.size() > 0) {
            progress = ProgressDialog.show(this, null, getString(R.string.fetches_departures), true);
            getAllFavourites();
        }
        else{
            name.setText(getString(R.string.you_have_no_favs));
        }

    }



    public void getAllFavourites(){

            String favoriteStops = buildString(favourites);

            api = new APIInterface();
            api.execute("Favourites/GetFavourites?favouritesRequest=" + favoriteStops);

            long time = System.currentTimeMillis();

            name.setText(getString(R.string.last_updated) + " " + convertTime(time));

            setListener();

    }

    public void setListener(){

        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {
                progress.dismiss();

                if (json != null && json.length() > 0) {

                    rowItem = new ArrayList<Favourite>();

                    for (int i = 0; i < json.length(); i++)
                        try {
                            JSONObject jo = json.getJSONObject(i);

                            String lineName;

                            try {
                                lineName = jo.getJSONArray("MonitoredStopVisits").getJSONObject(0).getJSONObject("MonitoredVehicleJourney").getString("PublishedLineName") + " " + jo.getString("Destination");
                            } catch (JSONException e) {
                                e.getMessage();
                                lineName = jo.getString("Destination");
                            }

                            try {
                                firstArrivaltime = jo.getJSONArray("MonitoredStopVisits").getJSONObject(0).getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedDepartureTime");
                                firstArrivaltime = formatDate(firstArrivaltime);
                            } catch (JSONException e) {
                                firstArrivaltime = "n/a";
                                e.getMessage();
                            }
                            try {
                                secondArrivaltime = jo.getJSONArray("MonitoredStopVisits").getJSONObject(1).getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedDepartureTime");
                                secondArrivaltime = formatDate(secondArrivaltime);
                            } catch (JSONException e) {
                                secondArrivaltime = "n/a";
                                e.getMessage();
                            }
                            try {
                                thirdArrivaltime = jo.getJSONArray("MonitoredStopVisits").getJSONObject(2).getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedDepartureTime");
                                thirdArrivaltime = formatDate(thirdArrivaltime);
                            } catch (JSONException e) {
                                thirdArrivaltime = "n/a";
                                e.getMessage();
                            }

                            String color;

                            try {
                                color = jo.getJSONArray("MonitoredStopVisits").getJSONObject(0).getJSONObject("Extensions").getString("LineColour");
                            } catch (JSONException e) {
                                e.getMessage();
                                color = "B4B4B4";
                            }


                            Favourite fav = new Favourite(favourites.get(i).getId(), lineName, firstArrivaltime, secondArrivaltime, thirdArrivaltime, color, favourites.get(i).fromStopName);


                            rowItem.add(fav);

                            list = (ListView) findViewById(R.id.favouriteList);
                            FavouritesBaseAdapter adapter = new FavouritesBaseAdapter(getBaseContext(), rowItem);
                            list.setAdapter(adapter);
                            list.setOnItemLongClickListener(new OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    showDeleteFavouriteDialog(rowItem.get(i));
                                    return false;
                                }

                            });
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
                else{
                    list = (ListView) findViewById(R.id.favouriteList);
                    list.setVisibility(View.GONE);
                    TextView empty = (TextView) findViewById(R.id.no_data);
                    empty.setVisibility(View.VISIBLE);
                }
            }
        });

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
    }

    @Override
    protected void onPause(){
        super.onPause();
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
            showInternetDialog();
            Log.i(TAG, "Internet is not available");
        }

    }



    public void showInternetDialog(){

         internetDialog = new AlertDialog.Builder(this)
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

    public void showDeleteFavouriteDialog(Favourite f){

        new AlertDialog.Builder(this)
                .setTitle(f.getLineName())
                .setMessage(getString(R.string.delete_fav))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (deleteFavourite(f.getId())) {
                            Toast.makeText(getBaseContext(), f.getLineName() + " " + getString(R.string.was_deleted), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else {
                            Toast.makeText(getBaseContext(), getString(R.string.could_not_delete) + " " + f.getLineName(), Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //Closes the dialog without any actions
                    }
                })
                .show();

    }


    public void showDeleteAllDialog(){

        if (favourites.size() > 0){

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_all_favs))
                    .setMessage(getString(R.string.cannot_be_undone))
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (db.deleteAllFavourites()) {
                                Toast.makeText(getBaseContext(), getString(R.string.all_favs_deleted), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            } else {
                                Toast.makeText(getBaseContext(), getString(R.string.could_not_delete) + " " + getString(R.string.all_favs), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Closes the dialog without any actions
                        }
                    })
                    .show();
        }
        else{
            Toast.makeText(this, getString(R.string.you_have_no_favs), Toast.LENGTH_SHORT).show();
        }

    }

    public void showInfoDialog(){

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.info))
                .setMessage(getString(R.string.info_text))
                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

    }

    public String formatDate(String dateString) {
        Date date;
        String formattedDate = "";

        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateString);

            long interval = date.getTime()- new Date().getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(interval);

            //sets text based on number of minutes upon departure
            if (diffInMinutes < 1){
                formattedDate = getString(R.string.now);
            }
            else if (diffInMinutes < 8 ){
                formattedDate = diffInMinutes + " " + getString(R.string.minutes);
            }
            else{
                formattedDate = new SimpleDateFormat("HH:mm",Locale.getDefault()).format(date);
            }

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


    public String buildString(List<Favourite> list){
        StringBuilder sb = new StringBuilder();
        for (Favourite f : list){
            sb.append(f.getStopId() + "-");
            sb.append(f.getLineNo() + "-");
            sb.append(f.destination + ",");
        }

        String s = sb.toString();

        if (s.endsWith(",")){
            s = s.substring(0, s.length() -1);
        }
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.exit:
                finish();
                return true;
            case R.id.refresh:
                refresh();
                return true;
            case R.id.info:
                showInfoDialog();
                return true;
            case R.id.delete_all_favs:
                showDeleteAllDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean refresh(){
        if (favourites.size() > 0){
            progress = ProgressDialog.show(this, null, getString(R.string.updates), true);

            getAllFavourites();
            checkInternetConnection();
            return true;
        }
        return false;
    }

    public boolean deleteFavourite(int i){
        DBHandler db = new DBHandler(this);
        if (db.deleteFavourite(i)){
            return true;
        }
        return false;
    }


}//end MainActivity



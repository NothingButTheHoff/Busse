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
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private final static String TAG = "MainActivity";

    APIInterface api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        checkInternetConnection();

        api = new APIInterface();
        api.execute("Place/GetStop/45");

        TextView name = (TextView) findViewById(R.id.stopName);


        api.setTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setTaskComplete(JSONArray json) {
                //do something with JSON array
            }
        });


        api.setParseJSONObjectCompleteListener(new APIInterface.OnParseJSONObjectComplete() {
            @Override
            public void setParseJSONObjectComplete(JSONObject jsonObject) {
                //do something with the json Object
                try {
                    String n = jsonObject.getString("Name");
                    name.setText(n);
                } catch (JSONException e) {
                    e.printStackTrace();
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



}//end MainActivity



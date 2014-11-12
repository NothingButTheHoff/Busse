package com.pefi.Busse;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private final static String TAG = "MainActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        checkInternetConnection();

        APIInterface api = new APIInterface();
        api.execute("Place/GetStop/45?json=true");

        TextView name = (TextView) findViewById(R.id.stopName);

        api.setMyTaskCompleteListener(new APIInterface.OnTaskComplete() {
            @Override
            public void setMyTaskComplete(JSONObject json) {
                //do something with JSON object

                try {
                    name.setText(json.getString("Name"));
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
            Log.i(TAG, "Internet is not available");
        }

    }
}

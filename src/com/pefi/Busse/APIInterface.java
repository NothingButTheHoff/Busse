package com.pefi.Busse;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by pererikfinstad on 11/11/14.
 */

public class APIInterface extends AsyncTask<String, String, String> {

    public final static String TAG = "APIInterface";
    public final static String API_URL = "http://reisapi.ruter.no/";

    private OnTaskComplete onTaskComplete;
    private OnParseJSONObjectComplete onParseJSONObjectComplete;

    JSONObject jsonObject;
    JSONArray jsonArray;
    InputStream is = null;
    String jsonString;



    @Override
    protected String doInBackground(String... strings) {

        String urlString = strings[0]; // URL to call

        String result = "Success!";
        int response = -1;

        // GET
        try {
            Log.i(TAG, "URL to be called: " + API_URL + urlString );

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet get = new HttpGet(API_URL + urlString);

            try {
                HttpResponse httpresponse = httpclient.execute(get);
                response = httpresponse.getStatusLine().getStatusCode();
                HttpEntity httpentity = httpresponse.getEntity();
                is = httpentity.getContent();


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e ) {
            return e.getMessage();
        }

        jsonString = buildString(is);

        //Parse to JSON
        jsonArray = parseJSONArray(jsonString);

        if (jsonArray == null){
            jsonObject = parseJSONObject(jsonString);
        }

        Log.i(TAG, "HTTP Response Code: " + response);

        return result;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        if (jsonArray != null){
            onTaskComplete.setTaskComplete(jsonArray);
        }

        if (jsonObject != null){
            onParseJSONObjectComplete.setParseJSONObjectComplete(jsonObject);
        }
        else{

        }

        Log.i(TAG, "Result: " + result);
    }


    private String buildString(InputStream is){
        String jsonString;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                jsonString = sb.toString();

                System.out.println("JSON-string from API: " + jsonString);
                return jsonString;

            } catch (IOException e) {
               return e.getMessage();
            }
        } catch (Exception e) {
           return e.getMessage();
        }

    }



    private JSONArray parseJSONArray(String s) {
        try {
            jsonArray = new JSONArray(s);

        } catch (JSONException e) {
            e.getMessage();
        }

        return jsonArray;

    }

    private JSONObject parseJSONObject(String s) {
        try {
            jsonObject = new JSONObject(s);

        } catch (JSONException e) {
            e.getMessage();
        }

        return jsonObject;

    }

    //Interface for JSON Arrays
    public interface OnTaskComplete {
        public void setTaskComplete(JSONArray jsonArray);
    }

    public void setTaskCompleteListener(OnTaskComplete onTaskComplete) {

        this.onTaskComplete = onTaskComplete;
    }

    // Interface for JSON Objects
    public interface OnParseJSONObjectComplete {
        public void setParseJSONObjectComplete(JSONObject jsonObject);
    }

    public void setParseJSONObjectCompleteListener(OnParseJSONObjectComplete onParseJSONObjectComplete){
        this.onParseJSONObjectComplete = onParseJSONObjectComplete;
    }



} // end APIInterface



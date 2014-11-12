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

    //JSONObject json;
    JSONArray json;
    InputStream is = null;


    public interface OnTaskComplete {
        public void setMyTaskComplete(JSONArray json);
    }

    public void setMyTaskCompleteListener(OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
    }


    @Override
    protected String doInBackground(String... strings) {

        String urlString = strings[0]; // URL to call

        String result = "Success!";

        // GET
        try {
            Log.i(TAG, "URL to be called: " + API_URL + urlString );
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet get = new HttpGet(API_URL + urlString);
            try {
                HttpResponse httpresponse = httpclient.execute(get);
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

        //Convert to JSONArray
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                is.close();
                String jsonString = sb.toString();
                System.out.println("JSON-string from API: " + jsonString);
                try {
                    json = new JSONArray(jsonString);
                }catch (JSONException e){
                    e.getMessage();
                }
            }catch (IOException e){
                e.getMessage();
            }
        }catch (Exception e){
            e.getMessage();
        }

        System.out.println("JSONObject: " + json);
        return result;

    }

    @Override
    protected void onPostExecute(String result) {
        onTaskComplete.setMyTaskComplete(json);
        Log.i(TAG, "Result: " + result);
    }



    /**
     * Method for checking the data received from the web API
     *
     * @param inputStream Bytestream of the data received from the web resource
     * @return  Returns a readable string of the converted bytestream
     * @throws IOException
     */
    private static String convertBufferedInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }


} // end APIInterface



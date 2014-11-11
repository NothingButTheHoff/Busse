package com.pefi.Busse;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pererikfinstad on 11/11/14.
 */
public class APIInterface extends AsyncTask<String, String, String> {
    public final static String TAG = "APIInterface";
    public final static String API_URL = "http://reisapi.ruter.no/";


    @Override
    protected String doInBackground(String... strings) {

        String urlString = strings[0]; // URL to call

        String result = "";

        InputStream in = null;

        // GET
        try {

            Log.i(TAG, "URL to be sent: " + API_URL + urlString);

            URL url = new URL(API_URL + urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream());



            Log.i(TAG, "InputStream = " + convertBufferedInputStreamToString(in));

            JSONObject json = new JSONObject(convertBufferedInputStreamToString(in));
            System.out.println(json.toString());
        } catch (Exception e ) {

            System.out.println("Could not connect to the API: " + e.getMessage());

            return e.getMessage();

        }

        return result;

    }

    @Override
    protected void onPostExecute(String result) {
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



package com.example.anshujalan.newsnow;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AnshuJalan on 2/4/2018.
 */

public class QueryUtils
{
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static ArrayList<News> extractNews(String requestUrl)
    {
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<News> news = new ArrayList<>();

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try
        {
            jsonResponse = makeHttpResponse(url);
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG, "Unable to make Http request", e);
        }

        news = extractInformation(jsonResponse);
        return news;
    }

    private static URL createUrl(String requestUrl)
    {
        URL url = null;
        try
        {
            url = new URL(requestUrl);
        }
        catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, "The requested Url is malformed", e);
        }
        return url;
    }

    private static String makeHttpResponse(URL url) throws IOException
    {
        String jsonResponse = null;

        if(url == null)
        {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        }
        catch(IOException e)
        {
            Log.e(LOG_TAG, "Problem connecting", e);
        }
        finally {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if(inputStream != null)
            {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream)throws IOException
    {
        StringBuilder output = new StringBuilder();

        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        if(inputStream != null) {
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);


            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static ArrayList<News> extractInformation(String jsonResponse)
    {
        ArrayList<News> newsList = new ArrayList<>();

        if(TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for(int i = 0; i < results.length(); i++)
            {
                JSONObject item = results.getJSONObject(i);

                String title = item.getString("webTitle");
                String url = item.getString("webUrl");
                String type = item.getString("type");

                newsList.add(new News(title, type, url));
            }
        }
        catch(JSONException e)
        {
            Log.e(LOG_TAG, "Unable to read", e);
        }

        return newsList;
    }
}

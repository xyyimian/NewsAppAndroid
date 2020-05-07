package com.example.newsapp;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutoSuggest {
    private static AutoSuggest mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public AutoSuggest(Context ctx){
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AutoSuggest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AutoSuggest(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) throws JSONException {
        String url = "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?q="+query;
        JSONObject header = new JSONObject();
        header.put("Ocp-Apim-Subscription-Key", "4d04d04e40264f9a8ea2c88fa1fbedee");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, header,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            ArrayList<String> sgList = new ArrayList<String>();
                            JSONArray sgArray = response.getJSONArray("suggestionGroups").getJSONObject(0).getJSONArray("searchSuggestions");
                            for(int i = 0; i < sgArray.length(); ++i){
                                sgList.add(sgArray.getJSONObject(i).optString("displayText"));
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, errorListener);

        AutoSuggest.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }
}



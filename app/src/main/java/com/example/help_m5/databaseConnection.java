package com.example.help_m5;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class databaseConnection {

    final String Hizan_alibaba_url = "http://47.251.34.10:3000/";
    final String TAG = "databaseConnection";
    final short posts = 0;
    final short study = 1;
    final short entertainments = 2;
    final short restaurants = 3;

    /**Parameter:
     * int facility_type : a integer representing the type
     * int page_number : what range of facility to be search in database
     * Purpose:
     * Get preview for desired facilities
     * Reture:
     * A string (Json) that hold those information
     *  */
    public String getFacilities(int facility_type, int page_number, Context applicationContext){
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        queue.start();
        HashMap<String, String> params = new HashMap<String,String>();

        String facilityToFetch = "";


        int start = (page_number - 1) * 5;
        int end = page_number * 5;

        params.put("start", ""+start);
        params.put("end", ""+start);

        switch (facility_type){
            case posts:
                facilityToFetch = "posts";
            case study:
                facilityToFetch = "study";
            case entertainments:
                facilityToFetch = "entertainments";
            case restaurants:
                facilityToFetch = "restaurants";
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, Hizan_alibaba_url+facilityToFetch, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response is: "+response.toString());

//                        try {
//                            Log.d(TAG, response.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERROR when connecting to database");
            }
        });
        queue.add(jsObjRequest);
        return null;
    }

}

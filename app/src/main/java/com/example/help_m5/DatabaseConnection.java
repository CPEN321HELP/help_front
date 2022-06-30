package com.example.help_m5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import com.example.help_m5.databinding.FragmentEntertainmentsBinding;
import com.example.help_m5.databinding.FragmentPostsBinding;

public class DatabaseConnection {


    final String Hizan_alibaba_url = "http://47.251.34.10:3000/";
    final String TAG = "databaseConnection";
    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;

    /**Parameter:
     * int facility_type : a integer representing the type
     * int page_number : what range of facility to be search in database
     * Purpose:
     * Get preview for desired facilities
     * Reture:
     * A string (Json) that hold those information
     *  */
    public void getFacilities(Object Binding, int facility_type, int page_number, Context applicationContext, FragmentEntertainmentsBinding binding){
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        queue.start();
        HashMap<String, String> params = new HashMap<String,String>();

        String facilityToFetch = "";
//        Log.d(TAG, "0: "+facility_type);
//        Log.d(TAG, "0: "+facilityToFetch);

        params.put("page_number", ""+page_number);

        Log.d(TAG, ""+facility_type);
        switch (facility_type){
            case posts:
                facilityToFetch = "posts";
                break;
            case study:
                facilityToFetch = "study";
                break;
            case entertainments:
                facilityToFetch = "entertainments";
                break;
            case restaurants:
                facilityToFetch = "restaurants";
                break;
        }
//        Log.d(TAG, "1: "+facility_type);
//        Log.d(TAG, facilityToFetch);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, Hizan_alibaba_url+facilityToFetch, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response is: "+response.toString());
                try {
                    int length = response.getInt("length");
                    JSONArray facilities = response.getJSONArray("result");
                    for(int index =0; index <length; index++){
                        JSONArray facility1 = facilities.getJSONArray(index);
                        switch (index){
                            case 0:
                                load_part1(binding, facility_type, facility1);
                                break;
                            case 1:
                                load_part2(binding, facility_type, facility1);
                                break;
                            case 2:
                                load_part3(binding, facility_type, facility1);
                                break;
                            case 3:
                                load_part4(binding, facility_type, facility1);
                                break;
                            case 4:
                                load_part5(binding, facility_type, facility1);
                                break;
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERROR when connecting to database");
            }
        });
        queue.add(jsObjRequest);
        Log.d(TAG, "response here is: "+ jsObjRequest.toString());;
    }

    private void load_part1(Object Binding, int facility_type, JSONArray result)  {
        TextView titleTextView_facility1 = null, dateTextView_facility1 =null, contentTextView_facility1 = null, facilityID_textView1 = null;
        RatingBar ratingBar_facility1 = null;

        switch (facility_type){
            case posts:
//                titleTextView_facility1 = ((FragmentPostsBinding) Binding).titleTextViewFacility1;
//                dateTextView_facility1 = ((FragmentPostsBinding) Binding).dateTextViewFacility1;
//                contentTextView_facility1 = ((FragmentPostsBinding) Binding).contentTextViewFacility1;
//                ratingBar_facility1 = ((FragmentPostsBinding) Binding).ratingBarFacility1;
//                facilityID_textView1 = ((FragmentPostsBinding) Binding).facilityIDTextView1;
                break;
            case study:
//                titleTextView_facility1 = ((FragmentStudyBinding) Binding).titleTextViewFacility1;
//                dateTextView_facility1 = ((FragmentStudyBinding) Binding).dateTextViewFacility1;
//                contentTextView_facility1 = ((FragmentStudyBinding) Binding).contentTextViewFacility1;
//                ratingBar_facility1 = ((FragmentStudyBinding) Binding).ratingBarFacility1;
//                facilityID_textView1 = ((FragmentStudyBinding) Binding).facilityIDTextView1;

                break;
            case entertainments:
                titleTextView_facility1 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility1;
                dateTextView_facility1 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility1;
                contentTextView_facility1 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility1;
                ratingBar_facility1 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility1;
                facilityID_textView1 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextView1;
                break;
            case restaurants:
//                titleTextView_facility1 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility1;
//                dateTextView_facility1 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility1;
//                contentTextView_facility1 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility1;
//                ratingBar_facility1 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility1;
//                facilityID_textView1 = ((FragmentRestaurantsBinding) Binding).facilityIDTextView1;
                break;
        }
        try{
            facilityID_textView1.setText(result.getString(0));
        }catch (JSONException E){
            facilityID_textView1.setText("ERROR when loading title 1");
        }

        try{
            ratingBar_facility1.setRating((float) result.getDouble(1));
        }catch (JSONException E){
            ratingBar_facility1.setRating((float) 0);
        }

        try{
            titleTextView_facility1.setText(result.getString(2));
        }catch (JSONException E){
            titleTextView_facility1.setText("ERROR when loading title 1");
        }

        try{
            contentTextView_facility1.setText(result.getString(3));
        }catch (JSONException E){
            contentTextView_facility1.setText("ERROR when loading title 1");
        }

        try{
            dateTextView_facility1.setText(result.getString(4));
        }catch (JSONException E){
            dateTextView_facility1.setText("ERROR when loading date 1");
        }
    }

    private void load_part2(Object Binding, int facility_type, JSONArray result)  {
        TextView titleTextView_facility2 = null, dateTextView_facility2 =null, contentTextView_facility2 = null, facilityID_textView2 = null;
        RatingBar ratingBar_facility2 = null;

        switch (facility_type){
            case posts:
//                titleTextView_facility2 = ((FragmentPostsBinding) Binding).titleTextViewFacility2;
//                dateTextView_facility2 = ((FragmentPostsBinding) Binding).dateTextViewFacility2;
//                contentTextView_facility2 = ((FragmentPostsBinding) Binding).contentTextViewFacility2;
//                ratingBar_facility2 = ((FragmentPostsBinding) Binding).ratingBarFacility2;
//                facilityID_textView2 = ((FragmentPostsBinding) Binding).facilityIDTextView2;
                break;
            case study:
//                titleTextView_facility2 = ((FragmentStudyBinding) Binding).titleTextViewFacility2;
//                dateTextView_facility2 = ((FragmentStudyBinding) Binding).dateTextViewFacility2;
//                contentTextView_facility2 = ((FragmentStudyBinding) Binding).contentTextViewFacility2;
//                ratingBar_facility2 = ((FragmentStudyBinding) Binding).ratingBarFacility2;
//                facilityID_textView2 = ((FragmentStudyBinding) Binding).facilityIDTextView2;

                break;
            case entertainments:
                titleTextView_facility2 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility2;
                dateTextView_facility2 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility2;
                contentTextView_facility2 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility2;
                ratingBar_facility2 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility2;
                facilityID_textView2 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextView2;
                break;
            case restaurants:
//                titleTextView_facility2 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility2;
//                dateTextView_facility2 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility2;
//                contentTextView_facility2 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility2;
//                ratingBar_facility2 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility2;
//                facilityID_textView2 = ((FragmentRestaurantsBinding) Binding).facilityIDTextView2;
                break;
        }
        try{
            facilityID_textView2.setText(result.getString(0));
        }catch (JSONException E){
            facilityID_textView2.setText("ERROR when loading id 2");
        }

        try{
            ratingBar_facility2.setRating((float) result.getDouble(1));
        }catch (JSONException E){
            ratingBar_facility2.setRating((float) 0);
        }

        try{
            titleTextView_facility2.setText(result.getString(2));
        }catch (JSONException E){
            titleTextView_facility2.setText("ERROR when loading title 2");
        }

        try{
            contentTextView_facility2.setText(result.getString(3));
        }catch (JSONException E){
            contentTextView_facility2.setText("ERROR when loading title 2");
        }

        try{
            dateTextView_facility2.setText(result.getString(4));
        }catch (JSONException E){
            dateTextView_facility2.setText("ERROR when loading date 2");
        }
    }

    private void load_part3(Object Binding, int facility_type, JSONArray result)  {
        TextView titleTextView_facility3 = null, dateTextView_facility3 =null, contentTextView_facility3 = null, facilityID_textView3 = null;
        RatingBar ratingBar_facility3 = null;

        switch (facility_type){
            case posts:
//                titleTextView_facility3 = ((FragmentPostsBinding) Binding).titleTextViewFacility3;
//                dateTextView_facility3 = ((FragmentPostsBinding) Binding).dateTextViewFacility3;
//                contentTextView_facility3 = ((FragmentPostsBinding) Binding).contentTextViewFacility3;
//                ratingBar_facility3 = ((FragmentPostsBinding) Binding).ratingBarFacility3;
//                facilityID_textView3 = ((FragmentPostsBinding) Binding).facilityIDTextView3;
                break;
            case study:
//                titleTextView_facility3 = ((FragmentStudyBinding) Binding).titleTextViewFacility3;
//                dateTextView_facility3 = ((FragmentStudyBinding) Binding).dateTextViewFacility3;
//                contentTextView_facility3 = ((FragmentStudyBinding) Binding).contentTextViewFacility3;
//                ratingBar_facility3 = ((FragmentStudyBinding) Binding).ratingBarFacility3;
//                facilityID_textView3 = ((FragmentStudyBinding) Binding).facilityIDTextView3;

                break;
            case entertainments:
                titleTextView_facility3 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility3;
                dateTextView_facility3 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility3;
                contentTextView_facility3 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility3;
                ratingBar_facility3 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility3;
                facilityID_textView3 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextView3;
                break;
            case restaurants:
//                titleTextView_facility3 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility3;
//                dateTextView_facility3 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility3;
//                contentTextView_facility3 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility3;
//                ratingBar_facility3 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility3;
//                facilityID_textView3 = ((FragmentRestaurantsBinding) Binding).facilityIDTextView3;
                break;
        }
        try{
            facilityID_textView3.setText(result.getString(0));
        }catch (JSONException E){
            facilityID_textView3.setText("ERROR when loading id 3");
        }

        try{
            ratingBar_facility3.setRating((float) result.getDouble(1));
        }catch (JSONException E){
            ratingBar_facility3.setRating((float) 0);
        }

        try{
            titleTextView_facility3.setText(result.getString(2));
        }catch (JSONException E){
            titleTextView_facility3.setText("ERROR when loading title 3");
        }

        try{
            contentTextView_facility3.setText(result.getString(3));
        }catch (JSONException E){
            contentTextView_facility3.setText("ERROR when loading title 3");
        }

        try{
            dateTextView_facility3.setText(result.getString(4));
        }catch (JSONException E){
            dateTextView_facility3.setText("ERROR when loading date 3");
        }
    }

    private void load_part4(Object Binding, int facility_type, JSONArray result)  {
        TextView titleTextView_facility4 = null, dateTextView_facility4 =null, contentTextView_facility4 = null, facilityID_textView4 = null;
        RatingBar ratingBar_facility4 = null;

        switch (facility_type){
            case posts:
//                titleTextView_facility4 = ((FragmentPostsBinding) Binding).titleTextViewFacility4;
//                dateTextView_facility4 = ((FragmentPostsBinding) Binding).dateTextViewFacility4;
//                contentTextView_facility4 = ((FragmentPostsBinding) Binding).contentTextViewFacility4;
//                ratingBar_facility4 = ((FragmentPostsBinding) Binding).ratingBarFacility4;
//                facilityID_textView4 = ((FragmentPostsBinding) Binding).facilityIDTextView4;
                break;
            case study:
//                titleTextView_facility4 = ((FragmentStudyBinding) Binding).titleTextViewFacility4;
//                dateTextView_facility4 = ((FragmentStudyBinding) Binding).dateTextViewFacility4;
//                contentTextView_facility4 = ((FragmentStudyBinding) Binding).contentTextViewFacility4;
//                ratingBar_facility4 = ((FragmentStudyBinding) Binding).ratingBarFacility4;
//                facilityID_textView4 = ((FragmentStudyBinding) Binding).facilityIDTextView4;

                break;
            case entertainments:
                titleTextView_facility4 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility4;
                dateTextView_facility4 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility4;
                contentTextView_facility4 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility4;
                ratingBar_facility4 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility4;
                facilityID_textView4 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextView4;
                break;
            case restaurants:
//                titleTextView_facility4 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility4;
//                dateTextView_facility4 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility4;
//                contentTextView_facility4 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility4;
//                ratingBar_facility4 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility4;
//                facilityID_textView4 = ((FragmentRestaurantsBinding) Binding).facilityIDTextView4;
                break;
        }
        try{
            facilityID_textView4.setText(result.getString(0));
        }catch (JSONException E){
            facilityID_textView4.setText("ERROR when loading id 4");
        }

        try{
            ratingBar_facility4.setRating((float) result.getDouble(1));
        }catch (JSONException E){
            ratingBar_facility4.setRating((float) 0);
        }

        try{
            titleTextView_facility4.setText(result.getString(2));
        }catch (JSONException E){
            titleTextView_facility4.setText("ERROR when loading title 4");
        }

        try{
            contentTextView_facility4.setText(result.getString(3));
        }catch (JSONException E){
            contentTextView_facility4.setText("ERROR when loading title 4");
        }

        try{
            dateTextView_facility4.setText(result.getString(4));
        }catch (JSONException E){
            dateTextView_facility4.setText("ERROR when loading date 4");
        }
    }

    private void load_part5(Object Binding, int facility_type, JSONArray result)  {
        TextView titleTextView_facility5 = null, dateTextView_facility5 =null, contentTextView_facility5 = null, facilityID_textView5 = null;
        RatingBar ratingBar_facility5 = null;

        switch (facility_type){
            case posts:
//                titleTextView_facility5 = ((FragmentPostsBinding) Binding).titleTextViewFacility5;
//                dateTextView_facility5 = ((FragmentPostsBinding) Binding).dateTextViewFacility5;
//                contentTextView_facility5 = ((FragmentPostsBinding) Binding).contentTextViewFacility5;
//                ratingBar_facility5 = ((FragmentPostsBinding) Binding).ratingBarFacility5;
//                facilityID_textView5 = ((FragmentPostsBinding) Binding).facilityIDTextView5;
                break;
            case study:
//                titleTextView_facility5 = ((FragmentStudyBinding) Binding).titleTextViewFacility5;
//                dateTextView_facility5 = ((FragmentStudyBinding) Binding).dateTextViewFacility5;
//                contentTextView_facility5 = ((FragmentStudyBinding) Binding).contentTextViewFacility5;
//                ratingBar_facility5 = ((FragmentStudyBinding) Binding).ratingBarFacility5;
//                facilityID_textView5 = ((FragmentStudyBinding) Binding).facilityIDTextView5;

                break;
            case entertainments:
                titleTextView_facility5 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility5;
                dateTextView_facility5 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility5;
                contentTextView_facility5 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility5;
                ratingBar_facility5 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility5;
                facilityID_textView5 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextView5;
                break;
            case restaurants:
//                titleTextView_facility5 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility5;
//                dateTextView_facility5 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility5;
//                contentTextView_facility5 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility5;
//                ratingBar_facility5 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility5;
//                facilityID_textView5 = ((FragmentRestaurantsBinding) Binding).facilityIDTextView5;
                break;
        }
        try{
            facilityID_textView5.setText(result.getString(0));
        }catch (JSONException E){
            facilityID_textView5.setText("ERROR when loading id 5");
        }

        try{
            ratingBar_facility5.setRating((float) result.getDouble(1));
        }catch (JSONException E){
            ratingBar_facility5.setRating((float) 0);
        }

        try{
            titleTextView_facility5.setText(result.getString(2));
        }catch (JSONException E){
            titleTextView_facility5.setText("ERROR when loading title 5");
        }

        try{
            contentTextView_facility5.setText(result.getString(3));
        }catch (JSONException E){
            contentTextView_facility5.setText("ERROR when loading title 5");
        }

        try{
            dateTextView_facility5.setText(result.getString(4));
        }catch (JSONException E){
            dateTextView_facility5.setText("ERROR when loading date 5");
        }
    }

}

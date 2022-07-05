package com.example.help_m5;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;

import com.example.help_m5.databinding.FragmentEntertainmentsBinding;

//DatabaseConnection
public class DatabaseConnection {


    final String vm_ip = "http://47.251.34.10:3000/";
    final String TAG = "databaseConnection";

    static final int posts = 0;
    static final int study = 1;
    static final int entertainments = 2;
    static final int restaurants = 3;
    static final int report_user = 4;
    static final int report_comment = 5;
    static final int report_facility = 6;

    static final int normal_local_load = 0;
    static final int normal_server_load = 1;
    static final int reached_end = 2;
    static final int server_error = 3;
    static final int local_error = 4;

    /**
     * Parameter:
     * int facility_type : a integer representing the type
     * int page_number : what range of facility to be search in database
     * Purpose:
     * Get preview for desired facilities
     * Reture:
     * A string (Json) that hold those information
     */
    static int status = server_error;

    public int getFacilities(Object binding, int facility_type, int page_number, Context applicationContext, boolean is_search, boolean is_report, String content_to_search) {
        String fileName = "";
        if (is_search) {
            fileName = "search_" + getStringType(facility_type) + ".json";
        } else if (is_report){
            fileName = "report" + getStringType(facility_type) + ".json";
        } else {
            fileName = getStringType(facility_type) + ".json";
        }
        return searchFacilities(binding, facility_type, page_number, applicationContext, is_search, is_report, content_to_search, fileName);
    }

    /**
     * @param binding            : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type      : int representing the type of facility calling this function
     * @param page_number        : what range to load
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param content_to_search  : string user typed in search box
     * @return : 0, indicate successfully load the data from cached file to screen
     * 4, indicate unsuccessfully load the data from cached file to screen
     * 1, indicate successfully load the data from server to screen
     * 3, indicate unsuccessfully load the data from server to screen
     * 2, reached end of show
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    private int searchFacilities(Object binding, int facility_type, int page_number, Context applicationContext, boolean is_search, boolean is_report, String content_to_search, String fileName) {
        if (isCached(applicationContext, fileName)) {//page up and page down should go here
            try {
                JSONObject data = new JSONObject(readFromJson(applicationContext, fileName));
                int result = loadToScreen(binding, facility_type, page_number, data);
                if (result == 1) {
                    return reached_end;
                }
                return normal_local_load;
            } catch (JSONException e) {
                e.printStackTrace();
                return local_error;
            }
        } else { //search should go here
            final int[] load_status = {normal_server_load};
            final RequestQueue queue = Volley.newRequestQueue(applicationContext);
            HashMap<String, String> params = new HashMap<String, String>();
            queue.start();
            String url = vm_ip + getStringType(facility_type);
            params.put("page_number", "" + page_number);

            if (is_search) {
                url += "/search";
                params.put("search", "" + content_to_search);
            }

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    Log.d(TAG, "response is: " + response.toString());
                    int result = writeToJson(applicationContext, response, fileName);

                    if (result == 2) {
                        load_status[0] = local_error;// IOException
                        return;
                    }

                    try {
                        JSONObject data = new JSONObject(readFromJson(applicationContext, fileName));
                        int result2 = loadToScreen(binding, facility_type, page_number, data);
                        if (result2 == 1) {
                            load_status[0] = reached_end; //reached end of facility json array
                        }
                    } catch (JSONException e) {
                        load_status[0] = local_error; // error reading json file
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    load_status[0] = server_error;
                    Log.d(TAG, "ERROR when connecting to database");
                }
            });
            queue.add(jsObjRequest);
            Log.d(TAG, "load_status is " + load_status[0]);
            if (load_status[0] == normal_server_load) {
                return normal_server_load;
            } else if (load_status[0] == reached_end) {
                return reached_end;
            } else if (load_status[0] == local_error) {
                return local_error;
            } else {
                return server_error;
            }
        }
    }

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param page_number   : what range to load
     * @param data          : Json format data to be process and show on screen
     * @return 0 execute as expected, 1 reached end of show
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    private int loadToScreen(Object binding, int facility_type, int page_number, JSONObject data) {
        try {
            int length = data.getInt("length");
            int start = (page_number - 1) * 5;
            int end = Math.min((page_number * 5), length);
            int counter = 0;
            JSONArray array = data.getJSONArray("result");

            for (int index = start; index < end; index++) {
                loadToFragment(binding, facility_type, array.getJSONArray(index), counter);
                counter++;
            }
            if (end == length) {
                return 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param fileName           :
     * @return true, if file is cached; false otherwise
     * @Pupose : to check if file is cached in internal storage, this method is used only by searchFacilities()
     */
    private boolean isCached(Context applicationContext, String fileName) {
        try {
            File file = new File(applicationContext.getFilesDir(), fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param facility_info : a json array that holds information about facilities
     * @param index         : a int index range from 0 to 5
     * @Pupose load information from JSONArray to texView
     */
    private void loadToFragment(Object binding, int facility_type, JSONArray facility_info, int index) {
        switch (index) {
            case 0:
                load_facility1(binding, facility_type, facility_info);
                break;
            case 1:
                load_facility2(binding, facility_type, facility_info);
                break;
            case 2:
                load_facility3(binding, facility_type, facility_info);
                break;
            case 3:
                load_facility4(binding, facility_type, facility_info);
                break;
            case 4:
                load_facility5(binding, facility_type, facility_info);
                break;
        }
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param response           : response from server
     * @return 0 if cached successfully; 1, if File Already Exists; 2 if IOException.
     * @Pupose write json response from server to a file
     */
    private int writeToJson(Context applicationContext, JSONObject response, String fileName) {
        try {
            File file = new File(applicationContext.getFilesDir(), fileName);
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(response.toString().getBytes());
            writer.close();
            Log.d(TAG, "write to file" + fileName + " path is: " + file.getCanonicalPath());
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        }
        return 0;
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param fileName           : file name to be read
     * @return String of corrsponding file; "1" if FileNotFoundException; "2" if IOException
     * @Pupose read json response from file
     */
    private String readFromJson(Context applicationContext, String fileName) {
        try {
            File file = new File(applicationContext.getFilesDir(), fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            // This responce will have Json Format String
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "1";
        } catch (IOException e) {
            e.printStackTrace();
            return "2";
        }
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @Pupose clean all files stored in /data/data/com.example.help_m5/files/
     */
    public void cleanCaches(Context applicationContext){
        if( applicationContext == null){
            Log.d(TAG, "applicationContext Null");
            return;
        }
        File targetDir = applicationContext.getFilesDir();
        if( targetDir == null){
            Log.d(TAG, "error cleaning");
            return;
        }
        if(targetDir.isDirectory()){
            Log.d(TAG, "start cleaning");
        }
        File[] files = targetDir.listFiles();
        if( files == null){
            Log.d(TAG, "dir empty, nothing to clean");
            return;
        }
        for(File f : files){
            f.delete();
        }
    }
    /**
     * @param facility_type : int representing the type of facility calling this function
     * @return String of facility type
     * @Pupose take int facility_type and return string of facility_type
     */
    private String getStringType(int facility_type) {
        String facilityToFetch = "";
        switch (facility_type) {
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
            case report_user:
                facilityToFetch = "report/user";
                break;
            case report_facility:
                facilityToFetch = "report/facility";
                break;
            case report_comment:
                facilityToFetch = "report/comment";

                break;
        }
        return facilityToFetch;
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility1(Object Binding, int facility_type, JSONArray result) {
        TextView titleTextView_facility1 = null, dateTextView_facility1 = null, contentTextView_facility1 = null, facilityID_textView1_facility1 = null;
        RatingBar ratingBar_facility1 = null;
        ConstraintLayout constraintLayout_facility1 = null;
        switch (facility_type) {
            case posts:
//                titleTextView_facility1 = ((FragmentPostsBinding) Binding).titleTextViewFacility1;
//                dateTextView_facility1 = ((FragmentPostsBinding) Binding).dateTextViewFacility1;
//                contentTextView_facility1 = ((FragmentPostsBinding) Binding).contentTextViewFacility1;
//                ratingBar_facility1 = ((FragmentPostsBinding) Binding).ratingBarFacility1;
//                facilityID_textView1 = ((FragmentPostsBinding) Binding).facilityIDTextViewFacility1;
//                constraintLayout_facility1 = ((FragmentPostsBinding) Binding).facility1;
                break;
            case study:
//                titleTextView_facility1 = ((FragmentStudyBinding) Binding).titleTextViewFacility1;
//                dateTextView_facility1 = ((FragmentStudyBinding) Binding).dateTextViewFacility1;
//                contentTextView_facility1 = ((FragmentStudyBinding) Binding).contentTextViewFacility1;
//                ratingBar_facility1 = ((FragmentStudyBinding) Binding).ratingBarFacility1;
//                facilityID_textView1 = ((FragmentStudyBinding) Binding).facilityIDTextViewFacility1;
//                constraintLayout_facility1 = ((FragmentStudyBinding) Binding).facility1;
                break;
            case entertainments:
                titleTextView_facility1 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility1;
                dateTextView_facility1 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility1;
                contentTextView_facility1 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility1;
                ratingBar_facility1 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility1;
                facilityID_textView1_facility1 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextViewFacility1;
                constraintLayout_facility1 = ((FragmentEntertainmentsBinding) Binding).facility1;
                break;
            case restaurants:
//                titleTextView_facility1 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility1;
//                dateTextView_facility1 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility1;
//                contentTextView_facility1 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility1;
//                ratingBar_facility1 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility1;
//                facilityID_textView1 = ((FragmentRestaurantsBinding) Binding).facilityIDTextViewFacility1;
//                constraintLayout_facility1 = ((FragmentRestaurantsBinding) Binding).facility1;

                break;
        }
        try {
            facilityID_textView1_facility1.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility1.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility1.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility1.setRating((float) 0);
        }

        try {
            titleTextView_facility1.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility1.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility1.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility1.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility1.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility1.setText("ERROR when loading date 1");
        }
        constraintLayout_facility1.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility2(Object Binding, int facility_type, JSONArray result) {
        TextView titleTextView_facility2 = null, dateTextView_facility2 = null, contentTextView_facility2 = null, facilityID_textView1_facility2 = null;
        RatingBar ratingBar_facility2 = null;
        ConstraintLayout constraintLayout_facility2 = null;
        switch (facility_type) {
            case posts:
//                titleTextView_facility2 = ((FragmentPostsBinding) Binding).titleTextViewFacility2;
//                dateTextView_facility2 = ((FragmentPostsBinding) Binding).dateTextViewFacility2;
//                contentTextView_facility2 = ((FragmentPostsBinding) Binding).contentTextViewFacility2;
//                ratingBar_facility2 = ((FragmentPostsBinding) Binding).ratingBarFacility2;
//                facilityID_textView1 = ((FragmentPostsBinding) Binding).facilityIDTextViewFacility2;
//                constraintLayout_facility2 = ((FragmentPostsBinding) Binding).facility2;
                break;
            case study:
//                titleTextView_facility2 = ((FragmentStudyBinding) Binding).titleTextViewFacility2;
//                dateTextView_facility2 = ((FragmentStudyBinding) Binding).dateTextViewFacility2;
//                contentTextView_facility2 = ((FragmentStudyBinding) Binding).contentTextViewFacility2;
//                ratingBar_facility2 = ((FragmentStudyBinding) Binding).ratingBarFacility2;
//                facilityID_textView1 = ((FragmentStudyBinding) Binding).facilityIDTextViewFacility2;
//                constraintLayout_facility2 = ((FragmentStudyBinding) Binding).facility2;
                break;
            case entertainments:
                titleTextView_facility2 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility2;
                dateTextView_facility2 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility2;
                contentTextView_facility2 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility2;
                ratingBar_facility2 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility2;
                facilityID_textView1_facility2 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextViewFacility2;
                constraintLayout_facility2 = ((FragmentEntertainmentsBinding) Binding).facility2;
                break;
            case restaurants:
//                titleTextView_facility2 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility2;
//                dateTextView_facility2 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility2;
//                contentTextView_facility2 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility2;
//                ratingBar_facility2 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility2;
//                facilityID_textView1 = ((FragmentRestaurantsBinding) Binding).facilityIDTextViewFacility2;
//                constraintLayout_facility2 = ((FragmentRestaurantsBinding) Binding).facility2;

                break;
        }
        try {
            facilityID_textView1_facility2.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility2.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility2.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility2.setRating((float) 0);
        }

        try {
            titleTextView_facility2.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility2.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility2.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility2.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility2.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility2.setText("ERROR when loading date 1");
        }
        constraintLayout_facility2.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility3(Object Binding, int facility_type, JSONArray result) {
        TextView titleTextView_facility3 = null, dateTextView_facility3 = null, contentTextView_facility3 = null, facilityID_textView1_facility3 = null;
        RatingBar ratingBar_facility3 = null;
        ConstraintLayout constraintLayout_facility3 = null;
        switch (facility_type) {
            case posts:
//                titleTextView_facility3 = ((FragmentPostsBinding) Binding).titleTextViewFacility3;
//                dateTextView_facility3 = ((FragmentPostsBinding) Binding).dateTextViewFacility3;
//                contentTextView_facility3 = ((FragmentPostsBinding) Binding).contentTextViewFacility3;
//                ratingBar_facility3 = ((FragmentPostsBinding) Binding).ratingBarFacility3;
//                facilityID_textView1 = ((FragmentPostsBinding) Binding).facilityIDTextViewFacility3;
//                constraintLayout_facility3 = ((FragmentPostsBinding) Binding).facility3;
                break;
            case study:
//                titleTextView_facility3 = ((FragmentStudyBinding) Binding).titleTextViewFacility3;
//                dateTextView_facility3 = ((FragmentStudyBinding) Binding).dateTextViewFacility3;
//                contentTextView_facility3 = ((FragmentStudyBinding) Binding).contentTextViewFacility3;
//                ratingBar_facility3 = ((FragmentStudyBinding) Binding).ratingBarFacility3;
//                facilityID_textView1 = ((FragmentStudyBinding) Binding).facilityIDTextViewFacility3;
//                constraintLayout_facility3 = ((FragmentStudyBinding) Binding).facility3;
                break;
            case entertainments:
                titleTextView_facility3 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility3;
                dateTextView_facility3 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility3;
                contentTextView_facility3 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility3;
                ratingBar_facility3 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility3;
                facilityID_textView1_facility3 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextViewFacility3;
                constraintLayout_facility3 = ((FragmentEntertainmentsBinding) Binding).facility3;
                break;
            case restaurants:
//                titleTextView_facility3 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility3;
//                dateTextView_facility3 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility3;
//                contentTextView_facility3 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility3;
//                ratingBar_facility3 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility3;
//                facilityID_textView1 = ((FragmentRestaurantsBinding) Binding).facilityIDTextViewFacility3;
//                constraintLayout_facility3 = ((FragmentRestaurantsBinding) Binding).facility3;

                break;
        }
        try {
            facilityID_textView1_facility3.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility3.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility3.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility3.setRating((float) 0);
        }

        try {
            titleTextView_facility3.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility3.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility3.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility3.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility3.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility3.setText("ERROR when loading date 1");
        }
        constraintLayout_facility3.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility4(Object Binding, int facility_type, JSONArray result) {
        TextView titleTextView_facility4 = null, dateTextView_facility4 = null, contentTextView_facility4 = null, facilityID_textView1_facility4 = null;
        RatingBar ratingBar_facility4 = null;
        ConstraintLayout constraintLayout_facility4 = null;
        switch (facility_type) {
            case posts:
//                titleTextView_facility4 = ((FragmentPostsBinding) Binding).titleTextViewFacility4;
//                dateTextView_facility4 = ((FragmentPostsBinding) Binding).dateTextViewFacility4;
//                contentTextView_facility4 = ((FragmentPostsBinding) Binding).contentTextViewFacility4;
//                ratingBar_facility4 = ((FragmentPostsBinding) Binding).ratingBarFacility4;
//                facilityID_textView1 = ((FragmentPostsBinding) Binding).facilityIDTextViewFacility4;
//                constraintLayout_facility4 = ((FragmentPostsBinding) Binding).facility4;
                break;
            case study:
//                titleTextView_facility4 = ((FragmentStudyBinding) Binding).titleTextViewFacility4;
//                dateTextView_facility4 = ((FragmentStudyBinding) Binding).dateTextViewFacility4;
//                contentTextView_facility4 = ((FragmentStudyBinding) Binding).contentTextViewFacility4;
//                ratingBar_facility4 = ((FragmentStudyBinding) Binding).ratingBarFacility4;
//                facilityID_textView1 = ((FragmentStudyBinding) Binding).facilityIDTextViewFacility4;
//                constraintLayout_facility4 = ((FragmentStudyBinding) Binding).facility4;
                break;
            case entertainments:
                titleTextView_facility4 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility4;
                dateTextView_facility4 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility4;
                contentTextView_facility4 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility4;
                ratingBar_facility4 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility4;
                facilityID_textView1_facility4 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextViewFacility4;
                constraintLayout_facility4 = ((FragmentEntertainmentsBinding) Binding).facility4;
                break;
            case restaurants:
//                titleTextView_facility4 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility4;
//                dateTextView_facility4 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility4;
//                contentTextView_facility4 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility4;
//                ratingBar_facility4 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility4;
//                facilityID_textView1 = ((FragmentRestaurantsBinding) Binding).facilityIDTextViewFacility4;
//                constraintLayout_facility4 = ((FragmentRestaurantsBinding) Binding).facility4;

                break;
        }
        try {
            facilityID_textView1_facility4.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility4.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility4.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility4.setRating((float) 0);
        }

        try {
            titleTextView_facility4.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility4.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility4.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility4.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility4.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility4.setText("ERROR when loading date 1");
        }
        constraintLayout_facility4.setVisibility(View.VISIBLE);
    }

    /**
     * @param Binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param result        : a json array that holds information about facilities
     * @Pupose load information from JSONArray to texView
     */
    private void load_facility5(Object Binding, int facility_type, JSONArray result) {
        TextView titleTextView_facility5 = null, dateTextView_facility5 = null, contentTextView_facility5 = null, facilityID_textView1_facility5 = null;
        RatingBar ratingBar_facility5 = null;
        ConstraintLayout constraintLayout_facility5 = null;
        switch (facility_type) {
            case posts:
//                titleTextView_facility5 = ((FragmentPostsBinding) Binding).titleTextViewFacility5;
//                dateTextView_facility5 = ((FragmentPostsBinding) Binding).dateTextViewFacility5;
//                contentTextView_facility5 = ((FragmentPostsBinding) Binding).contentTextViewFacility5;
//                ratingBar_facility5 = ((FragmentPostsBinding) Binding).ratingBarFacility5;
//                facilityID_textView1 = ((FragmentPostsBinding) Binding).facilityIDTextViewFacility5;
//                constraintLayout_facility5 = ((FragmentPostsBinding) Binding).facility5;
                break;
            case study:
//                titleTextView_facility5 = ((FragmentStudyBinding) Binding).titleTextViewFacility5;
//                dateTextView_facility5 = ((FragmentStudyBinding) Binding).dateTextViewFacility5;
//                contentTextView_facility5 = ((FragmentStudyBinding) Binding).contentTextViewFacility5;
//                ratingBar_facility5 = ((FragmentStudyBinding) Binding).ratingBarFacility5;
//                facilityID_textView1 = ((FragmentStudyBinding) Binding).facilityIDTextViewFacility5;
//                constraintLayout_facility5 = ((FragmentStudyBinding) Binding).facility5;
                break;
            case entertainments:
                titleTextView_facility5 = ((FragmentEntertainmentsBinding) Binding).titleTextViewFacility5;
                dateTextView_facility5 = ((FragmentEntertainmentsBinding) Binding).dateTextViewFacility5;
                contentTextView_facility5 = ((FragmentEntertainmentsBinding) Binding).contentTextViewFacility5;
                ratingBar_facility5 = ((FragmentEntertainmentsBinding) Binding).ratingBarFacility5;
                facilityID_textView1_facility5 = ((FragmentEntertainmentsBinding) Binding).facilityIDTextViewFacility5;
                constraintLayout_facility5 = ((FragmentEntertainmentsBinding) Binding).facility5;
                break;
            case restaurants:
//                titleTextView_facility5 = ((FragmentRestaurantsBinding) Binding).titleTextViewFacility5;
//                dateTextView_facility5 = ((FragmentRestaurantsBinding) Binding).dateTextViewFacility5;
//                contentTextView_facility5 = ((FragmentRestaurantsBinding) Binding).contentTextViewFacility5;
//                ratingBar_facility5 = ((FragmentRestaurantsBinding) Binding).ratingBarFacility5;
//                facilityID_textView1 = ((FragmentRestaurantsBinding) Binding).facilityIDTextViewFacility5;
//                constraintLayout_facility5 = ((FragmentRestaurantsBinding) Binding).facility5;

                break;
        }
        try {
            facilityID_textView1_facility5.setText(result.getString(0));
        } catch (JSONException E) {
            facilityID_textView1_facility5.setText("ERROR when loading title 1");
        }

        try {
            ratingBar_facility5.setRating((float) result.getDouble(1));
        } catch (JSONException E) {
            ratingBar_facility5.setRating((float) 0);
        }

        try {
            titleTextView_facility5.setText(result.getString(2));
        } catch (JSONException E) {
            titleTextView_facility5.setText("ERROR when loading title 1");
        }

        try {
            contentTextView_facility5.setText(result.getString(3));
        } catch (JSONException E) {
            contentTextView_facility5.setText("ERROR when loading title 1");
        }

        try {
            dateTextView_facility5.setText(result.getString(4));
        } catch (JSONException E) {
            dateTextView_facility5.setText("ERROR when loading date 1");
        }
        constraintLayout_facility5.setVisibility(View.VISIBLE);
    }
}

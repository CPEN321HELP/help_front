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
import com.example.help_m5.databinding.FragmentFacilityBinding;
import com.example.help_m5.databinding.FragmentReportBinding;

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

//DatabaseConnection
public class DatabaseConnection {

    //    final String vm_ip = "http://20.213.243.141:8000/";
    final String vm_ip = "http://47.251.34.10:3000/"; //this is Hizan's alibaba server.
    final String TAG = "databaseConnection";

    //following are types of facility
    static final int posts = 0;
    static final int study = 1;
    static final int entertainments = 2;
    static final int restaurants = 3;
    static final int report_user = 4;
    static final int report_comment = 5;
    static final int report_facility = 6;
    //above are types of facility

    //below are types of error that could happen
    static final int normal_local_load = 0;
    static final int normal_server_load = 1;
    static final int reached_end = 2;
    static final int server_error = 3;
    static final int local_error = 4;
    static final int only_one_page = 5;
    //above are types of error that could happen

    int status_add_facility = normal_server_load;

    public int addFacility(Context applicationContext,String title, String description, String type, String imageLink, String longitude, String latitude){
        String url = vm_ip + "addFacility";
        Log.d(TAG, url);
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();

        params.put("title", title);
        params.put("description", description);
        params.put("long", longitude);
        params.put("lat", latitude);
        params.put("type", type);
        params.put("facilityImageLink", imageLink);

        Log.d(TAG, params.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response is: " + response.toString());
                status_add_facility = normal_server_load;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status_add_facility = server_error;
                Log.d(TAG, "ERROR when connecting to database getSpecificFacility");
            }
        });
        queue.add(jsObjRequest);
        Log.d(TAG, "status_add_facility is " + status_add_facility);
        return status_add_facility;
    }

    int status_getSpecificFacility = normal_server_load;

    public int getSpecificFacility(int facility_type, String facility_id, Context applicationContext){
        String fileName = "specific_facility.json";
        String url = vm_ip + "specific";
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);
        HashMap<String, String> params = new HashMap<String, String>();
        queue.start();
        params.put("facility_id", facility_id);
        params.put("facility_type", ""+facility_type);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response is: " + response.toString());
                writeToJson(applicationContext, response, fileName);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status_getSpecificFacility = server_error;
                Log.d(TAG, "ERROR when connecting to database getSpecificFacility");
            }
        });
        queue.add(jsObjRequest);
        Log.d(TAG, "status_getSpecificFacility is " + status_getSpecificFacility);
        return status_getSpecificFacility;
    }

    int status_getFacilities = normal_server_load;
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
        Log.d(TAG, "facility_type :"+facility_type);
        if (isCached(applicationContext, fileName)) {//page up and page down should go here
            try {
                JSONObject data = new JSONObject(readFromJson(applicationContext, fileName));
                int result = loadToScreen(binding, facility_type, page_number, data, is_report);
                if (result == 1) {
                    return reached_end;
                } else if (result == 2){
                    return only_one_page;
                }
                return normal_local_load;
            } catch (JSONException e) {
                e.printStackTrace();
                return local_error;
            }
        } else { //search should go here
            final RequestQueue queue = Volley.newRequestQueue(applicationContext);
            HashMap<String, String> params = new HashMap<String, String>();
            queue.start();
            params.put("page_number", "" + page_number);
            String url = vm_ip;

            if(is_report){
                url += "user/Report/" + getStringType(facility_type);
            }else {
                url += getStringType(facility_type);
                if (is_search) {
                    url += "/search";
                    params.put("search", "" + content_to_search);
                }else{
                    url += "/newest";
                }
            }
            Log.d(TAG,url);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "response is: " + response.toString());

                    int length = -1;
                    try {
                        length = response.getInt("length");
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    if (length == -1){
                        status_getFacilities = server_error;
                        return;
                    } else if(length<=5){
                        status_getFacilities = only_one_page;
                    }else{
                        int end = Math.min((page_number * 5), length);
                        if (end == length) {
                            status_getFacilities = reached_end;
                        }
                    }

                    int result = writeToJson(applicationContext, response, fileName);

                    if (result == 2) {
                        status_getFacilities = local_error;// IOException
                        return;
                    }

                    try {
                        JSONObject data = new JSONObject(readFromJson(applicationContext, fileName));
                        int result2 = loadToScreen(binding, facility_type, page_number, data, is_report);
                        if (result2 == 1) {
                            status_getFacilities = server_error; //reached end of facility json array
                        }
                    } catch (JSONException e) {
                        status_getFacilities = local_error; // error reading json file
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    status_getFacilities = server_error;
                    Log.d(TAG, "ERROR when connecting to database searchFacilities");
                }
            });
            queue.add(jsObjRequest);
            Log.d(TAG, "status_getFacilities is " + status_getFacilities);
            return status_getFacilities;
        }
    }

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param page_number   : what range to load
     * @param data          : Json format data to be process and show on screen
     * @return 0 execute as expected, 1 errer when reading json
     * @Pupose : to load the content from server our cached file to screen for user to view
     */
    private int loadToScreen(Object binding, int facility_type, int page_number, JSONObject data, boolean isReport) {
        try {
            int length = data.getInt("length");
            int start = (page_number - 1) * 5;
            int end = Math.min((page_number * 5), length);
            int counter = 0;
            JSONArray array = data.getJSONArray("result");

            for (int index = start; index < end; index++) {
                loadToFragment(binding, facility_type, array.getJSONArray(index), counter, isReport);
                counter++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
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
        File f = new File(applicationContext.getFilesDir().toString()+"/"+fileName);
        return f.exists() && !f.isDirectory();
    }

    /**
     * @param binding       : a subclass of databinding, used to find TextView, Ratingbar
     * @param facility_type : int representing the type of facility calling this function
     * @param facility_info : a json array that holds information about facilities
     * @param index         : a int index range from 0 to 5
     * @Pupose load information from JSONArray to texView
     */
    private void loadToFragment(Object binding, int facility_type, JSONArray facility_info, int index, boolean isReport) {
        switch (index) {
            case 0:
                load_facility1(binding, facility_type, facility_info, isReport);
                break;
            case 1:
                load_facility2(binding, facility_type, facility_info, isReport);
                break;
            case 2:
                load_facility3(binding, facility_type, facility_info, isReport);
                break;
            case 3:
                load_facility4(binding, facility_type, facility_info, isReport);
                break;
            case 4:
                load_facility5(binding, facility_type, facility_info, isReport);
                break;
        }
    }

    /**
     * @param applicationContext : Central interface to provide configuration for an application.
     * @param response           : response from server
     * @return 0 if cached successfully; 1, if File Already Exists; 2 if IOException.
     * @Pupose write json response from server to a file
     */
    public int writeToJson(Context applicationContext, JSONObject response, String fileName) {
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
    public String readFromJson(Context applicationContext, String fileName) {
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
            case report_user: //need ?
                facilityToFetch = "user";
                break;
            case report_facility:
                facilityToFetch = "facility";
                break;
            case report_comment:
                facilityToFetch = "comment";

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
    private void load_facility1(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility1 = null, dateTextView_facility1 = null, contentTextView_facility1 = null, facilityID_textView1_facility1 = null;
        RatingBar ratingBar_facility1 = null;
        ConstraintLayout constraintLayout_facility1 = null;

        if(isReport){
            titleTextView_facility1 = ((FragmentReportBinding) Binding).titleTextViewFacility1;
            dateTextView_facility1 = ((FragmentReportBinding) Binding).dateTextViewFacility1;
            contentTextView_facility1 = ((FragmentReportBinding) Binding).contentTextViewFacility1;
            ratingBar_facility1 = ((FragmentReportBinding) Binding).ratingBarFacility1;
            facilityID_textView1_facility1 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility1;
            constraintLayout_facility1 = ((FragmentReportBinding) Binding).facility1;
        }else {
            titleTextView_facility1 = ((FragmentFacilityBinding) Binding).titleTextViewFacility1;
            dateTextView_facility1 = ((FragmentFacilityBinding) Binding).dateTextViewFacility1;
            contentTextView_facility1 = ((FragmentFacilityBinding) Binding).contentTextViewFacility1;
            ratingBar_facility1 = ((FragmentFacilityBinding) Binding).ratingBarFacility1;
            facilityID_textView1_facility1 = ((FragmentFacilityBinding) Binding).facilityIDTextViewFacility1;
            constraintLayout_facility1 = ((FragmentFacilityBinding) Binding).facility1;
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
    private void load_facility2(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility2 = null, dateTextView_facility2 = null, contentTextView_facility2 = null, facilityID_textView1_facility2 = null;
        RatingBar ratingBar_facility2 = null;
        ConstraintLayout constraintLayout_facility2 = null;

        if(isReport){
            titleTextView_facility2 = ((FragmentReportBinding) Binding).titleTextViewFacility2;
            dateTextView_facility2 = ((FragmentReportBinding) Binding).dateTextViewFacility2;
            contentTextView_facility2 = ((FragmentReportBinding) Binding).contentTextViewFacility2;
            ratingBar_facility2 = ((FragmentReportBinding) Binding).ratingBarFacility2;
            facilityID_textView1_facility2 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility2;
            constraintLayout_facility2 = ((FragmentReportBinding) Binding).facility2;
        }else {
            titleTextView_facility2 = ((FragmentFacilityBinding) Binding).titleTextViewFacility2;
            dateTextView_facility2 = ((FragmentFacilityBinding) Binding).dateTextViewFacility2;
            contentTextView_facility2 = ((FragmentFacilityBinding) Binding).contentTextViewFacility2;
            ratingBar_facility2 = ((FragmentFacilityBinding) Binding).ratingBarFacility2;
            facilityID_textView1_facility2 = ((FragmentFacilityBinding) Binding).facilityIDTextViewFacility2;
            constraintLayout_facility2 = ((FragmentFacilityBinding) Binding).facility2;
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
    private void load_facility3(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility3 = null, dateTextView_facility3 = null, contentTextView_facility3 = null, facilityID_textView1_facility3 = null;
        RatingBar ratingBar_facility3 = null;
        ConstraintLayout constraintLayout_facility3 = null;

        if(isReport){
            titleTextView_facility3 = ((FragmentReportBinding) Binding).titleTextViewFacility3;
            dateTextView_facility3 = ((FragmentReportBinding) Binding).dateTextViewFacility3;
            contentTextView_facility3 = ((FragmentReportBinding) Binding).contentTextViewFacility3;
            ratingBar_facility3 = ((FragmentReportBinding) Binding).ratingBarFacility3;
            facilityID_textView1_facility3 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility3;
            constraintLayout_facility3 = ((FragmentReportBinding) Binding).facility3;
        }else {
            titleTextView_facility3 = ((FragmentFacilityBinding) Binding).titleTextViewFacility3;
            dateTextView_facility3 = ((FragmentFacilityBinding) Binding).dateTextViewFacility3;
            contentTextView_facility3 = ((FragmentFacilityBinding) Binding).contentTextViewFacility3;
            ratingBar_facility3 = ((FragmentFacilityBinding) Binding).ratingBarFacility3;
            facilityID_textView1_facility3 = ((FragmentFacilityBinding) Binding).facilityIDTextViewFacility3;
            constraintLayout_facility3 = ((FragmentFacilityBinding) Binding).facility3;
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
    private void load_facility4(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility4 = null, dateTextView_facility4 = null, contentTextView_facility4 = null, facilityID_textView1_facility4 = null;
        RatingBar ratingBar_facility4 = null;
        ConstraintLayout constraintLayout_facility4 = null;

        if(isReport){
            titleTextView_facility4 = ((FragmentReportBinding) Binding).titleTextViewFacility4;
            dateTextView_facility4 = ((FragmentReportBinding) Binding).dateTextViewFacility4;
            contentTextView_facility4 = ((FragmentReportBinding) Binding).contentTextViewFacility4;
            ratingBar_facility4 = ((FragmentReportBinding) Binding).ratingBarFacility4;
            facilityID_textView1_facility4 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility4;
            constraintLayout_facility4 = ((FragmentReportBinding) Binding).facility4;
        }else {
            titleTextView_facility4 = ((FragmentFacilityBinding) Binding).titleTextViewFacility4;
            dateTextView_facility4 = ((FragmentFacilityBinding) Binding).dateTextViewFacility4;
            contentTextView_facility4 = ((FragmentFacilityBinding) Binding).contentTextViewFacility4;
            ratingBar_facility4 = ((FragmentFacilityBinding) Binding).ratingBarFacility4;
            facilityID_textView1_facility4 = ((FragmentFacilityBinding) Binding).facilityIDTextViewFacility4;
            constraintLayout_facility4 = ((FragmentFacilityBinding) Binding).facility4;
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
    private void load_facility5(Object Binding, int facility_type, JSONArray result, boolean isReport) {
        TextView titleTextView_facility5 = null, dateTextView_facility5 = null, contentTextView_facility5 = null, facilityID_textView1_facility5 = null;
        RatingBar ratingBar_facility5 = null;
        ConstraintLayout constraintLayout_facility5 = null;

        if(isReport){
            titleTextView_facility5 = ((FragmentReportBinding) Binding).titleTextViewFacility5;
            dateTextView_facility5 = ((FragmentReportBinding) Binding).dateTextViewFacility5;
            contentTextView_facility5 = ((FragmentReportBinding) Binding).contentTextViewFacility5;
            ratingBar_facility5 = ((FragmentReportBinding) Binding).ratingBarFacility5;
            facilityID_textView1_facility5 = ((FragmentReportBinding) Binding).facilityIDTextViewFacility5;
            constraintLayout_facility5 = ((FragmentReportBinding) Binding).facility5;
        }else {
            titleTextView_facility5 = ((FragmentFacilityBinding) Binding).titleTextViewFacility5;
            dateTextView_facility5 = ((FragmentFacilityBinding) Binding).dateTextViewFacility5;
            contentTextView_facility5 = ((FragmentFacilityBinding) Binding).contentTextViewFacility5;
            ratingBar_facility5 = ((FragmentFacilityBinding) Binding).ratingBarFacility5;
            facilityID_textView1_facility5 = ((FragmentFacilityBinding) Binding).facilityIDTextViewFacility5;
            constraintLayout_facility5 = ((FragmentFacilityBinding) Binding).facility5;
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

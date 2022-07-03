package com.example.help_m5.ui.entertainments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.help_m5.DatabaseConnection;
import com.example.help_m5.databinding.FragmentEntertainmentsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EntertainmentsFragment extends Fragment {

    static final int posts = 0;
    static final int study = 1;
    static final int entertainments = 2;
    static final int restaurants = 3;

    static final int facility_type = entertainments;

    static final int normal_local_load = 0;
    static final int normal_server_load = 1;
    static final int reached_end = 2;
    static final int server_error = 3;
    static final int local_error = 4;

    final String TAG = "EntertainmentsFragment";


    static boolean onSearch = false;   //if this true means user is viewing search result

    private SearchView facilitySearchView;
    private DatabaseConnection DBconnection;
    private FragmentEntertainmentsBinding binding;
    private FloatingActionButton page_up;
    private FloatingActionButton page_down;
    private FloatingActionButton add_facility;
    private FloatingActionButton main;


    int search_page_number = 1;
    int newest_page_number = 1;
    boolean reached_end_newest = false;
    boolean reached_end_search = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EntertainmentsViewModel entertainmentsViewModel = new ViewModelProvider(this).get(EntertainmentsViewModel.class);

        binding = FragmentEntertainmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DBconnection = new DatabaseConnection();
        DBconnection.getFacilities(binding, facility_type,1, getContext(), false, "");

        facilitySearchView = binding.searchFacility;
        facilitySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG,"searching: "+query);
                onSearch = true;
                int result = DBconnection.getFacilities(binding, facility_type,1, getContext(), true, query);
                if (result == normal_local_load ){
                    Log.d(TAG, "Load data from local device");
                }else if (result == normal_server_load){
                    Log.d(TAG, "Load data from server");
                }else if(result == local_error){
                    Log.d(TAG, "ERROR Load data from local device");
                    Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                } else if (result == server_error){
                    Log.d(TAG, "ERROR Load data from server");
                    Toast.makeText(getContext(), "Error happened when connecting to server, please exist", Toast.LENGTH_SHORT).show();
                } else if (result == reached_end){
                    Log.d(TAG, "load finished");
                }
                search_page_number = 1;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d(TAG,"searching: "+newText);
                return false;
            }
        });

        ConstraintLayout shows1 = binding.facility1;
        shows1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"opening 1",Toast.LENGTH_SHORT).show();
                // TODO implet so it opens a new page
            }
        });

        page_up = binding.fabPrevious;
        page_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSearch){
                    if(search_page_number == 1 ){
                        Toast.makeText(getContext(), "You are already on first page", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    search_page_number -= 1;
                    int result = DBconnection.getFacilities(binding, facility_type, search_page_number, getContext(), true, "");
                    if(result == local_error){
                        search_page_number = 1;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "down page Error happened when loading data, please exist");
                    }else if (result == reached_end){
                        search_page_number = 1;
                        Log.d(TAG, "down page load all");
                    }
                } else {
                    if(newest_page_number == 1 ){
                        Toast.makeText(getContext(), "You are already on first page", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newest_page_number -= 1;
                    int result = DBconnection.getFacilities(binding, facility_type, newest_page_number, getContext(), false, "");
                    if(result == local_error){
                        newest_page_number = 1;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "down page Error happened when loading data, please exist");
                    }else if (result == reached_end){
                        newest_page_number = 1;
                        Log.d(TAG, "down page load all");
                    }
                }
            }
        });

        page_down = binding.fabNext;
        page_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSearch){
                   if(reached_end_search){
                       reached_end_search = false;
                       search_page_number = 1;
                   }else {
                       search_page_number++;
                   }
                    int result = DBconnection.getFacilities(binding, facility_type, search_page_number, getContext(), true, "");
                    if(result == local_error){
                        reached_end_search = true;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                    }else if (result == reached_end){
                        reached_end_search = true;
                    }
                    Log.d(TAG, "result is :"+result);
                    Log.d(TAG, "search_page_number is :"+search_page_number);
                    Log.d(TAG, "reached_end_search is :"+reached_end_search);

                } else {
                    if(reached_end_newest){
                        reached_end_newest = false;
                        newest_page_number = 1;
                    }else {
                        newest_page_number++;
                    }
                    int result = DBconnection.getFacilities(binding, facility_type, newest_page_number, getContext(), false, "");
                    if(result == local_error){
                        reached_end_newest = true;
                        Toast.makeText(getContext(), "Error happened when loading data, please exist", Toast.LENGTH_SHORT).show();
                    }else if (result == reached_end){
                        reached_end_newest = true;
                    }
                    Log.d(TAG, "result is :"+result);
                    Log.d(TAG, "search_page_number is :"+newest_page_number);
                    Log.d(TAG, "reached_end_search is :"+reached_end_newest);
                }
            }
        });

        add_facility = binding.fabAdd;
        add_facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement function to add new facility
            }
        });
        main = binding.fabMain;
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( page_down.getVisibility() == View.INVISIBLE){
                    page_up.setVisibility(View.VISIBLE);
                    page_down.setVisibility(View.VISIBLE);
                    add_facility.setVisibility(View.VISIBLE);
                }else{
                    page_up.setVisibility(View.INVISIBLE);
                    page_down.setVisibility(View.INVISIBLE);
                    add_facility.setVisibility(View.INVISIBLE);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
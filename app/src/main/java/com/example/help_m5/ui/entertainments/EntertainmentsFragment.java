package com.example.help_m5.ui.entertainments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.help_m5.DatabaseConnection;
import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentEntertainmentsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EntertainmentsFragment extends Fragment {
    final int posts = 0;
    final int study = 1;
    final int entertainments = 2;
    final int restaurants = 3;

    private DatabaseConnection DBconnection;
    private FragmentEntertainmentsBinding binding;
    private FloatingActionButton page_up;
    private FloatingActionButton page_down;
    private FloatingActionButton add_facility;
    private FloatingActionButton main;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EntertainmentsViewModel entertainmentsViewModel = new ViewModelProvider(this).get(EntertainmentsViewModel.class);

        binding = FragmentEntertainmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ConstraintLayout shows1 = binding.facility1;
        shows1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"opening 1",Toast.LENGTH_SHORT).show();
                shows1.setBackgroundColor(R.color.black);
            }
        });

        page_up = binding.fabPrevious;
        page_down = binding.fabNext;
        add_facility = binding.fabAdd;
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
        DBconnection = new DatabaseConnection();
        DBconnection.getFacilities(binding, entertainments,1,getContext(),binding);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
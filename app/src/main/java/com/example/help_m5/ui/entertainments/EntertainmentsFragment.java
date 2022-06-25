package com.example.help_m5.ui.entertainments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentEntertainmentsBinding;

public class EntertainmentsFragment extends Fragment {

    private FragmentEntertainmentsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EntertainmentsViewModel entertainmentsViewModel =
                new ViewModelProvider(this).get(EntertainmentsViewModel.class);

        binding = FragmentEntertainmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEntertainments;
        entertainmentsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
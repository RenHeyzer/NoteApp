package com.example.noteapp.onboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noteapp.R;
import com.example.noteapp.databinding.FragmentSecondBinding;
import com.example.noteapp.databinding.FragmentThirdBinding;
import com.example.noteapp.utils.PreferencesHelper;

public class ThirdFragment extends Fragment {
    FragmentThirdBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThirdBinding.inflate(getLayoutInflater(), container, false);
        setClickStart();
        return binding.getRoot();
    }

    private void setClickStart() {
        binding.txtStart.setOnClickListener(v -> {
            PreferencesHelper prefHelper  = new PreferencesHelper();
            prefHelper.unit(requireContext());
            prefHelper.onSaveOnBoardState();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_home);
        });


    }
}

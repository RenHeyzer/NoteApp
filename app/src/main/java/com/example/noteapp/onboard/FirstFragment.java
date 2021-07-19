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
import com.example.noteapp.databinding.FragmentFirstBinding;
import com.example.noteapp.utils.PreferencesHelper;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(getLayoutInflater(), container, false);
        setClickSkip1();
        return binding.getRoot();
    }

    private void setClickSkip1() {
        binding.txtSkip1.setOnClickListener(v -> {
            PreferencesHelper helper = new PreferencesHelper();
            helper.unit(requireContext());
            helper.onSaveOnBoardState();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_home);
        });
    }
}
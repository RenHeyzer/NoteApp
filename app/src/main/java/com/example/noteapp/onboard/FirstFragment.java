package com.example.noteapp.onboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClickSkip1();
    }

    private void setClickSkip1() {
        binding.txtSkip1.setOnClickListener(v -> {
            PreferencesHelper helper = new PreferencesHelper();
            helper.unit(requireContext());
            helper.onSaveOnBoardState();
            close();
        });
    }

    public void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigateUp();
    }
}
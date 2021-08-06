package com.example.noteapp.onboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noteapp.adapters.ViewPagerAdapter;
import com.example.noteapp.databinding.FragmentOnBoardBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnBoardFragment extends Fragment {

    FragmentOnBoardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnBoardBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().
                getSupportFragmentManager(), getLifecycle());
        binding.viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2,
                (tab, position) -> {}).attach();
    }
}


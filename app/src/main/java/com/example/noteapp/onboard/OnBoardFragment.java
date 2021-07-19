package com.example.noteapp.onboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noteapp.adapter.ViewPagerAdapter;
import com.example.noteapp.databinding.FragmentFirstBinding;
import com.example.noteapp.databinding.FragmentOnBoardBinding;
import com.example.noteapp.databinding.FragmentSecondBinding;
import com.example.noteapp.databinding.FragmentThirdBinding;
import com.example.noteapp.utils.PreferencesHelper;

public class OnBoardFragment extends Fragment {

    FragmentOnBoardBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        binding = FragmentOnBoardBinding.inflate(getLayoutInflater(), container, false);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager, true);
        return binding.getRoot();
    }
}
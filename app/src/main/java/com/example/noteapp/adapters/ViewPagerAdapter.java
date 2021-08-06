package com.example.noteapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.noteapp.onboard.FirstFragment;
import com.example.noteapp.onboard.OnBoardFragment;
import com.example.noteapp.onboard.SecondFragment;
import com.example.noteapp.onboard.ThirdFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

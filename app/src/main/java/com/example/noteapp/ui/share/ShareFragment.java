package com.example.noteapp.ui.share;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noteapp.R;
import com.example.noteapp.databinding.FragmentShareBinding;
import com.example.noteapp.databinding.FragmentSlideshowBinding;
import com.example.noteapp.ui.slideshow.SlideshowViewModel;


public class ShareFragment extends Fragment {
    private ShareViewModel shareViewModel;
    private FragmentShareBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shareViewModel =
                new ViewModelProvider(this).get(ShareViewModel.class);

        binding = FragmentShareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textShare;
        shareViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
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
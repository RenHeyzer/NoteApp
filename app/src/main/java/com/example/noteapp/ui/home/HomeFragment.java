package com.example.noteapp.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.L;
import com.example.noteapp.R;
import com.example.noteapp.databinding.NavHeaderMainBinding;
import com.example.noteapp.model.TaskModel;
import com.example.noteapp.adapter.TaskAdapter;
import com.example.noteapp.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TaskAdapter adapter = new TaskAdapter();
    private boolean isLinearLayout = true;

    private ArrayList<TaskModel> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setupRecycler();
        getData();
        initSearch();
        return binding.getRoot();
    }

    private void getData() {
        getParentFragmentManager().setFragmentResultListener("res", getViewLifecycleOwner(), (requestKey, result) -> {
            TaskModel taskModel = (TaskModel) result.getSerializable("done");
            list.add(taskModel);
            adapter.addText(taskModel);
        });
    }

    private void initSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<TaskModel> filteredList = new ArrayList<>();
        for (TaskModel item : list) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void setupRecycler() {
        binding.rvRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecycler.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            if (isLinearLayout) {
                item.setIcon(R.drawable.ic_baseline_format_list_bulleted_24);
                binding.rvRecycler.setLayoutManager(new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));
                binding.rvRecycler.setAdapter(adapter);
                isLinearLayout = false;
            } else {
                item.setIcon(R.drawable.ic_baseline_dashboard_24);
                binding.rvRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.rvRecycler.setAdapter(adapter);
                isLinearLayout = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
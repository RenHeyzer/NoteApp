package com.example.noteapp.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.noteapp.ItemClickListener;
import com.example.noteapp.R;
import com.example.noteapp.models.TaskModel;
import com.example.noteapp.adapters.TaskAdapter;
import com.example.noteapp.databinding.FragmentHomeBinding;
import com.example.noteapp.room.MyApp;

import java.util.ArrayList;
import java.util.List;

import static com.example.noteapp.ui.form.NoteFragment.BUNDLE_KEY;
import static com.example.noteapp.ui.form.NoteFragment.REQUEST_KEY;

public class HomeFragment extends Fragment implements ItemClickListener {

    private FragmentHomeBinding binding;
    private TaskAdapter adapter;
    private boolean isLinearLayout = true;
    private boolean isList = true;
    private int position;

    private List<TaskModel> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecycler();
        getNotesFromDB();
        initSearch();
        deleteData();
    }

    private void deleteData() {
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TaskModel mtaskModel = adapter.getWordAtPosition(position);
                MyApp.getInstance().taskDao().delete(mtaskModel);

            }
        });
        touchHelper.attachToRecyclerView(binding.rvRecycler);
    }

    private void getNotesFromDB() {
        MyApp.getInstance().taskDao().getAll().observe(requireActivity(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> taskModels) {
                adapter.setList(taskModels);
                list = taskModels;
            }
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
        adapter = new TaskAdapter(isList, HomeFragment.this);
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

    @Override
    public void onItemClick(int position, TaskModel taskModel) {
        this.position = position;
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY, taskModel);
        NavController navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.home_to_noteFragment, bundle);
    }
}
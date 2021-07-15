package com.example.noteapp.ui.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noteapp.R;
import com.example.noteapp.model.TaskModel;
import com.example.noteapp.databinding.FragmentNoteBinding;

public class NoteFragment extends Fragment {
    private FragmentNoteBinding binding;
    private TaskModel taskModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        initClickListener(navController);
        return binding.getRoot();
    }

    private void initClickListener(NavController navController) {
        binding.txtDone.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString();
            taskModel = new TaskModel(title);
            Bundle bundle = new Bundle();
            bundle.putSerializable("done", taskModel);
            getParentFragmentManager().setFragmentResult("res", bundle);
            navController.navigateUp();
        });
    }

}
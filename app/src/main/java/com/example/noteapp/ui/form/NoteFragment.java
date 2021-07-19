package com.example.noteapp.ui.form;

import android.content.Context;
import android.content.res.Configuration;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.noteapp.MainActivity;
import com.example.noteapp.R;
import com.example.noteapp.model.TaskModel;
import com.example.noteapp.databinding.FragmentNoteBinding;
import com.google.android.material.tabs.TabLayout;

public class NoteFragment extends Fragment {
    private FragmentNoteBinding binding;
    private TaskModel taskModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        } catch (Exception e) {
            Log.e(, "onCreateView", e);
            throw e;
        }
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        initClickListener(navController);
        setFocusForEditText();
        return binding.getRoot();
    }

    private void setFocusForEditText() {
        binding.etTitle.post(() -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    binding.etTitle.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
            binding.etTitle.requestFocus();
        });
    }

    private void initClickListener(NavController navController) {

        binding.txtDone.setOnClickListener(v -> {
            if (binding.etTitle.getText().toString().trim().equals("")) {
                binding.etTitle.setError("Введите текст");
            } else {
                String title = binding.etTitle.getText().toString();
                taskModel = new TaskModel(title);
                Bundle bundle = new Bundle();
                bundle.putSerializable("done", taskModel);
                getParentFragmentManager().setFragmentResult("res", bundle);
                navController.navigateUp();
            }
        });

        binding.ivArrowBack.setOnClickListener(v -> navController.navigate(R.id.nav_home));

    }
}
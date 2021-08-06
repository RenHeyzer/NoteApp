package com.example.noteapp.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.databinding.FragmentForgotPassBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassFragment extends Fragment {

    FragmentForgotPassBinding binding;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPassBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setForgotPassClickListener();
        setArrowBackListener();
    }

    private void setArrowBackListener() {
        binding.ivArrowBack.setOnClickListener(v -> {
            close();
        });

    }

    private void setForgotPassClickListener() {
        binding.btnSendEmail.setOnClickListener(v -> {
            String address = binding.tilEmail.getEditText().getText().toString().trim();
            if (!emailError()) {
                if (TextUtils.isEmpty(address)) {
                    binding.tilEmail.setError(getString(R.string.auth_input_email));
                } else {
                    binding.tilEmail.setError(null);
                }
            }
            sendPasswordResetEmail(address);
        });
    }

    private void sendPasswordResetEmail(String address) {
        auth.sendPasswordResetEmail(address)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), (R.string.email_send),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), (R.string.email_dont_send),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean emailError() {
        String address = binding.tilEmail.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            binding.tilEmail.setError(getString(R.string.auth_input_email));
            return false;
        } else {
            binding.tilEmail.setError(null);
            return true;
        }
    }

    public void close() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_content_main);
        navController.navigateUp();
    }
}
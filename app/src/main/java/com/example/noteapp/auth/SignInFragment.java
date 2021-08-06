package com.example.noteapp.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.databinding.FragmentSignInBinding;
import com.example.noteapp.interfaces.DrawerLocker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {

    FragmentSignInBinding binding;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInit();
        ((DrawerLocker)getActivity()).setDrawerLocked(true);
        setSignUpClickListener();
        setForgotPassClickListener();
    }

    private void setForgotPassClickListener() {
        binding.txtForgotPass.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(
                    requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.forgotPassFragment);
        });

    }

    private void setSignUpClickListener() {
        binding.txtSignIn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(
                    requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_signInFragment_to_authFragment);
        });
    }

    private void setInit() {
        binding.btnSignIn.setOnClickListener(v -> {
            String address = binding.tilEmail.getEditText().getText().toString().trim();
            String password = binding.tilPassword.getEditText().getText().toString().trim();
            if (!emailError() | !passwordError()) {
                if (TextUtils.isEmpty(address)) {
                    binding.tilEmail.setError(getString(R.string.auth_input_email));
                } else {
                    binding.tilEmail.setError(null);
                }
                if (TextUtils.isEmpty(password)) {
                    binding.tilPassword.setError(getString(R.string.auth_input_password));
                } else {
                    binding.tilPassword.setError(null);
                }
                return;
            }
            signInWithEmailAndPassword(address, password);
        });
    }

    private void signInWithEmailAndPassword(String address, String password) {
        auth.signInWithEmailAndPassword(address, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "singInWithEmailAndPassword:success");
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(requireActivity(), (R.string.login_success),
                                Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(
                                requireActivity(), R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.nav_home);
                    } else {
                        Log.w("TAG", "signInWithEmailAndPassword:failure",
                                task.getException());
                        Toast.makeText(requireActivity(), (R.string.login_failed),
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

    private boolean passwordError() {
        String password = binding.tilPassword.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError(getString(R.string.auth_input_password));
            return false;
        } else {
            binding.tilPassword.setError(null);
            return true;
        }
    }

    public void close() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_content_main);
        navController.navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((DrawerLocker) getActivity()).setDrawerLocked(false);
    }
}
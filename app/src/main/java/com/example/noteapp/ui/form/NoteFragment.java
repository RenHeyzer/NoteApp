package com.example.noteapp.ui.form;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.noteapp.R;

import com.example.noteapp.models.TaskModel;
import com.example.noteapp.databinding.FragmentNoteBinding;
import com.example.noteapp.room.MyApp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class NoteFragment extends Fragment {
    private static final int RECORD_AUDIO_REQUEST_CODE = 5;
    private FragmentNoteBinding binding;
    private TaskModel taskModel;
    private TaskModel model;
    public final static String REQUEST_KEY = "res";
    public final static String BUNDLE_KEY = "done";
    public final static String BUNDLE_UPDATE_KEY = "updateDone";
    private final Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClickListener();
        getDataForEdit();
        setFocusForEditText();
        checkPermissions();
        initSpeechRecognizer();
        setRecognitionListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setRecognitionListener() {
        SpeechRecognizer.createSpeechRecognizer(requireContext());
        SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                binding.fabVoice.setImageResource(R.drawable.ic_baseline_keyboard_voice_24);
                binding.etTitle.setHint(R.string.input_text);
            }

            @Override
            public void onError(int error) {
                binding.fabVoice.setImageResource(R.drawable.ic_baseline_mic_none_24);
                binding.etTitle.setHint(R.string.text_of_voice_error);
                recognizer.cancel();
            }

            @Override
            public void onResults(Bundle results) {
                binding.fabVoice.setImageResource(R.drawable.ic_baseline_keyboard_voice_24);
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                binding.etTitle.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        binding.fabVoice.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                binding.etTitle.setText("");
                binding.etTitle.setHint(R.string.listening);
                binding.fabVoice.setImageResource(R.drawable.ic_baseline_record_voice_over_24);
                recognizer.startListening(speechIntent);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                binding.fabVoice.setImageResource(R.drawable.ic_baseline_mic_none_24);
                recognizer.stopListening();
            }
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) ;
        }
    }

    public void initSpeechRecognizer() {
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }
    }

    private void getDataForEdit() {
        if (getArguments() != null) {
            taskModel = (TaskModel) getArguments().getSerializable(BUNDLE_KEY);
            if (taskModel != null) {
                binding.etTitle.setText(taskModel.getTitle());
            }
        }
    }

    private void setFocusForEditText() {
        binding.etTitle.post(() -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) requireActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    binding.etTitle.getApplicationWindowToken(),
                    InputMethodManager.SHOW_IMPLICIT, 0);
            binding.etTitle.requestFocus();
        });
    }

    private void initClickListener() {
        binding.txtDone.setOnClickListener(v -> {
            if (binding.etTitle.getText().toString().trim().equals("")) {
                binding.etTitle.setError(getString(R.string.text_of_error));
                binding.etTitle.getError().charAt(4);
                YoYo.with(Techniques.Shake)
                        .duration(1008)
                        .repeat(1)
                        .playOn(binding.txtDone);
                return;
            }
            String title = binding.etTitle.getText().toString();
            Bundle bundle = new Bundle();
            model = new TaskModel(title, "19.07.2021");
            if (taskModel == null) {
                MyApp.getInstance().taskDao().insertTask(model);
                bundle.putSerializable(BUNDLE_KEY, model);
            } else {
                taskModel.setTitle(title);
                MyApp.getInstance().taskDao().update(taskModel);
                bundle.putSerializable(BUNDLE_UPDATE_KEY, taskModel);
            }
            getParentFragmentManager().setFragmentResult(REQUEST_KEY, bundle);
            close();
        });
        binding.ivArrowBack.setOnClickListener(v -> {
            close();
            keyboardHide();
        });
    }

    private void keyboardHide() {
        InputMethodManager inputManager = (InputMethodManager)
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = requireActivity().getCurrentFocus();
        if (view == null) {
            view = new View(getActivity());
        }
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void close() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_content_main);
        navController.navigateUp();
    }
}
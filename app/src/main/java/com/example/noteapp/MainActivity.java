package com.example.noteapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.noteapp.auth.AuthFragment;
import com.example.noteapp.interfaces.DrawerLocker;
import com.example.noteapp.utils.PreferencesHelper;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noteapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements DrawerLocker {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ImageView imageView;
    private PreferencesHelper prefHelper = new PreferencesHelper();
    private SharedPreferences sharedPref;
    public final static int SELECT_PHOTO = 1;
    public final static String SET_IMAGE_KEY = "db";
    public DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_share, R.id.nav_gallery, R.id.nav_slideshow)
                //here may be mistakes
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            navController.navigate(R.id.authFragment);
        }
        prefHelper.unit(this);
        if (!prefHelper.isShown()) {
            navController.navigate(R.id.signInFragment);
            navController.navigate(R.id.authFragment);
            navController.navigate(R.id.on_board_fragment);
        }
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        binding.appBarMain.fab.setOnClickListener(view ->
                navController.navigate(R.id.home_to_noteFragment));
        setVisibilityGone(navController);
        destination(navController);
        getImage();
        setSaveImage();
    }


    private void setVisibilityGone(NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.note_fragment || destination.getId() == R.id.signInFragment ||
                    destination.getId() == R.id.authFragment || destination.getId() == R.id.authFragment
                    || destination.getId() == R.id.on_board_fragment
                    || destination.getId() == R.id.forgotPassFragment) {
                binding.appBarMain.toolbar.setVisibility(View.GONE);
            } else {
                binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(uri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    sharedPref.edit().putString(SET_IMAGE_KEY, encodedImage).commit();
                    imageView.setImageURI(uri);
                    prefHelper.onSaveImage();
                    Glide.with(this)
                            .load(uri)
                            .circleCrop()
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

    private void getImage() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });
        imageView.setOnLongClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(imageView.getContext()).create();
            alertDialog.setTitle(getString(R.string.text_of_room_dialog));
            alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Нет", (dialog, which) -> {

            });
            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "Да", (dialog, which) -> {
                prefHelper.onSavePlaceHolder();
                Glide.with(MainActivity.this)
                        .load(mGetContent)
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(imageView);
            });
            alertDialog.show();
            return false;
        });
    }

    private void setSaveImage() {
        sharedPref = getSharedPreferences("profileImage", MODE_PRIVATE);
        if (prefHelper.isShownImage()) {
            if (!sharedPref.getString(SET_IMAGE_KEY, "").equals("")) {
                byte[] decodedString = Base64.decode(sharedPref.getString(SET_IMAGE_KEY, ""), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);
            }
        }
    }

    private void destination(NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (controller.getGraph().getStartDestination() == destination.getId()) {
                binding.appBarMain.fab.show();
            } else {
                binding.appBarMain.fab.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void setDrawerLocked(boolean shouldLock) {
        if (shouldLock) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }
}
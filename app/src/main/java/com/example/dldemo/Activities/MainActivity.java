package com.example.dldemo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dldemo.R;
import com.example.dldemo.adapters.ListAdapter;
import com.example.dldemo.model.LangModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListAdapter.FinaListClickListener,NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public BottomAppBar mBottomAppBar;
    public DrawerLayout mDrawerLayout;
    public FloatingActionButton fab;
    public Toolbar mToolbar;
    public NavOptions.Builder leftToRightBuilder, rightToLeftBuilder;
    private NavigationView mNavigationView;
    FirebaseAuth mAuth;


    @Override
    protected void onResume() {
        super.onResume();

        ImageView profileImage = mNavigationView.getHeaderView(0).findViewById(R.id.navigationDrawerProfileImage);
        ImageView verifiedImage = mNavigationView.getHeaderView(0).findViewById(R.id.navigationDrawerVerifiedImage);
        TextView profileFullName = mNavigationView.getHeaderView(0).findViewById(R.id.navigationDrawerProfileName);
        TextView profileEmail = mNavigationView.getHeaderView(0).findViewById(R.id.navigationDrawerEmail);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                profileFullName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }

            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
                Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                        .error(R.drawable.image_error_icon)
                        .placeholder(R.drawable.user_icon)
                        .into(profileImage);
            } else {



                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Get the Firebase storage reference for the profile image
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("images").child(uid).child("profile.jpg");

// Get the profileImageUrl from the Firebase Realtime Database
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                            if (profileImageUrl != null) {
                                // Load the profile image into the ImageView using Picasso
                                Picasso.get().load(profileImageUrl).into(profileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });
            }

            profileEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                verifiedImage.setImageResource(R.drawable.verified_icon);
            } else {
                verifiedImage.setImageResource(R.drawable.not_verified_icon);
            }

            profileEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Email verification sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<LangModel> langModelList = null;
        try {
            langModelList = getFinaData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRecyclerView(langModelList);

        mAuth = FirebaseAuth.getInstance();

        //setting hooks

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);


        mNavigationView.setCheckedItem(R.id.nav_home);

        mNavigationView.setNavigationItemSelectedListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    Log.d(TAG, "onClick: closing drawer");
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    Log.d(TAG, "onClick: opening drawer");
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        Log.d(TAG, "onNavigationItemSelected: animations for opening fragment to right of current one");
        leftToRightBuilder = new NavOptions.Builder();
        leftToRightBuilder.setEnterAnim(R.anim.slide_in_right);
        leftToRightBuilder.setExitAnim(R.anim.slide_out_left);
        leftToRightBuilder.setPopEnterAnim(R.anim.slide_in_left);
        leftToRightBuilder.setPopExitAnim(R.anim.slide_out_right);
        leftToRightBuilder.setLaunchSingleTop(true);

        Log.d(TAG, "onNavigationItemSelected: animations for opening fragment to left of current one");
        rightToLeftBuilder = new NavOptions.Builder();
        rightToLeftBuilder.setEnterAnim(R.anim.slide_in_left);
        rightToLeftBuilder.setExitAnim(R.anim.slide_out_right);
        rightToLeftBuilder.setPopEnterAnim(R.anim.slide_in_right);
        rightToLeftBuilder.setPopExitAnim(R.anim.slide_out_left);
        rightToLeftBuilder.setLaunchSingleTop(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("EXTRA_REMINDER_ITEM");
        if (bundle != null) {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finishAffinity();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else {
                            Toast.makeText(MainActivity.this, "Could not sign out. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    private void initRecyclerView(List<LangModel> langModelList) {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter adapter = new ListAdapter(langModelList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<LangModel> getFinaData() throws Exception {
        InputStream is = getResources().openRawResource(R.raw.langages);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        int n;
        while(( n = reader.read(buffer)) != -1) {
            writer.write(buffer, 0,n);
        }

        String jsonStr = writer.toString();
        Gson gson = new Gson();
        LangModel[] langModels =  gson.fromJson(jsonStr, LangModel[].class);
        return Arrays.asList(langModels);
    }
    @Override
    public void onItemClick(LangModel langModel) {
        Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
        intent.putExtra("LangModel", langModel);
        intent.putExtra("Language", langModel.getName());
        startActivity(intent);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: starts");
        int itemId = item.getItemId();

        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            Log.d(TAG, "onNavigationItemSelected: closing drawer");
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        switch (itemId) {
            case R.id.nav_home:
                Log.d(TAG, "onNavigationItemSelected: home selected");
                return true;

            case R.id.nav_profile:
                Log.d(TAG, "onNavigationItemSelected: profile selected");
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            case R.id.nav_dashb:
                Log.d(TAG, "onNavigationItemSelected: dashboard selected");
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                return true;

            case R.id.nav_logout:
                Log.d(TAG, "onNavigationItemSelected: logging out");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.logout_dialog_box, null);
                Button logoutYesButton = view.findViewById(R.id.logout_yes);
                Button logoutNoButton = view.findViewById(R.id.logout_no);
                builder.setView(view);

                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(true);

                logoutYesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        alertDialog.dismiss();
                    }
                });

                logoutNoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                return true;


            case R.id.nav_feedback:

                startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                return true;

            case R.id.nav_report_bug:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"rabiiyahya1@gmail.com"}); // Replace with your email address
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report");
                intent.putExtra(Intent.EXTRA_TEXT, "Please describe the bug you encountered:");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
                return true;

            case R.id.nav_about_us:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                return true;

            default:
                Toast.makeText(this, "This feature is not yet available", Toast.LENGTH_SHORT).show();
                return false;
        }
    }




    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Could not sign out. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back pressed");
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            Log.d(TAG, "onBackPressed: closing drawer");
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {
            Log.d(TAG, "onBackPressed: navigating to home");
            super.onBackPressed();            finish();

        }
    }



}
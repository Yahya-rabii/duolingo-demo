package com.example.dldemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private final String REMEMBER_ME_ENABLED = "rememberMeEnabled";
    private final String REMEMBER_ME_EMAIL = "rememberMeEmail";
    private final String REMEMBER_ME_PASSWORD = "rememberMePassword";

    private SharedPreferences.Editor mEditor;

    private TextInputLayout emailTextInput, passwordTextInput;
    private TextInputEditText emailEditText, passwordEditText;
    private CheckBox rememberMe;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;

    /**
     * Handles the result of the Google sign-in Intent.
     *
     * @param requestCode The request code passed to startActivityForResult().
     * @param resultCode The result code returned by the child Activity.
     * @param data The Intent that was returned by the child Activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the Google sign-in Intent.
        if (requestCode == RC_SIGN_IN) {
            // Show a progress bar to indicate that the login process is in progress.
            findViewById(R.id.loginScreenProgressBar).setVisibility(View.VISIBLE);

            // Get the result of the Google sign-in Intent.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // If sign-in was successful, authenticate with Firebase.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
                // If sign-in failed, hide the progress bar and show an error message.
                findViewById(R.id.loginScreenProgressBar).setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Login unsuccessful, Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onCreate: Checking internet connection");
        checkConnection(LoginActivity.this);
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: starts");

        setContentView(R.layout.activity_login);






        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //todo System.out.println(" dsqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq "+gso.getAccount().name);
        Log.d(TAG, "onCreate: Checking internet connection");
        checkConnection(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d(TAG, "onCreate: User already logged in");

            findViewById(R.id.loginScreenProgressBar).setVisibility(View.VISIBLE);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finishAffinity();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("SESSIONS", MODE_PRIVATE);
        mEditor = sharedPreferences.edit();

        //Setting hooks
        emailTextInput = findViewById(R.id.loginScreenEmailTextInput);
        emailEditText = findViewById(R.id.loginScreenEmailEditText);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordTextInput = findViewById(R.id.loginScreenPasswordTextInput);
        passwordEditText = findViewById(R.id.loginScreenPasswordEditText);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button loginButton = findViewById(R.id.loginScreenLoginButton);
        Button googleLoginButton = findViewById(R.id.loginScreenGoogleLoginButton);
        Button forgotPasswordButton = findViewById(R.id.loginScreenForgotPasswordButton);

        rememberMe = findViewById(R.id.loginScreenRememberMeCheckBox);

        if (sharedPreferences.getBoolean(REMEMBER_ME_ENABLED, false)) {
            emailEditText.setText(sharedPreferences.getString(REMEMBER_ME_EMAIL, ""));
            passwordEditText.setText(sharedPreferences.getString(REMEMBER_ME_PASSWORD, ""));
            rememberMe.setChecked(true);
        }

        findViewById(R.id.loginScreenCreateAccountButton).setOnClickListener(v -> {
            Log.d(TAG, "onClick: creating shared animation on create account button clicked");
            Pair[] pairs = new Pair[6];
            pairs[0] = new Pair<>(findViewById(R.id.loginScreenWelcomeBack), "welcome_transition");
            pairs[1] = new Pair<>(findViewById(R.id.loginScreenCreateAccountButton), "create_account_transition");
            pairs[2] = new Pair<>(findViewById(R.id.loginScreenLoginButton), "login_transition");
            pairs[3] = new Pair<>(findViewById(R.id.loginScreenEmailTextInput), "email_transition");
            pairs[4] = new Pair<>(findViewById(R.id.loginScreenPasswordTextInput), "password_transition");
            pairs[5] = new Pair<>(findViewById(R.id.loginScreenBackgroundRectangle), "bgrect_transition");

            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

            startActivity(new Intent(LoginActivity.this, RegisterActivity.class), activityOptions.toBundle());
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: login button clicked");

            if (validateFields()) {
                login();
            }
        });

        googleLoginButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: google login button clicked");

            googleLogin();
        });

        forgotPasswordButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: forget password clicked");
            if (validateEmail(Objects.requireNonNull(emailEditText.getText()).toString()) != null) {
                Log.d(TAG, "onClick: invalid email address entered");
                Toast.makeText(LoginActivity.this, "Put a valid email associated with an account to change the password", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailEditText.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: email sent to user for password reset");
                                Toast.makeText(LoginActivity.this, "A password reset email has been sent to your email address", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "onComplete: email for password reset cannot be sent");
                                Toast.makeText(LoginActivity.this, "Couldn't send a password reset email", Toast.LENGTH_SHORT).show();
                                Objects.requireNonNull(task.getException()).printStackTrace();
                            }
                        });
            }
        });

        Log.d(TAG, "onCreate: ends");
    }

    private void checkConnection(Activity activity) {
        Log.d(TAG, "isConnected: internet check");

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);

        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if (networkCapabilities == null || !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            Log.d(TAG, "onCreate: Internet not connected");
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Please connect to the internet to proceed further")
                    .setCancelable(false)
                    .setPositiveButton("Connect", (dialog, which) -> {
                        Log.d(TAG, "onClick: opening settings for internet");
                        activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: closing app");
                            activity.finishAffinity();
                        }
                    });
            builder.show();
        } else {
            Log.d(TAG, "isConnected: internet connected");
        }
    }

    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // handle successful sign-in
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                } else {
                    Log.d(TAG, "googleSignInLauncher: Login unsuccessful");
                    Toast.makeText(this, "Login unsuccessful", Toast.LENGTH_LONG).show();
                }
            });

    private void googleLogin() {
        Log.d(TAG, "googleLogin: starts");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
        Log.d(TAG, "googleLogin: ends");
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
            // ...
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d(TAG, "firebaseAuthWithGoogle: starts");

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential: success");
                        storeUserDetails();
                    } else {
                        handleFirebaseAuthError(task);
                    }
                });
    }

    private void handleFirebaseAuthError(Task<AuthResult> task) {
        try {
            throw task.getException();
        } catch (FirebaseAuthInvalidUserException e) {
            Log.e(TAG, "handleFirebaseAuthError: invalid user", e);
            // ...
        } catch (FirebaseAuthInvalidCredentialsException e) {
            Log.e(TAG, "handleFirebaseAuthError: invalid credentials", e);
            // ...
        } catch (FirebaseAuthUserCollisionException e) {
            Log.e(TAG, "handleFirebaseAuthError: user already exists", e);
            // ...
        } catch (Exception e) {
            Log.e(TAG, "handleFirebaseAuthError: other error", e);
            // ...
        }
    }

    private void storeUserDetails() {
        Log.d(TAG, "storeUserDetails: starts");

        final GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

        if (signInAccount != null) {

            final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            final User user = new User(signInAccount.getDisplayName(), signInAccount.getEmail()  , false);

            UserProfileChangeRequest.Builder profileUpdates = new UserProfileChangeRequest.Builder();
            profileUpdates.setDisplayName(signInAccount.getDisplayName());

            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates.build())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        } else {
                            Log.d(TAG, "onComplete: Could not update display name");
                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("usernames").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.d(TAG, "onDataChange: account already exists");
                        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                        }
                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User tempUser = snapshot.getValue(User.class);
                                if (tempUser != null) {
                                    tempUser.setEmail(signInAccount.getEmail());
                                    tempUser.setFullName(signInAccount.getDisplayName());
                                    tempUser.setEmailVerified(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified());
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(tempUser);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finishAffinity();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Log.d(TAG, "onDataChange: account does not exist");
                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(Objects.requireNonNull(signInAccount.getEmail()))
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: email " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

                                        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                        }

                                        //Make users database
                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user);

                                        //Make usernames database
                                        FirebaseDatabase.getInstance().getReference().child("usernames").child(uid).setValue(user.getUsername());

                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finishAffinity();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else if (Objects.equals(Objects.requireNonNull(task.getException()).getMessage(), "The email address is already in use by another account.")) {
                                        Log.d(TAG, "onComplete: email already in use");

                                        findViewById(R.id.loginScreenProgressBar).setVisibility(View.INVISIBLE);

                                        Toast.makeText(LoginActivity.this, "Email already in use by other account. Cannot sign in", Toast.LENGTH_LONG).show();

                                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                .requestIdToken(getString(R.string.default_web_client_id))
                                                .requestEmail()
                                                .build();

                                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                                        googleSignInClient.signOut();

                                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                                .addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: account deleted successfully"))
                                                .addOnFailureListener(e -> Log.d(TAG, "onFailure: account could not be deleted"));
                                    } else {
                                        findViewById(R.id.loginScreenProgressBar).setVisibility(View.INVISIBLE);
                                        task.getException().printStackTrace();
                                    }
                                });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        Log.d(TAG, "storeUserDetails: ends");
    }

    private void login() {
        Log.d(TAG, "login: starts");
        findViewById(R.id.loginScreenProgressBar).setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(Objects.requireNonNull(emailEditText.getText()).toString(), Objects.requireNonNull(passwordEditText.getText()).toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: login successful");
                        if (rememberMe.isChecked()) {
                            mEditor.putBoolean(REMEMBER_ME_ENABLED, true);
                            mEditor.putString(REMEMBER_ME_EMAIL, emailEditText.getText().toString());
                            mEditor.putString(REMEMBER_ME_PASSWORD, passwordEditText.getText().toString());
                            mEditor.apply();
                        } else {
                            mEditor.putBoolean(REMEMBER_ME_ENABLED, false);
                            mEditor.apply();
                        }
                        updateDetails();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finishAffinity();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        findViewById(R.id.loginScreenProgressBar).setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        task.getException().printStackTrace();
                    }
                });
    }

    private void updateDetails() {
        Log.d(TAG, "updateDetails: starts");

        Log.d(TAG, "updateDetails: updating email");
        FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: starts");
        super.onPause();

        if (emailTextInput.isErrorEnabled()) {
            emailTextInput.setError(null);
        }

        if (passwordTextInput.isErrorEnabled()) {
            passwordTextInput.setError(null);
        }
    }

    private boolean validateFields() {
        Log.d(TAG, "validateFields: starts");
        String emailValidation = validateEmail(Objects.requireNonNull(emailEditText.getText()).toString());
        String passwordValidation = validatePassword(Objects.requireNonNull(passwordEditText.getText()).toString());

        if (emailValidation == null && passwordValidation == null) {
            Log.d(TAG, "validateFields() returned: " + true);
            return true;
        }

        if (emailValidation != null) {
            emailTextInput.setError(emailValidation);
        }

        if (passwordValidation != null) {
            passwordTextInput.setError(passwordValidation);
        }

        Log.d(TAG, "validateFields() returned: " + false);
        return false;
    }

    private String validateEmail(String email) {
        if (email.trim().length() == 0) {
            return "Email cannot be empty";
        }
        return null;
    }

    private String validatePassword(String password) {
        if (password.trim().length() == 0) {
            return "Password cannot be empty";
        }
        return null;
    }
}
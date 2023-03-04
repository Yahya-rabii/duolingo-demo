package com.example.dldemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;



public class LoginActivity extends AppCompatActivity {
    /**
     * Firebase authentication.
     */
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    /* View Elements */
    private EditText inputEmail, inputPassword;
    Button sendVerifyMailAgainButton;
    TextView errorView;
    TextView tvClickHere;
    public FirebaseAuth mAuth;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Get View Elements by its id */
        inputEmail = findViewById(R.id.signInEmailTextInput);
        inputPassword = findViewById(R.id.signInPasswordTextInput);
        Button btnLogin = findViewById(R.id.signInButton);
        TextView btnRegister = findViewById(R.id.tvRegisterHere);
        sendVerifyMailAgainButton = findViewById(R.id.verifyEmailAgainButton);
        errorView = findViewById(R.id.signInErrorView);
        tvClickHere = findViewById(R.id.tvClickHere);

        sendVerifyMailAgainButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        tvClickHere.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));


        /* Event handler when Login Button is clicked */
        btnLogin.setOnClickListener(view -> {
            /* Input validation */
            try {
                String email = getTextFromInput(inputEmail);
                String password = getTextFromInput(inputPassword);

                /* start authentication. */
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    try {




                        if (inputEmail.getText().toString().contentEquals("")) {


                            errorView.setText("Email cant be empty");


                        } else if (inputPassword.getText().toString().contentEquals("")) {

                            errorView.setText("Password cant be empty");

                        } else {


                            mAuth.signInWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString());
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    if (user.isEmailVerified()) {


                                        System.out.println("Email Verified : " + user.isEmailVerified());
                                        Intent HomeActivity = new Intent(LoginActivity.this, MainActivity.class);
                                        setResult(RESULT_OK, null);
                                        startActivity(HomeActivity);
                                        LoginActivity.this.finish();


                                    } else {

                                        sendVerifyMailAgainButton.setVisibility(View.VISIBLE);
                                        errorView.setText("Please Verify your EmailID and SignIn");

                                    }
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                if (task.getException() != null) {
                                    errorView.setText(task.getException().getMessage());
                                }

                            }




                        }



                        if (!task.isSuccessful())
                            throw Objects.requireNonNull(task.getException());

                        /* If sign in success, verify the email. */
                        user = task.getResult().getUser();

                        if (user == null)
                            recreate();
                        if (user.isEmailVerified())
                            goToMainActivity();
                        else {
                            auth.signOut();
                            Toast.makeText(this, "Please verify your email first, then try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        /* If sign in failed, show alert message. */
                        Toast.makeText(this, "Login failed! Wrong Email or Password.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            } catch (Exception e) {
                Log.w("InvalidInput", "Input is invalid", e);
            }
        });

        /* Event handler when Login Button is clicked */
        btnRegister.setOnClickListener(view -> {
            /* Go to register activity. */
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        /* Check if user is authorized (non-null). */
        try {
            if (user == null)
                throw new FirebaseAuthInvalidUserException("404", "User not found.");
            if (!user.isEmailVerified()) {
                auth.signOut();
                throw new FirebaseAuthEmailException("404", "Email is not verified.");
            }
            goToMainActivity();
        } catch (FirebaseAuthEmailException e) {
            Toast.makeText(this, "Please verify your email first then try again.", Toast.LENGTH_LONG)
                    .show();
        } catch (Exception e) {
            Log.w("login", "User not found", e);
        }
    }


    /**
     * Retreive data from input, and validate the requirement.
     *
     * @param input The input element.
     * @return The input value.
     * @throws NullPointerException If validation failed.
     */
    private String getTextFromInput(EditText input) {
        String value = input.getText().toString();

        if (value.isEmpty()) {
            input.setError("This input is required");
            throw new NullPointerException("Field " + input.getHint() + " is required.");
        }

        return value;
    }


    /**
     * Go to main activity.
     */
    private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();    // Stop current activity.
    }
}

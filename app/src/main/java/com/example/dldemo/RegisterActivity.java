package com.example.dldemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "SignUpActivity";
    public FirebaseAuth mAuth;
    Button signUpButton;
    EditText signUpEmailTextInput;
    EditText signUpPasswordTextInput;
    CheckBox agreementCheckBox;
    TextView errorView;
    TextView tvLoginHere;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        signUpEmailTextInput = findViewById(R.id.signUpEmailTextInput);
        signUpPasswordTextInput = findViewById(R.id.signUpPasswordTextInput);
        signUpButton = findViewById(R.id.signUpButton);
        agreementCheckBox = findViewById(R.id.agreementCheckbox);
        errorView = findViewById(R.id.signUpErrorView);
        tvLoginHere = findViewById(R.id.tvLoginHere);

        tvLoginHere.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        signUpButton.setOnClickListener(view -> {

            if (signUpEmailTextInput.getText().toString().contentEquals("")) {


                errorView.setText("Email cannot be empty");


            } else if (signUpPasswordTextInput.getText().toString().contentEquals("")) {


                errorView.setText("Password cannot be empty");


            } else if (!agreementCheckBox.isChecked()) {

                errorView.setText("Please agree to terms and Condition");


            } else {


                mAuth.createUserWithEmailAndPassword(signUpEmailTextInput.getText().toString(), signUpPasswordTextInput.getText().toString()).addOnCompleteListener(RegisterActivity.this, task -> {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        try {
                            if (user != null)
                                user.sendEmailVerification()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        RegisterActivity.this);

                                                // set title
                                                alertDialogBuilder.setTitle("Please Verify Your EmailID");
                                                Intent HomeActivity = new Intent(RegisterActivity.this, LoginActivity.class);

                                                // set dialog message
                                                alertDialogBuilder
                                                        .setMessage("A verification Email Is Sent To Your Registered EmailID, please click on the link and Sign in again!")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Sign In", (dialog, id) -> startActivity(HomeActivity));


                                                // RegisterActivity.this.finish();


                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();


                                            }
                                        });

                        } catch (Exception e) {
                            errorView.setText(e.getMessage());
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                        if (task.getException() != null) {
                            errorView.setText(task.getException().getMessage());
                        }

                    }

                });

            }

        });



    }
}

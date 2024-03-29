package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegistrationActivity extends AppCompatActivity {

    // UI variables
    private EditText etEmail, etPassword, etConfirmedPassword;
    private Button btnCreateAccount;
    ProgressDialog progressDialog;

    // Firebase variables
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // initialize UI variables
        etEmail = findViewById(R.id.email_input);
        etPassword = findViewById(R.id.password_input);
        etConfirmedPassword = findViewById(R.id.confirmPassword_input);
        btnCreateAccount = findViewById(R.id.createAccount_button);
        progressDialog = new ProgressDialog(this);

        // initialize Firebase variables
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }


    // create new user account
    public void CreateUserAccount(View view) {
        if(validateInputs()){
            updateProgress();

            String email = getTextAsString(etEmail);
            String password = getTextAsString(etPassword);
            mAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this,
                                        "New Account Created Successfully", Toast.LENGTH_SHORT).show();
                                sendUserToDestinationActivity();
                            } else {
                                Toast.makeText(RegistrationActivity.this,
                                        "Oops... " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    // validate all user EditText inputs
    private boolean validateInputs() {
        if(!isValidEmailAddress() || !isValidPassword() || !matchingPasswords()){
            return false;
        } else {
            return true;
        }
    }


    // validate email address
    private boolean isValidEmailAddress() {
        if (isEmptyInput(etEmail)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    // validate password
    private boolean isValidPassword() {
        if (isEmptyInput(etPassword)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        } else if (!isValidPasswordLength(etPassword)) {
            etPassword.setError("Password must be between 6 and 16 characters long");
            etPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    // validate confirmed password
    private boolean matchingPasswords() {
        if (isEmptyInput(etConfirmedPassword)) {
            etConfirmedPassword.setError("Password is required");
            etConfirmedPassword.requestFocus();
            return false;
        } else if (!isValidPasswordLength(etConfirmedPassword)) {
            etConfirmedPassword.setError("Password must be between 6 and 16 characters");
            etConfirmedPassword.requestFocus();
            return false;
        } else if(!etConfirmedPassword.getText().toString().trim().
                equals(etPassword.getText().toString().trim())) {
            etConfirmedPassword.setError("Passwords do not match");
            etConfirmedPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    // check if EditText is empty
    private boolean isEmptyInput(EditText input) {
        return TextUtils.isEmpty(input.getText().toString().trim());
    }


    // check password input length is 6 - 16 characters
    private boolean isValidPasswordLength(EditText input) {
        return (input.getText().toString().trim().length() >= 6 &&
                input.getText().toString().trim().length() <= 16);
    }


    // update user that their account is being created
    private void updateProgress(){
        progressDialog.setMessage("Creating new account...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    // get text from EditText and return as a String
    private String getTextAsString(EditText input) {
        return input.getText().toString().trim();
    }



    // send user to specified activity (after successful creation of new account)
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // THIS METHOD NEEDS TO BE ALTERED TO SEND USER TO WHICHEVER ACTIVITY THEY SHOULD BE SENT TO!!!!
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void sendUserToDestinationActivity() {
        Intent intent = new Intent(RegistrationActivity.this, DestinationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
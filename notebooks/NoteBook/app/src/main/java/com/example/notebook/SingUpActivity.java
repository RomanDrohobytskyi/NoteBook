package com.example.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressBar progressBar;
    private EditText editTextEmail, editTextPassword, editTextPasswordConfirm;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        editTextEmail = findViewById(R.id.emailEditTextSign);
        editTextPassword = findViewById(R.id.passwordEditTextSign);
        editTextPasswordConfirm = findViewById(R.id.passwordSecondEditTextSign);

        progressBar = findViewById(R.id.progressbarSign);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.singUpBTN).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.singUpBTN:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();

        //Check wrong user data
        if (!checkUserData(email, password, passwordConfirm)){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User successful registered!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(SingUpActivity.this, MainActivity.class));
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean checkUserData(String email, String password, String passwordConfirm){
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return false;
        }

        if (password.isEmpty() ^ passwordConfirm.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6!");
            editTextPassword.requestFocus();
            return false;
        }

        if (!password.equals(passwordConfirm)){
            editTextPassword.setError("Passwords are different!");
            editTextPassword.requestFocus();
            return false;
        }

        if (!isPasswordHasNumber(password)){
            editTextPassword.setError("Password has not numbers!");
            editTextPassword.requestFocus();
            return false;
        }

        if (!isPasswordHasDigit(password)){
            editTextPassword.setError("Password has not symbols!");
            editTextPassword.requestFocus();
            return false;
        }

        if (!isUpperCaseLiter(password)){
            editTextPassword.setError("Password has not upper liters!");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isPasswordHasNumber(String password){
        return password.matches(".*[0-9].*");
    }

    private boolean isPasswordHasDigit(String password){
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(password);
        return m.find();
    }

    private boolean isUpperCaseLiter(String password){
        Pattern p = Pattern.compile("[^A-Z]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        return m.find();
    }

}

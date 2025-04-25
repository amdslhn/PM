package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextInputEditText userName, password;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi FirebaseAuth dan Views
        mAuth = FirebaseAuth.getInstance();
        initializeViews();

        // Tombol login
        loginButton.setOnClickListener(view -> loginUser());

        // Ke halaman register
        registerText.setOnClickListener(view -> navigateToRegister());

        // Tombol lupa password
        forgotPasswordText.setOnClickListener(view -> showForgotPasswordMessage());
    }

    private void initializeViews() {
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        forgotPasswordText = findViewById(R.id.forgotPassword);
        registerText = findViewById(R.id.logNow);
    }

    private void loginUser() {
        String email = userName.getText() != null ? userName.getText().toString().trim() : "";
        String passwordUser = password.getText() != null ? password.getText().toString().trim() : "";

        if (email.isEmpty()) {
            userName.setError("Email tidak boleh kosong!");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userName.setError("Format email tidak valid!");
            return;
        }

        if (passwordUser.isEmpty()) {
            password.setError("Password tidak boleh kosong!");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, passwordUser)
                .addOnSuccessListener(authResult -> onLoginSuccess())
                .addOnFailureListener(e -> onLoginFailure(e));
    }

    private void onLoginSuccess() {
        Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Login.this, Home.class));
        finish();
    }

    private void onLoginFailure(Exception e) {
        Toast.makeText(Login.this, "Login Gagal: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void navigateToRegister() {
        startActivity(new Intent(Login.this, Register.class));
    }

    private void showForgotPasswordMessage() {
        Toast.makeText(Login.this, "Fitur lupa password belum diimplementasikan", Toast.LENGTH_SHORT).show();
    }
}

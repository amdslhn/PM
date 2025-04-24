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

    TextInputEditText userName, password;
    CheckBox remember;
    Button login;
    TextView forgotPassword, logNow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi view
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.rememberMe);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);
        logNow = findViewById(R.id.logNow);

        // Tombol login
        login.setOnClickListener(view -> {
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
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Home.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Login.this, "Login Gagal: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        // Ke halaman register
        logNow.setOnClickListener(view -> {
            startActivity(new Intent(Login.this, Register.class));
        });

        // Tombol lupa password (bisa dikembangkan)
        forgotPassword.setOnClickListener(view -> {
            Toast.makeText(Login.this, "Fitur lupa password belum diimplementasikan", Toast.LENGTH_SHORT).show();
        });
    }
}

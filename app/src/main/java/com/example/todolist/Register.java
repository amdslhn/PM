package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";

    Button signUpButton;
    TextInputEditText usernameSignUp, passwordSignUp, nimPengguna, emailPengguna;
    FirebaseAuth auth;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        // Inisialisasi view
        signUpButton = findViewById(R.id.signUpBotton);
        usernameSignUp = findViewById(R.id.userSignUp);
        passwordSignUp = findViewById(R.id.password);
        nimPengguna = findViewById(R.id.nimPengguna);
        emailPengguna = findViewById(R.id.email);

        // Klik tombol daftar
        signUpButton.setOnClickListener(view -> {
            String userName = usernameSignUp.getText() != null ? usernameSignUp.getText().toString().trim() : "";
            String password = passwordSignUp.getText() != null ? passwordSignUp.getText().toString().trim() : "";
            String email = emailPengguna.getText() != null ? emailPengguna.getText().toString().trim() : "";
            String NIM = nimPengguna.getText() != null ? nimPengguna.getText().toString().trim() : "";

            if (TextUtils.isEmpty(userName)) {
                usernameSignUp.setError("Masukkan Username!");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordSignUp.setError("Masukkan Password!");
                return;
            }
            if (password.length() < 6) {
                passwordSignUp.setError("Password minimal 6 karakter!");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailPengguna.setError("Masukkan Email!");
                return;
            }
            if (TextUtils.isEmpty(NIM)) {
                nimPengguna.setError("Masukkan NIM!");
                return;
            }

            registerUser(userName, email, password, NIM);
        });
    }

    private void registerUser(String username, String email, String password, String NIM) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fUser = auth.getCurrentUser();
                        if (fUser != null) {
                            String uid = fUser.getUid();

                            User userDetails = new User(uid, username, email, password, NIM);

                            databaseRef.child(uid).setValue(userDetails).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    fUser.sendEmailVerification();
                                    Toast.makeText(Register.this, "Akun berhasil dibuat. Verifikasi email Anda!", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(Register.this, Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Register.this, "Gagal menyimpan data pengguna!", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Database Error: ", task1.getException());
                                }
                            });
                        }
                    } else {
                        Toast.makeText(Register.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Auth Error: ", task.getException());
                    }
                });
    }
}

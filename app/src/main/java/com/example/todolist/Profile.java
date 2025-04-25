package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvNIM;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private FirebaseUser fUser;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi view dan Firebase
        initializeViews();
        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        // Jika pengguna terautentikasi
        if (fUser != null) {
            loadUserProfile(fUser.getUid());
        }

        // Tombol back untuk kembali ke landing page
        backButton.setOnClickListener(view -> navigateToLandingPage());
    }

    private void initializeViews() {
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvNIM = findViewById(R.id.tvNIM);
        backButton = findViewById(R.id.back);
    }

    private void loadUserProfile(String userId) {
        databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("userEmail").getValue(String.class);
                    String nim = snapshot.child("userNIM").getValue(String.class);

                    updateProfileUI(username, email, nim);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvUsername.setText("Gagal mengambil data.");
            }
        });
    }

    private void updateProfileUI(String username, String email, String nim) {
        tvUsername.setText("Username: " + username);
        tvEmail.setText("Email: " + email);
        tvNIM.setText("NIM: " + nim);
    }

    private void navigateToLandingPage() {
        startActivity(new Intent(Profile.this, LandingPage.class));
    }
}

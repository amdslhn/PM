package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    private TextView tvUsername, tvEmail, tvNIM, back;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private FirebaseUser fUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvNIM = findViewById(R.id.tvNIM);
        back = findViewById(R.id.back);

        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        if (fUser != null) {
            String userId = fUser.getUid();

            databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        String email = snapshot.child("userEmail").getValue(String.class);
                        String nim = snapshot.child("userNIM").getValue(String.class);

                        tvUsername.setText("Username: " + username);
                        tvEmail.setText("Email: " + email);
                        tvNIM.setText("NIM: " + nim);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvUsername.setText("Gagal mengambil data.");
                }
            });
            back.setOnClickListener(view -> {
                startActivity(new Intent(Profile.this, LandingPage.class));
            });

        }
    }
}

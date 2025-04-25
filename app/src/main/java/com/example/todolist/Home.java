package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    private Button buttonMulai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mengaktifkan fitur Edge to Edge
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Mengatur padding untuk tampilan sesuai dengan insets dari sistem
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi Button Mulai dan menetapkan listener untuk klik
        buttonMulai = findViewById(R.id.buttonMulai);
        buttonMulai.setOnClickListener(view -> navigateToLandingPage());
    }

    // Metode untuk navigasi ke halaman LandingPage
    private void navigateToLandingPage() {
        Intent intent = new Intent(Home.this, LandingPage.class);
        startActivity(intent);
    }
}

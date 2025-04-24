package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends AppCompatActivity {

    private EditText setTodo;
    private Button toggleTodoButton;
    private RecyclerView recyclerView;
    private Todo adapter;
    private List<TodoAdapter> todoList = new ArrayList<>();
    private DatabaseReference todoRef;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Firebase
        todoRef = FirebaseDatabase.getInstance().getReference("todos");

        // Views
        setTodo = findViewById(R.id.setTodo);
        toggleTodoButton = findViewById(R.id.toggleTodoButton);
        recyclerView = findViewById(R.id.todoRecyclerView);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // RecyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Todo(todoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);

        toggleTodoButton.setOnClickListener(v -> {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
                toggleTodoButton.setText("Tampilkan Daftar Todo");
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                toggleTodoButton.setText("Sembunyikan Daftar Todo");
            }
        });

        findViewById(R.id.fabAddTodo).setOnClickListener(v -> tambahTodo());

        // ✅ Navigasi bottom
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
                startActivity(new Intent(LandingPage.this, Profile.class));
                return true;
            } else if (id == R.id.menu_tasks) {
                // sudah di halaman todo (beranda)
                return true;
            }
            return false;
        });

        // Ambil data dari Firebase
        loadTodos();
    }

    private void tambahTodo() {
        String text = setTodo.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Isi todo dulu", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = todoRef.push().getKey();
        TodoAdapter todo = new TodoAdapter(text, false);
        todo.setId(id);
        if (id != null) {
            todoRef.child(id).setValue(todo)
                    .addOnSuccessListener(a -> {
                        Toast.makeText(this, "Todo disimpan", Toast.LENGTH_SHORT).show();
                        setTodo.setText("");
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show());
        }
    }

    private void loadTodos() {
        todoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                todoList.clear();
                for (DataSnapshot ds : snap.getChildren()) {
                    TodoAdapter todo = ds.getValue(TodoAdapter.class);
                    if (todo != null) {
                        todo.setId(ds.getKey());
                        todoList.add(todo);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError err) {
                Toast.makeText(LandingPage.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Saat kembali dari ProfileActivity, fokuskan ulang ke tab "beranda"
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.menu_tasks);
        }
    }
}

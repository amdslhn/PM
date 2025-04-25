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

    private EditText todoInput;
    private Button toggleButton;
    private RecyclerView todoRecyclerView;
    private Todo todoAdapter;
    private final List<TodoAdapter> todos = new ArrayList<>();
    private DatabaseReference todoRef;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Inisialisasi Firebase
        todoRef = FirebaseDatabase.getInstance().getReference("todos");

        // Inisialisasi View
        todoInput = findViewById(R.id.setTodo);
        toggleButton = findViewById(R.id.toggleTodoButton);
        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        bottomNav = findViewById(R.id.bottomNavigation);

        // Setup RecyclerView
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new Todo(todos);
        todoRecyclerView.setAdapter(todoAdapter);
        todoRecyclerView.setVisibility(View.GONE);

        toggleButton.setOnClickListener(v -> toggleTodoList());
        findViewById(R.id.fabAddTodo).setOnClickListener(v -> tambahTodo());

        // Navigasi bawah
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
                startActivity(new Intent(this, Profile.class));
                return true;
            } else if (id == R.id.menu_tasks) {
                return true;
            }
            return false;
        });

        loadTodos();
    }

    private void toggleTodoList() {
        boolean visible = todoRecyclerView.getVisibility() == View.VISIBLE;
        todoRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
        toggleButton.setText(visible ? "Tampilkan Daftar Todo" : "Sembunyikan Daftar Todo");
    }

    private void tambahTodo() {
        String text = todoInput.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Isi todo dulu", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = todoRef.push().getKey();
        TodoAdapter newTodo = new TodoAdapter(text, false);
        newTodo.setId(id);

        if (id != null) {
            todoRef.child(id).setValue(newTodo)
                    .addOnSuccessListener(a -> {
                        Toast.makeText(this, "Todo disimpan", Toast.LENGTH_SHORT).show();
                        todoInput.setText("");
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
                    );
        }
    }

    private void loadTodos() {
        todoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todos.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TodoAdapter todo = data.getValue(TodoAdapter.class);
                    if (todo != null) {
                        todo.setId(data.getKey());
                        todos.add(todo);
                    }
                }
                todoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LandingPage.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.menu_tasks);
        }
    }
}

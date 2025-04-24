package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Todo extends RecyclerView.Adapter<Todo.TodoViewHolder> {
    private List<TodoAdapter> todoList;

    public Todo(List<TodoAdapter> todoList) {
        this.todoList = todoList;
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox checkBox;
        public CheckBox priorityBox;
        public ImageButton deleteButton;

        public TodoViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.todo_title);
            checkBox = itemView.findViewById(R.id.todo_checkbox);
            priorityBox = itemView.findViewById(R.id.todo_priority);
            deleteButton = itemView.findViewById(R.id.todo_delete);
        }
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoAdapter todo = todoList.get(position);
        holder.title.setText(todo.getTitle());
        holder.checkBox.setChecked(todo.isDone());
        holder.priorityBox.setChecked(todo.isPriority());

        // Simpan update ke Firebase
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setDone(isChecked);
            updateTodo(todo);
        });

        holder.priorityBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setPriority(isChecked);
            updateTodo(todo);
            sortTodos();
        });

        // Tombol hapus
        holder.deleteButton.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("todos")
                    .child(todo.getId()).removeValue();
            todoList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    private void updateTodo(TodoAdapter todo) {
        FirebaseDatabase.getInstance().getReference("todos")
                .child(todo.getId()).setValue(todo);
    }

    private void sortTodos() {
        Collections.sort(todoList, (a, b) -> Boolean.compare(b.isPriority(), a.isPriority()));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}

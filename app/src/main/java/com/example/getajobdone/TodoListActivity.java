package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getajobdone.adpater.TodoListAdapter;
import com.example.getajobdone.databinding.ActivityTodoListBinding;
import com.example.getajobdone.model.serviceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    ActivityTodoListBinding binding;
    private TodoListAdapter adapter;
    private List<serviceModel> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(this));

        binding.btnBack.setOnClickListener(view -> finish());

        binding.fabNavigateToMap.setOnClickListener(view -> {
            Intent intent = new Intent(TodoListActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        todoList = new ArrayList<>();
        adapter = new TodoListAdapter(todoList, this);
        binding.recyclerViewTodoList.setAdapter(adapter);

        fetchTodoList();
    }

    private void fetchTodoList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UsersTodoList")
                .child(auth.getUid()).child("TodoList");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    serviceModel service = dataSnapshot.getValue(serviceModel.class);
                    todoList.add(service);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TodoListActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
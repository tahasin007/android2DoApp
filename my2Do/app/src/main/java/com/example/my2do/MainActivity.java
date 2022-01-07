package com.example.my2do;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my2do.Adapter.ToDoAdapter;
import com.example.my2do.Model.ToDoModel;
import com.example.my2do.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {
    private RecyclerView recyclerView;
    private TextView textView;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;

    private List<ToDoModel> mList;
    private ToDoAdapter adapter;
    ToDoModel singleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.text_view_all_tasks);
        fab = findViewById(R.id.fab);
        myDB = new DataBaseHelper(MainActivity.this);

        mList = new ArrayList<>();
        adapter = new ToDoAdapter(MainActivity.this, myDB);

        mList = myDB.getAllTasks();

        Collections.reverse(mList);
        adapter.setTasks(mList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0 && data != null) {
            String position = data.getStringExtra("position");
            int pos = Integer.parseInt(position);
            singleTask = mList.get(pos);
            myDB.deleteTask(singleTask.getTaskId());
            mList.remove(pos);
            adapter.notifyItemRemoved(pos);
            Toast.makeText(this, "\'" + singleTask.getTaskTitle() + "\' Deleted", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 0 && resultCode == 5 && data != null) {
            String position = data.getStringExtra("position");
            int pos = Integer.parseInt(position);
            singleTask = mList.get(pos);

            Bundle bundle = new Bundle();
            bundle.putInt("id", singleTask.getTaskId());
            bundle.putString("taskTitle", singleTask.getTaskTitle());
            bundle.putString("taskCategory", singleTask.getTaskCategory());
            bundle.putString("taskDescription", singleTask.getTaskDescription());
            bundle.putString("taskDate", singleTask.getTaskDate());
            bundle.putInt("taskStatus", singleTask.getTaskStatus());

            AddNewTask task = new AddNewTask();
            task.setArguments(bundle);
            task.show(getSupportFragmentManager(), task.getTag());
            adapter.notifyDataSetChanged();
        }
    }
}
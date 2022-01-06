package com.example.my2do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.my2do.Model.ToDoModel;
import com.google.gson.Gson;

public class FullScreenTaskActivity extends AppCompatActivity{
    TextView textViewTaskTitle, textViewTaskDescription, textViewTaskDate, textViewTaskCategory, textViewTaskStatus;
    ImageButton btnEdit, btnDelete;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_task);

        textViewTaskTitle = findViewById(R.id.full_screen_text_view_task_title);
        textViewTaskDescription = findViewById(R.id.full_screen_text_view_task_note);
        textViewTaskDate = findViewById(R.id.full_screen_text_view_task_due_date);
        textViewTaskCategory = findViewById(R.id.full_screen_text_view_task_category);
        textViewTaskStatus = findViewById(R.id.full_screen_text_view_task_status);

        btnEdit = findViewById(R.id.image_button_edit);
        btnDelete = findViewById(R.id.image_button_delete);

        String jsonTaskObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonTaskObject = extras.getString("taskObject");
        }
        position = getIntent().getIntExtra("position", -1);
        ToDoModel taskItem = new Gson().fromJson(jsonTaskObject, ToDoModel.class);


//        position = Integer.parseInt(extras.getString("position"));
        if(taskItem != null){
            textViewTaskTitle.setText(taskItem.getTaskTitle());
            textViewTaskDate.setText(taskItem.getTaskDate());
            if(taskItem.getTaskCategory() != null && !taskItem.getTaskCategory().equals("Task Category...")){
                textViewTaskCategory.setText(taskItem.getTaskCategory());
            }
            if(taskItem.getTaskDescription() != null){
                textViewTaskDescription.setText(taskItem.getTaskDescription());
            }
            if(taskItem.getTaskStatus() == 0){
                textViewTaskStatus.setText("In Progress");
                textViewTaskStatus.setTextColor(getResources().getColor(R.color.light_red));
                textViewTaskStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pending_actions_24, 0, 0, 0);
            }
            else if(taskItem.getTaskStatus() == 1){
                textViewTaskStatus.setText("Done");
                textViewTaskStatus.setTextColor(getResources().getColor(R.color.green));
                textViewTaskStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_done_all_green_24, 0, 0, 0);
            }
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("position",String.valueOf(position));
                setResult(0,intent);
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("position",String.valueOf(position));
                setResult(5,intent);
                finish();
            }
        });
    }
}
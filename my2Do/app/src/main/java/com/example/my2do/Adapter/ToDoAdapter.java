package com.example.my2do.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my2do.AddNewTask;
import com.example.my2do.FullScreenTaskActivity;
import com.example.my2do.MainActivity;
import com.example.my2do.Model.ToDoModel;
import com.example.my2do.R;
import com.example.my2do.Utils.DataBaseHelper;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(MainActivity activity, DataBaseHelper myDB) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.taskTitle.setText(item.getTaskTitle());
        holder.taskDescription.setText(item.getTaskDescription());
        if (item.getTaskCategory() != null) {
            holder.cardViewTaskCategoryContainer.setVisibility(View.VISIBLE);
        }

        if (item.getTaskStatus() == 1) {
            holder.taskStatus.setVisibility(View.VISIBLE);
            holder.taskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskTitle.setTypeface(null, Typeface.ITALIC);
        } else if (item.getTaskStatus() == 0) {
            holder.taskStatus.setVisibility(View.GONE);
            holder.taskTitle.setPaintFlags(0);
            holder.taskTitle.setTypeface(null, Typeface.NORMAL);
        }
        holder.taskCategory.setText(item.getTaskCategory());
        holder.taskDate.setText(item.getTaskDate());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void updateTaskStatus(int position) {
        ToDoModel item = mList.get(position);
        if (item.getTaskStatus() == 1) {
            Toast.makeText(activity, "Already Marked as Done", Toast.LENGTH_SHORT).show();
        } else {
            myDB.updateTaskStatus(item.getTaskId());
            notifyDataSetChanged();
            Intent intent = activity.getIntent();
//            activity.finish();
            activity.startActivity(intent);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        TextView taskTitle, taskCategory, taskDescription, taskDate;
        ImageButton popUpButton;
        CardView cardViewTaskCategoryContainer;
        ImageView taskStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.text_view_task_title);
            taskCategory = itemView.findViewById(R.id.text_view_task_category);
            taskDescription = itemView.findViewById(R.id.text_view_task_description);
            taskDate = itemView.findViewById(R.id.text_view_task_date);
            popUpButton = itemView.findViewById(R.id.pop_up_image_button);
            cardViewTaskCategoryContainer = itemView.findViewById(R.id.card_view_category_container);
            taskStatus = itemView.findViewById(R.id.image_view_done_all);

            popUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
            });
        }

        private void showPopupMenu(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuitem) {
            int pos;
            ToDoModel singleTask;
            pos = getAdapterPosition();
            singleTask = mList.get(pos);

            switch (menuitem.getItemId()) {
                case R.id.action_popup_view:
                    Intent i = new Intent(activity, FullScreenTaskActivity.class);
                    i.putExtra("position", pos);
                    i.putExtra("taskObject", new Gson().toJson(singleTask));
                    activity.startActivityForResult(i, 0);
                    return true;

                case R.id.action_popup_delete:
                    myDB.deleteTask(singleTask.getTaskId());
                    mList.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(getContext(), "'" + singleTask.getTaskTitle() + "' Deleted", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.action_popup_edit:
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", singleTask.getTaskId());
                    bundle.putString("taskTitle", singleTask.getTaskTitle());
                    bundle.putString("taskCategory", singleTask.getTaskCategory());
                    bundle.putString("taskDescription", singleTask.getTaskDescription());
                    bundle.putString("taskDate", singleTask.getTaskDate());
                    bundle.putInt("taskStatus", singleTask.getTaskStatus());

                    AddNewTask task = new AddNewTask();
                    task.setArguments(bundle);
                    task.show(activity.getSupportFragmentManager(), task.getTag());
                    notifyDataSetChanged();
                    return true;

                default:
                    return false;
            }
        }
    }
}

package com.example.my2do.Model;

import java.util.Date;

public class ToDoModel {
    private String taskTitle, taskDescription, taskCategory, taskDate;
    private int taskId, taskStatus;

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String formatDateStr(String date){
        String[] arrOfStr = date.split("/");
        String dateFormatted = arrOfStr[2] + '-' + arrOfStr[1] + '-' + arrOfStr[0];
        return dateFormatted;
    }
}

package com.example.noteapp.interfaces;

import com.example.noteapp.models.TaskModel;

public interface ItemClickListener {
    void onItemClick(int position, TaskModel taskModel);
}

package com.example.noteapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.noteapp.models.TaskModel;

@Database(entities = TaskModel.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}

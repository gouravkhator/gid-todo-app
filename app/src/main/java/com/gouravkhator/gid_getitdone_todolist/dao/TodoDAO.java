package com.gouravkhator.gid_getitdone_todolist.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;

import java.util.List;

@Dao
public interface TodoDAO {
    @Insert
    void insertTask(TodoModel... todos);

    @Delete
    void deleteTask(TodoModel... todo);

    @Update
    void updateTask(TodoModel... todo);

    @Query("SELECT * FROM TodoData ORDER BY id DESC")
    LiveData<List<TodoModel>> getTodos();
}

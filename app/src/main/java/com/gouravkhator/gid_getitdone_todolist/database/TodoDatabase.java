package com.gouravkhator.gid_getitdone_todolist.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.gouravkhator.gid_getitdone_todolist.dao.TodoDAO;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;


@Database(entities = {TodoModel.class},version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    public static final String DatabaseName = "todos_db";
    private static TodoDatabase instance;

    public static TodoDatabase getInstance(final Context context){
        if(instance==null){
            instance= Room.databaseBuilder(
                    context.getApplicationContext(),
                    TodoDatabase.class,
                    DatabaseName
            ).build();
        }
        return instance;
    }
    public abstract TodoDAO getTodoDAO();
}

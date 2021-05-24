package com.gouravkhator.gid_getitdone_todolist;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.gouravkhator.gid_getitdone_todolist.asyncTasks.DeleteTodoAsyncTask;
import com.gouravkhator.gid_getitdone_todolist.asyncTasks.InsertTodoAsyncTask;
import com.gouravkhator.gid_getitdone_todolist.asyncTasks.UpdateTodoAsyncTask;
import com.gouravkhator.gid_getitdone_todolist.database.TodoDatabase;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;

import java.util.List;

public class TodoRepository {
    private TodoDatabase todoDatabase;
    public TodoRepository(final Context context){
        todoDatabase = TodoDatabase.getInstance(context);
    }
    public void insertTodo(TodoModel todo){
        new InsertTodoAsyncTask(todoDatabase.getTodoDAO()).execute(todo);
    }
    public void deleteTodo(TodoModel todo){
        new DeleteTodoAsyncTask(todoDatabase.getTodoDAO()).execute(todo);
    }
    public void updateTodo(TodoModel todo){
        new UpdateTodoAsyncTask(todoDatabase.getTodoDAO()).execute(todo);
    }
    public LiveData<List<TodoModel>> getTodo(){
        return todoDatabase.getTodoDAO().getTodos();
    }

}

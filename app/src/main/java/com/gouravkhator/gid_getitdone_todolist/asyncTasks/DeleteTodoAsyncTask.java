package com.gouravkhator.gid_getitdone_todolist.asyncTasks;

import android.os.AsyncTask;

import com.gouravkhator.gid_getitdone_todolist.dao.TodoDAO;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;

public class DeleteTodoAsyncTask extends AsyncTask<TodoModel,Void,Void> {
    private TodoDAO mDao;
    @Override
    protected Void doInBackground(TodoModel... todoModels) {
        mDao.deleteTask(todoModels);
        return null;
    }
    public DeleteTodoAsyncTask(TodoDAO dao){
        mDao= dao;
    }
}

package com.gouravkhator.gid_getitdone_todolist.asyncTasks;

import android.os.AsyncTask;

import com.gouravkhator.gid_getitdone_todolist.dao.TodoDAO;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;

public class UpdateTodoAsyncTask extends AsyncTask<TodoModel,Void,Void> {
    private TodoDAO mDao;
    @Override
    protected Void doInBackground(TodoModel... todoModels) {
        mDao.updateTask(todoModels);
        return null;
    }
    public UpdateTodoAsyncTask(TodoDAO dao){
        mDao= dao;
    }
}

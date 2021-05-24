package com.gouravkhator.gid_getitdone_todolist.asyncTasks;

import android.os.AsyncTask;

import com.gouravkhator.gid_getitdone_todolist.dao.TodoDAO;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;

public class InsertTodoAsyncTask extends AsyncTask<TodoModel,Void,Void> {

    private TodoDAO mDao;
    public InsertTodoAsyncTask(TodoDAO dao){
        mDao = dao;
    }
    @Override
    protected Void doInBackground(TodoModel... todoModels) {
        mDao.insertTask(todoModels);
        return null;
    }
}

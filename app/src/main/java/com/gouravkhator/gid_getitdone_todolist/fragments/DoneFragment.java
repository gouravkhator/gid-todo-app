package com.gouravkhator.gid_getitdone_todolist.fragments;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gouravkhator.gid_getitdone_todolist.R;
import com.gouravkhator.gid_getitdone_todolist.TaskActivity;
import com.gouravkhator.gid_getitdone_todolist.TodoRepository;
import com.gouravkhator.gid_getitdone_todolist.adapters.TodoRecyclerView;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;
import com.gouravkhator.gid_getitdone_todolist.other_ui.VerticalSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoneFragment extends Fragment
        implements TodoRecyclerView.OnCheckClickListener, TodoRecyclerView.OnNoteSelectedListener {

    private RecyclerView recyclerView;
    private ArrayList<TodoModel> doneList;
    private TodoRepository repository;
    private TodoRecyclerView adapter;
    private final String TAG = "DoneFragment : ";
    private SwipeRefreshLayout swipeRefreshLayout;
    public DoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_done, container, false);
        doneList= new ArrayList<>();
        recyclerView = view.findViewById(R.id.doneListRV);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshDone);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList();
            }
        });
        adapter = new TodoRecyclerView(doneList,this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new VerticalSpacingItemDecoration(10,5));
        repository= new TodoRepository(getContext());
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        repository.getTodo().observe(this, new Observer<List<TodoModel>>() {
            @Override
            public void onChanged(@Nullable List<TodoModel> todoModels) {
                if(doneList.size()>0)
                    doneList.clear();
                if(todoModels!=null) {
                    for (TodoModel todo : todoModels) {
                        if (todo.isDoneFlag()) {
                            doneList.add(todo);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onListChanged : "+ doneList.size());
                }
            }
        });

        return view;
    }

    private void updateList() {
        swipeRefreshLayout.setRefreshing(true);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void deleteTodo(final TodoModel todoModel) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Todo")
                .setMessage("The task will be permanently deleted !!! Are you sure to continue ? ")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        repository.deleteTodo(todoModel);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create()
                .show();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            deleteTodo(doneList.get(viewHolder.getAdapterPosition()));
        }
    };

    @Override
    public void onCheckClicked(int position , boolean isChecked) {
        if(isChecked) {
            doneList.get(position).setDoneFlag(true);
        }else{
            doneList.get(position).setDoneFlag(false);
        }
        repository.updateTodo(doneList.get(position));
    }

    @Override
    public void onNoteSelected(int position) {
        Intent intent = new Intent(getContext(), TaskActivity.class);
        intent.putExtra("taskSelected",doneList.get(position));
        startActivity(intent);
    }
}

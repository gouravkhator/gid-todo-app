package com.gouravkhator.gid_getitdone_todolist.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gouravkhator.gid_getitdone_todolist.R;
import com.gouravkhator.gid_getitdone_todolist.TaskActivity;
import com.gouravkhator.gid_getitdone_todolist.TodoRepository;
import com.gouravkhator.gid_getitdone_todolist.adapters.TodoRecyclerView;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;
import com.gouravkhator.gid_getitdone_todolist.other_ui.VerticalSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoFragment extends Fragment
        implements FloatingActionButton.OnClickListener,
        TodoRecyclerView.OnNoteSelectedListener,
        TodoRecyclerView.OnCheckClickListener {


    private RecyclerView recyclerView;
    private FloatingActionButton fabAddTodo;
    private ArrayList<TodoModel> todoList;
    private Context activityContext;
    private String dueDate="";
    private String timeSet="";
    private TodoRecyclerView adapter;
    private EditText titleInput;
    private String title;
    private TodoRepository repository;
    private TimePickerDialog timePickerDialog;
    private String TAG="TodoFragment : ";
    private ArrayList<String> dates , times;
    private Calendar c;
    private int mYear,mMonth,mDay,mHour,mMinute;
    private Calendar dateObject=null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] day_of_week = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    public TodoFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        recyclerView = view.findViewById(R.id.todoListRV);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshTodo);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList();
            }
        });
        fabAddTodo = view.findViewById(R.id.fabAddTasks);
        initAllViews();
        fabAddTodo.setOnClickListener(this);
        //scroll view on touch listener
        return view;
    }

    private void updateList() {
        swipeRefreshLayout.setRefreshing(true);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initAllViews(){
        activityContext = getContext();

        //all lists
        todoList = new ArrayList<>();
        dates = new ArrayList<>();
        times = new ArrayList<>();

        // recycler view and adapters
        recyclerView.setLayoutManager(new LinearLayoutManager(activityContext));
        adapter = new TodoRecyclerView(todoList,this,this);
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(10,5);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);

        // calendar all constants
        c= Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        //setAnimationToRV();

        //swipe to delete
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        //repository
        repository= new TodoRepository(activityContext);
        repository.getTodo().observe(this, new Observer<List<TodoModel>>() {
            @Override
            public void onChanged(@Nullable List<TodoModel> todoModels) {
                if(todoList.size()>0)
                    todoList.clear();
                if(todoModels!=null) {
                    for (TodoModel todo : todoModels) {
                        if (!todo.isDoneFlag()) {
                            todoList.add(todo);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //make a dialog with date picker and TextInputEditText to take note title
        // and save button and discard button in that dialog
        // also the date picker be like in digital mode and on clicking on the background of dialog it converts it to date picker actual
        final Calendar c= Calendar.getInstance();
        final Dialog dialog = new Dialog(activityContext);
        dialog.setTitle("Select Due Date");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.date_picker_layout);
        dialog.show();

        Button cancelDialog = dialog.findViewById(R.id.btnCancelDatePicker);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final DatePicker datePicker = dialog.findViewById(R.id.datePicker1);
        final Button saveBtn = dialog.findViewById(R.id.saveWithoutTime);
        final Button selectTime = dialog.findViewById(R.id.btnOkDatePicker);
        titleInput = dialog.findViewById(R.id.titleInputDialog);
        titleInput.setScroller(new Scroller(getContext()));
        titleInput.setMaxLines(1);
        titleInput.setVerticalScrollBarEnabled(true);
        titleInput.setMovementMethod(new ScrollingMovementMethod());

        datePicker.setMinDate(System.currentTimeMillis());
        datePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateObject = Calendar.getInstance();
                dateObject.set(year,monthOfYear,dayOfMonth);
                dueDate = day_of_week[dateObject.get(Calendar.DAY_OF_WEEK)-1] + " " + dateObject.get(Calendar.DAY_OF_MONTH) + " " + months[dateObject.get(Calendar.MONTH)];
                selectTime.setEnabled(true);
                saveBtn.setEnabled(true);
            }
        });
        title = titleInput.getText().toString();
        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isNotEmptyMod(s.toString())){
                    selectTime.setEnabled(true);
                    saveBtn.setEnabled(true);
                }
                else{
                    selectTime.setEnabled(false);
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(selectTime.isEnabled()) {
            selectTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title = titleInput.getText().toString();
                    if (isNotEmptyMod(title) && dateObject!=null) {
                        Log.d(TAG, "onClick: "+dueDate);
                        /*
                        if(dateObject.getYear()==Calendar.YEAR)
                            dueDate = new SimpleDateFormat("EE dd M").format(dateObject);
                         else
                            dueDate= new SimpleDateFormat("EE dd M YYYY").format(dateObject);*/
                        //month+1 as month is from 0
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);
                        timePickerDialog = new TimePickerDialog(activityContext, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String amPm="";
                                if (hourOfDay <= 12) {
                                    amPm = " AM";
                                } else {
                                    amPm = " PM";
                                    hourOfDay -= 12;
                                }
                                timeSet = hourOfDay + ":" + minute + amPm;
                                //addTodo - method to add it to adapter and database(room persistence).
                                addTodo();
                            }
                        }, mHour, mMinute, false);
                        // if date is set then set the time by showing time picker dialog
                        timePickerDialog.show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "*Title/Date Required", Toast.LENGTH_SHORT).show();
                        selectTime.setEnabled(false);
                    }
                }
            });
        }
        if(saveBtn.isEnabled()){
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title= titleInput.getText().toString();
                    if(isNotEmptyMod(title)){
                        addTodo();
                        dialog.dismiss();
                    }
                    else
                        saveBtn.setEnabled(false);
                }
            });
        }
    }

    private void addTodo() {
        TodoModel newTodo = new TodoModel();
        newTodo.setTitle(title);
        newTodo.setContents("");
        newTodo.setDoneDate("");
        if(dueDate!=null && isNotEmptyMod(dueDate)) {
            Log.d(TAG, "addTodo: " + title + " == " + dueDate+" : "+timeSet);
            newTodo.setDueDate(dueDate);
        }
        else{
            newTodo.setDueDate("");
        }
        dueDate=null;
        title=null;
        timeSet=null;
        newTodo.setDoneFlag(false);
        repository.insertTodo(newTodo);
        if(timePickerDialog!=null)
            timePickerDialog.dismiss();
        // as addTodo is called from save btn also
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
    @Override
    public void onNoteSelected(int position) {
        Intent intent = new Intent(activityContext, TaskActivity.class);
        intent.putExtra("taskSelected",todoList.get(position));
        startActivity(intent);
    }

    @Override
    public void onCheckClicked(int position,boolean isChecked) {
        //set done date by adding done date to to-do model and then setting it here by current date and time
        // also in done fragment take that date and set the done date there
        if(isChecked) {
            todoList.get(position).setDoneFlag(true);
        }else{
            todoList.get(position).setDoneFlag(false);
        }
        repository.updateTodo(todoList.get(position));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            deleteTodo(todoList.get(viewHolder.getAdapterPosition()));
        }
    };

    private boolean isNotEmptyMod(String string){
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDefined(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private Animation outToLeftAnimation(int duration) {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(duration);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation outToRightAnimation(int duration) {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(duration);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}
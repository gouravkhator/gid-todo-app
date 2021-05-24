package com.gouravkhator.gid_getitdone_todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;

import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    private ImageView deleteDate,deleteTime;
    private CheckBox checkBox;
    private EditText titleInput, contentsInput, dueDateInput, dueTimeInput;
    private Button saveBtn;
    private TodoModel todo=null;
    private Calendar c,dateCalendar;
    private int mYear,mMonth,mDay,mHour,mMinute;

    private TextView toolbarTitle;
    private String[] day_of_week = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private String dueDate = "",dueTime="" , contents = "", title = "";

    private Toolbar toolbar;
    private TodoRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        titleInput = findViewById(R.id.titleEditText);
        contentsInput = findViewById(R.id.contentsEditText);
        dueDateInput = findViewById(R.id.dueDateEditText);
        dueTimeInput = findViewById(R.id.dueTimeEditText);
        saveBtn = findViewById(R.id.btnSaveInTask);
        checkBox = findViewById(R.id.checkboxTaskPage);
        deleteDate = findViewById(R.id.deleteDateBtn);
        deleteTime = findViewById(R.id.deleteTimeBtn);

        toolbarTitle = findViewById(R.id.toolbarTaskTitle);
        repository = new TodoRepository(this);
        toolbar= findViewById(R.id.taskPageToolbar);
        titleInput.setScroller(new Scroller(this));
        titleInput.setMaxLines(1);
        titleInput.setVerticalScrollBarEnabled(true);
        titleInput.setMovementMethod(new ScrollingMovementMethod());
        contentsInput.setScroller(new Scroller(this));
        contentsInput.setMaxLines(5);
        contentsInput.setVerticalScrollBarEnabled(true);
        contentsInput.setMovementMethod(new ScrollingMovementMethod());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("taskSelected")){
            todo = getIntent().getParcelableExtra("taskSelected");
            setDefaultValues();
        }
        else{
            finish();
        }
    }

    private void setDefaultValues() {
        if(todo!=null){
            toolbarTitle.setText("My Todo");
            if(isNotEmptyMod(todo.getTitle())) {
                titleInput.setText(todo.getTitle());
                toolbarTitle.setText(todo.getTitle());
            }
            if(isNotEmptyMod(todo.getContents()))
                contentsInput.setText(todo.getContents());
            if(isNotEmptyMod(todo.getDueDate()))
                dueDateInput.setText(todo.getDueDate());
            if(isNotEmptyMod(todo.getDueTime()))
                dueTimeInput.setText(todo.getDueTime());

            titleInput.setScroller(new Scroller(this));
            titleInput.setMaxLines(1);
            titleInput.setVerticalScrollBarEnabled(true);
            titleInput.setMovementMethod(new ScrollingMovementMethod());
            contentsInput.setScroller(new Scroller(this));
            contentsInput.setMaxLines(5);
            contentsInput.setVerticalScrollBarEnabled(true);
            contentsInput.setMovementMethod(new ScrollingMovementMethod());

            checkBox.setChecked(todo.isDoneFlag());
            dueDateInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(isNotEmptyMod(s.toString())){
                        dueTimeInput.setEnabled(true);
                        deleteTime.setEnabled(true);
                    }
                    else{
                        dueTimeInput.setEnabled(false);
                        deleteTime.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            titleInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(isNotEmptyMod(s.toString())) {

                        //if title is not empty then save button and contents writing is enabled but
                        // its empty then contents writing is disabled and save btn is also disabled
                        todo.setTitle(s.toString());
                        saveBtn.setEnabled(true);
                        contentsInput.setEnabled(true);
                        dueDateInput.setEnabled(true);
                        checkBox.setEnabled(true);
                        deleteDate.setEnabled(true);
                    }
                    else{
                        Toast.makeText(TaskActivity.this, "*Title cannot be empty", Toast.LENGTH_SHORT).show();
                        checkBox.setEnabled(false);
                        dueTimeInput.setEnabled(false);
                        dueDateInput.setEnabled(false);
                        contentsInput.setEnabled(false); //want to first set title
                        saveBtn.setEnabled(false);
                        deleteDate.setEnabled(false);
                        deleteTime.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            if(contentsInput.isEnabled()) {
                contentsInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (isNotEmptyMod(s.toString())) {
                            todo.setContents(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }

            c= Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            if(dueDateInput.isEnabled()) {
                dueDateInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dialog = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateCalendar = Calendar.getInstance();
                                dateCalendar.set(year, month, dayOfMonth);
                                dueDateInput.setText(day_of_week[dateCalendar.get(Calendar.DAY_OF_WEEK) - 1] + " " +
                                        dateCalendar.get(Calendar.DAY_OF_MONTH) + " " + months[dateCalendar.get(Calendar.MONTH)]);
                            }
                        }, mYear, mMonth, mDay);
                        dialog.show();
                    }
                });
                dueTimeInput.setEnabled(true);
                dueTimeInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog dialog = new TimePickerDialog(TaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String amPm = "";
                                if (hourOfDay <= 12) {
                                    amPm = " AM";
                                } else {
                                    amPm = " PM";
                                    hourOfDay -= 12;
                                }
                                dueTimeInput.setText(hourOfDay + ":" + minute + amPm);
                            }
                        }, mHour, mMinute, false);
                        dialog.show();
                    }
                });
            }

            if(checkBox.isEnabled()){
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        todo.setDoneFlag(isChecked);
                    }
                });
            }
            if(deleteDate.isEnabled()){
                deleteDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNotEmptyMod(dueDateInput.getText().toString())){
                            dueDateInput.setText("");
                        }
                    }
                });
            }
            if(deleteTime.isEnabled()){
                deleteTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNotEmptyMod(dueTimeInput.getText().toString())){
                            dueTimeInput.setText("");
                        }
                    }
                });
            }
            save();
        }
    }
    private void save(){
        if(saveBtn.isEnabled()){
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title = titleInput.getText().toString();
                    contents = contentsInput.getText().toString();
                    dueDate = dueDateInput.getText().toString();
                    dueTime = dueTimeInput.getText().toString();
                    if(isNotEmptyMod(title))
                        todo.setTitle(title);
                    if(isNotEmptyMod(contents))
                        todo.setContents(contents);
                    if (isNotEmptyMod(dueDate))
                        todo.setDueDate(dueDate);
                    if(isNotEmptyMod(dueTime))
                        todo.setDueTime(dueTime);

                    todo.setDoneFlag(checkBox.isChecked());
                    Toast.makeText(TaskActivity.this, "Task is successfully saved", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        title = titleInput.getText().toString();
        contents = contentsInput.getText().toString();
        dueDate = dueDateInput.getText().toString();
        dueTime = dueTimeInput.getText().toString();
        if(isNotEmptyMod(title))
            todo.setTitle(title);
        if(isNotEmptyMod(contents))
            todo.setContents(contents);
        if (isNotEmptyMod(dueDate))
            todo.setDueDate(dueDate);
        if(isNotEmptyMod(dueTime))
            todo.setDueTime(dueTime);

        todo.setDoneFlag(checkBox.isChecked());
        repository.updateTodo(todo);
        super.onBackPressed();
    }

    private boolean isNotEmptyMod(String string){
        if(string!=null) {
            for (int i = 0; i < string.length(); i++) {
                if (Character.isDefined(string.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }


}

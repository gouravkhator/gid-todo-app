package com.gouravkhator.gid_getitdone_todolist.adapters;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gouravkhator.gid_getitdone_todolist.R;
import com.gouravkhator.gid_getitdone_todolist.models.TodoModel;

import java.util.ArrayList;
import java.util.HashMap;


public class TodoRecyclerView extends RecyclerView.Adapter<TodoRecyclerView.MyViewHolder>{

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    ArrayList<TodoModel> itemsPendingRemoval = new ArrayList<>();
    private Handler handler = new Handler();
    private HashMap<TodoModel,Runnable> pendingRunnables = new HashMap<>();
    private boolean undoOn;
    private ArrayList<TodoModel> todoList;
    private OnNoteSelectedListener onNoteSelectedListener;
    private OnCheckClickListener onCheckClickListener;
    public TodoRecyclerView(ArrayList<TodoModel> todoList , OnNoteSelectedListener onNoteSelectedListener,OnCheckClickListener onCheckClickListener) {
        this.onNoteSelectedListener = onNoteSelectedListener;
        this.todoList = todoList;
        this.onCheckClickListener = onCheckClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_view_holder,null);
        MyViewHolder viewHolder = new MyViewHolder(view,onNoteSelectedListener,onCheckClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String title="",dueDate="",contents="";
        final TodoModel todo = todoList.get(i);
        if(todo.getTitle()!=null)
            title= todo.getTitle();
        if(todo.getDueDate()!=null)
            dueDate= todo.getDueDate();
        if(todo.getContents()!=null)
            contents= todo.getContents();

        if(title.length()>17){
            title = title.substring(0,17)+"...";
        }
        if(contents.length()>20)
            contents = contents.substring(0,20) + "...";

        myViewHolder.title.setText(title);
        myViewHolder.details.setText("Due : "+dueDate+"\n"+"Notes : "+contents);
        //set details by converging due date and contents
        if(todo.isDoneFlag()) {
            myViewHolder.checkBox.setChecked(true);
        }
        else
            myViewHolder.checkBox.setChecked(false);


    }

    private boolean isNotEmptyMod(String string){
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDefined(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,details;
        CheckBox checkBox;
        public MyViewHolder(@NonNull final View itemView, final OnNoteSelectedListener onNoteSelectedListener, final OnCheckClickListener onCheckClickListener) {
            super(itemView);
            title=itemView.findViewById(R.id.note_title);
            details = itemView.findViewById(R.id.note_short_details);
            checkBox = itemView.findViewById(R.id.checkBoxTodo);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onCheckClickListener.onCheckClicked(getAdapterPosition(),isChecked);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.animate().setDuration(1000).alpha(0)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    onNoteSelectedListener.onNoteSelected(getAdapterPosition());
                                    itemView.setAlpha(1);
                                }
                            });
                }
            });
        }
    }
    public interface OnNoteSelectedListener{
        void onNoteSelected(int position);
    }
    public interface OnCheckClickListener{
        void onCheckClicked(int position,boolean isChecked);
    }
}

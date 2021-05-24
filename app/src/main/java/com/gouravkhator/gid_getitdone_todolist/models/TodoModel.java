package com.gouravkhator.gid_getitdone_todolist.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;


@Entity(tableName = "TodoData")
public class TodoModel implements Parcelable,Comparable<TodoModel> {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="dueDate")
    private String dueDate;
    @ColumnInfo(name="dueTime")
    private String dueTime;
    @ColumnInfo(name="doneDate")
    private String doneDate;
    @ColumnInfo(name="contents")
    private String contents;
    @ColumnInfo(name="doneFlag")
    private boolean doneFlag;
    @Ignore
    public TodoModel(){

    }


    protected TodoModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        dueDate = in.readString();
        dueTime = in.readString();
        doneDate = in.readString();
        contents = in.readString();
        doneFlag = in.readByte() != 0;
    }

    public static final Creator<TodoModel> CREATOR = new Creator<TodoModel>() {
        @Override
        public TodoModel createFromParcel(Parcel in) {
            return new TodoModel(in);
        }

        @Override
        public TodoModel[] newArray(int size) {
            return new TodoModel[size];
        }
    };

    public boolean isDoneFlag() {
        return doneFlag;
    }

    public void setDoneFlag(boolean doneFlag) {
        this.doneFlag = doneFlag;
    }

    public String getContents() {
        return contents;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(dueDate);
        dest.writeString(dueTime);
        dest.writeString(doneDate);
        dest.writeString(contents);
        dest.writeByte((byte) (doneFlag ? 1 : 0));
    }


    public TodoModel(int id, String title, String dueDate, String dueTime, String doneDate, String contents, boolean doneFlag) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.doneDate = doneDate;
        this.contents = contents;
        this.doneFlag = doneFlag;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    @Override
    public int compareTo(TodoModel o) {
        /*try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMMM");
            if(isNotEmptyMod(getDueDate()) && isNotEmptyMod(o.getDueDate()))
                return dateFormat.parse(getDueDate()).compareTo(dateFormat.parse(o.getDueDate()));
            else
                return 0;
        }catch (ParseException e){
            return 0;
        }*/
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMMM HH:mm a");
        SimpleDateFormat onlyDate = new SimpleDateFormat("EE dd MMMM");
        try {
            String first = getDueDate()+" "+getDueTime();
            String second = o.getDueDate()+" "+o.getDueTime();
            boolean fTotal = isNotEmptyMod(first);
            boolean sTotal = isNotEmptyMod(second);
            boolean fDateB = isNotEmptyMod(getDueDate())&& !isNotEmptyMod(getDueTime());
            boolean sDateB = isNotEmptyMod(o.getDueDate()) && !isNotEmptyMod(o.getDueTime());
            int result=0;
            if(fTotal && sTotal){
                result = dateFormat.parse(first).compareTo(dateFormat.parse(second));
            }
            else if((!fTotal && sTotal) || (fTotal && !sTotal) || (!fTotal && !sTotal))
                result = 0;
            else if(fDateB && sTotal)
                result = onlyDate.parse(getDueDate()).compareTo(dateFormat.parse(second));
            else if(fTotal && sDateB)
                result = dateFormat.parse(first).compareTo(onlyDate.parse(o.getDueDate()));
            else if(fDateB && sDateB)
                result = onlyDate.parse(getDueDate()).compareTo(onlyDate.parse(o.getDueDate()));

            return result;
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }*/
        return 0;
    }
    private boolean isNotEmptyMod(String string) {
        if (string == null) {
            return false;
        }
        else if (string.contains("null")){
            return false;
        }
        else {
            for (int i = 0; i < string.length(); i++) {
                if (Character.isDefined(string.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }
}

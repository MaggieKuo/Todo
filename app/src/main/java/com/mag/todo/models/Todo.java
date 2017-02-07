package com.mag.todo.models;

import android.os.Parcelable;

import com.mag.todo.databases.TodoDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Created by Maggie on 2017/1/13.
 */


@EqualsAndHashCode(callSuper = false)
@Table(database = TodoDB.class)
@Data
@Parcel(analyze = {Todo.class})
public class Todo extends BaseModel implements Parcelable {
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 0;

    public static final int STATUS_TO_DO = 0;
    public static final int STATUS_DONE = 1;
    public static final String TODO_BEAN = "TODO";
    //    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String EDIT_MODE = "MODE";
    public static final String EDIT_MODE_ADD = "ADD";
    public static final String EDIT_MODE_MODIFY = "MODIFY";
    public static final String POSITION = "POSITION";

    @PrimaryKey(autoincrement = true)
    @Column
    long _id;

    @Column
    Date todoDate;

    @Column
    int priority;

    @Column
    String detail;

    @Column
    String item;

    @Column
    int status;

    String cdate;

 /*   public static List<Todo> getTodoList(){
        List<Todo> todoList = SQLite.select()
                .from(Todo.class)
                .orderBy(Todo_Table.todoDate, true)
                .queryList();
        return todoList;
    }

    public static List<Todo> getTodoList(int status){
        List<Todo> todoList = SQLite.select()
                .from(Todo.class)
                .where(Todo_Table.status.eq(status))
                .orderBy(Todo_Table.todoDate, true)
                .queryList();

        return todoList;
    }*/

    public Todo() {
    }

    @ParcelConstructor
    public Todo(long _id, String detail, String item, int priority, int status, Date todoDate) {
        this._id = _id;
        this.detail = detail;
        this.item = item;
        this.priority = priority;
        this.status = status;
        this.todoDate = todoDate;
    }

    protected Todo(android.os.Parcel in) {
        _id = in.readLong();
        priority = in.readInt();
        detail = in.readString();
        item = in.readString();
        status = in.readInt();
        todoDate = (Date) in.readSerializable();
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeInt(priority);
        dest.writeString(detail);
        dest.writeString(item);
        dest.writeInt(status);
        dest.writeSerializable(todoDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(android.os.Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public String getCdate(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(todoDate);
    }
}

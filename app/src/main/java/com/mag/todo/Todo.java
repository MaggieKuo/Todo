package com.mag.todo;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Created by Maggie on 2017/1/13.
 */

@Table(database = TodoDB.class)
@Parcel(analyze = {Todo.class})
@Data
public class Todo extends BaseModel {
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 0;

    public static final int STATUS_TO_DO = 0;
    public static final int STATUS_DONE = 1;
    public static final String TODO_BEAN = "TODO";
    //    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

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

    public static List<Todo> getTodoList(){
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
    }

    public String getCdate(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(todoDate);
    }
}

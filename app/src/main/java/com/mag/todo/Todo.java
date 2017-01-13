package com.mag.todo;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

/**
 * Created by Maggie on 2017/1/13.
 */
@Table(database = TodoDB.class)
@Parcel(analyze = {Todo.class})
public class Todo extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    long _id;

    @Column
    String item;
}

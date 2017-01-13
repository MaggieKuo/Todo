package com.mag.todo;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Maggie on 2017/1/13.
 */
@Database(name = TodoDB.NAME, version = TodoDB.VERSION)
public class TodoDB {
    public static final String NAME = "TodoDB";
    public static final int VERSION = 1;
}

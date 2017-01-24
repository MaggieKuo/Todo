package com.mag.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoRecyclerAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_EDIT = 6;
    private RecyclerView lvItems;
    private RadioGroup radioGroup_order;
    private CheckBox display_overdue;
    private CheckBox display_done;
    private List<Todo> lists = null;
    private TodoRecyclerAdapter adapter;
    private int orderBy = R.id.radio_priority;
    private boolean isCheckedOverdue = true;
    private boolean isCheckedDone = false;
    private SQLCondition condition2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViews();
/*
        List<Todo> lists = SQLite.select()
                .from(Todo.class)
                .queryList();

        Log.d(TAG, "size="+lists.size());

        Todo item = new Todo();
        item.setItem("todo item 1");
        item.setPriority(Todo.PRIORITY_HIGH);
        item.setTodoDate(new Date());
        item.setStatus(Todo.STATUS_TO_DO);
        item.save();

        lists = SQLite.select()
                .from(Todo.class)
                .queryList();

        Log.d(TAG, "size="+lists.size());
*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditTodoActivity.class);
                intent.putExtra(Todo.EDIT_MODE, Todo.EDIT_MODE_ADD);
                startActivityForResult(intent, REQUEST_EDIT);
/*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
                }
            });
    }

    private void findViews() {
        radioGroup_order = (RadioGroup) findViewById(R.id.radiogroup_order);
        radioGroup_order.check(R.id.radio_priority);

        display_overdue = (CheckBox) findViewById(R.id.check_overdue);
        display_done = (CheckBox) findViewById(R.id.check_done);

        lvItems = (RecyclerView) findViewById(R.id.lvItems2);
        lvItems.setHasFixedSize(true);
        lvItems.setLayoutManager(new LinearLayoutManager(this));
        lvItems.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new TodoRecyclerAdapter((ArrayList<Todo>) lists, this);
        lvItems.setAdapter(adapter);
        change_search();

        // order by
        radioGroup_order.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                orderBy = checkedId;
                change_search();
            }
        });

        // display done data
        display_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedDone = isChecked;
                change_search();
            }
        });

        // display overdue data
        display_overdue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOverdue = isChecked;
                change_search();
            }
        });

    }

    private void change_search(){
        IProperty iProperty = Todo_Table.todoDate;
        boolean orderASC = true;
        SQLCondition condition =  Todo_Table.status.notEq(Todo.STATUS_DONE);;
        SQLCondition condition2 = Todo_Table.todoDate.greaterThanOrEq(new Date());
        switch (orderBy) {
            case R.id.radio_priority:
                iProperty = Todo_Table.priority;
                iProperty.plus(Todo_Table.todoDate);
                orderASC = false;
                break;
            case R.id.radio_duedate:
                iProperty = Todo_Table.todoDate;
                iProperty.plus(Todo_Table.priority);
                orderASC = true;
                break;
        }

        if (!isCheckedDone && !isCheckedOverdue){
            lists = SQLite.select()
                    .from(Todo.class)
                    .where(condition)
                    .and(condition2)
                    .orderBy(iProperty, orderASC)
                    .queryList();
        }else if (!isCheckedDone){
            lists = SQLite.select()
                    .from(Todo.class)
                    .where(condition)
                    .orderBy(iProperty, orderASC)
                    .queryList();
        }else if (!isCheckedOverdue){
            lists = SQLite.select()
                    .from(Todo.class)
                    .where(condition2)
                    .orderBy(iProperty, orderASC)
                    .queryList();
        }else{
            lists = SQLite.select()
                    .from(Todo.class)
                    .orderBy(iProperty, orderASC)
                    .queryList();
        }

        adapter.setTodoLists((ArrayList<Todo>) lists);
    }


    @Override
    public void onItemCheck(Todo todo, boolean isChecked) {
        todo.setStatus(isChecked ? Todo.STATUS_DONE : Todo.STATUS_TO_DO);
        todo.save();
    }

    @Override
    public void onItemClick(Todo todo, int position) {
        Log.d(TAG, "onItemClick " + todo.getItem() + "/ position=" + position);
        Intent intent = new Intent(this, EditTodoActivity.class);

        intent.putExtra(Todo.TODO_BEAN, (Parcelable) todo);
        intent.putExtra(Todo.EDIT_MODE, Todo.EDIT_MODE_MODIFY);
        intent.putExtra(Todo.POSITION, position);

        startActivityForResult(intent,REQUEST_EDIT );
    }

    @Override
    public void onItemDelete(Todo todo, int position) {
        Log.d(TAG, "onItemDelete " + todo.getItem());
        todo.delete();
        lists.remove(position);
        adapter.notifyDataSetChanged();
        // TODO refrash data
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_EDIT && resultCode==RESULT_OK){
            if (Todo.EDIT_MODE_MODIFY.equals(data.getStringExtra(Todo.EDIT_MODE))){
                int position = data.getIntExtra(Todo.POSITION, -1);
                Log.d(TAG, " onActivityResult  position=" + position);
                if (position>-1){
                    lists.set(position, (Todo) data.getParcelableExtra(Todo.TODO_BEAN));
                    adapter.notifyDataSetChanged();
                }
            }else {
                lists.add(0, (Todo) data.getParcelableExtra(Todo.TODO_BEAN));
                adapter.notifyDataSetChanged();
            }

//            data.getCharExtra("")
//            adapter.notifyDataSetChanged();
//            data.getStringExtra()
//            change_search();
        }


    }

}

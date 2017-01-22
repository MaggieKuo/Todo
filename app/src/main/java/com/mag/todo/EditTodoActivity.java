package com.mag.todo;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Date;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTodoActivity extends AppCompatActivity {

    private static final String TAG = EditTodoActivity.class.getSimpleName();
    @BindView(R.id.checkDone) CheckBox checkDone;
    @BindView(R.id.spinner_priority) Spinner spinnerPriority;
    @BindView(R.id.datePicker2) DatePicker todoDatePicker;
    @BindView(R.id.button) Button btnSave;
//    @BindView(R.id.activity_edit_todo) ConstraintLayout activityEditTodo;
    @BindView(R.id.edit_item) EditText editItem;
    @BindView(R.id.edit_detail) EditText editDetail;

    private ArrayAdapter<CharSequence> adapter;
    private Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        ButterKnife.bind(this);
        todo = getIntent().getParcelableExtra(Todo.TODO_BEAN);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setup();

        if (todo ==null) {
//            todo = new Todo();
            Calendar c = Calendar.getInstance();
            todoDatePicker.updateDate(
                    c.get(c.YEAR),
                    c.get(c.MONTH),
                    c.get(c.DAY_OF_MONTH));
        }else{
            checkDone.setChecked(todo.getStatus()==Todo.STATUS_DONE);
            Calendar c = Calendar.getInstance();
            Log.d(TAG, "todo.getTodoDate()==null : " + (todo.getTodoDate()==null));
            Log.d(TAG, "cdate=" + todo.getCdate());
            c.setTime(todo.getTodoDate());
            todoDatePicker.updateDate(
                    c.get(c.YEAR),
                    c.get(c.MONTH),
                    c.get(c.DAY_OF_MONTH)
            );
            editItem.setText(todo.getItem());
            editDetail.setText(todo.getDetail());
            spinnerPriority.setSelection(todo.getPriority());
        }


    }

    private void setup() {
        adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array,
                android.R.layout.simple_spinner_dropdown_item);
        Log.d(TAG, "count=" + adapter.getCount());
        Log.d(TAG, "spinnerPriority==null : " + (spinnerPriority == null));
        spinnerPriority.setAdapter(adapter);
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, parent.getSelectedItem().toString() + "/" + position + " /" + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.button)
    public void onClick() {
        boolean isAdd = false;
        Log.d(TAG, "save");
        if (TextUtils.isEmpty(editItem.getText().toString())){
            editItem.setError("please enter item");
            return;
        }

        if (todo==null){
            isAdd = true;
            todo = new Todo();
        }

        todo.setStatus(checkDone.isChecked() ? 1 : 0);
        Calendar c = Calendar.getInstance();
        c.set(todoDatePicker.getYear(), todoDatePicker.getMonth(), todoDatePicker.getDayOfMonth());
        todo.setTodoDate(c.getTime());
        todo.setItem(editItem.getText().toString());
        todo.setDetail(editDetail.getText().toString());
        if (isAdd)
            todo.save();
        else
            todo.update();

//        getIntent().putExtra(Todo.TODO_BEAN, (Parcelable) todo);
        setResult(RESULT_OK);
        finish();
    }
}

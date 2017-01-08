package com.mag.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button btnSave;
    private String data;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        data = getIntent().getStringExtra(TodoActivity.EDIT_DATA);
        position = getIntent().getIntExtra(TodoActivity.EDIT_POSITION, -1);
        findViews();

    }

    private void findViews() {
        editText = (EditText) findViewById(R.id.editItem);
        editText.setText(data);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String data = editText.getText().toString();
        if (!TextUtils.isEmpty(data)){
            getIntent().putExtra(TodoActivity.EDIT_DATA, data);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
}

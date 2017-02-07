package com.mag.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mag.todo.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private static final int RC_EDIT = 6;
    public static final String EDIT_POSITION = "POSITION";
    public static final String EDIT_DATA = "data";
    private ListView lvItems;
    private EditText edNewItem;
    private Button btnAddItem;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;
    private final String TODO_FILE = "todo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        findviews();
    }

    private void findviews() {
        lvItems = (ListView) findViewById(R.id.lvItems);
        edNewItem = (EditText) findViewById(R.id.edNewItem);
        readItems();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1, items);
        lvItems.setAdapter(adapter);

        lvItems.setOnItemLongClickListener(this);
        lvItems.setOnItemClickListener(this);

        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additem();
            }
        });
        if (items.size()==0){
            items.add("first item");
            items.add("second item");
        }

    }

    private void additem() {
        String item = edNewItem.getText().toString();
        if (!TextUtils.isEmpty(item)){
            adapter.add(item);
            edNewItem.setText("");
            writeItems();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        writeItems();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(EDIT_POSITION, position);
        intent.putExtra(EDIT_DATA, items.get(position));
        startActivityForResult(intent, RC_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_EDIT && resultCode==RESULT_OK){
            String item = data.getStringExtra(EDIT_DATA);
            int position = data.getIntExtra(EDIT_POSITION, -1);
            if (position>-1){
                items.set(position, item);
                adapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                startActivity(new Intent(this, SettingsActivity.class ));
                break;
        }

        return true;
    }

    private void readItems(){
        File file = getFilesDir();
        File todoFile = new File(file, TODO_FILE);
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File file = getFilesDir();
        File todoFile = new File(file, TODO_FILE);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

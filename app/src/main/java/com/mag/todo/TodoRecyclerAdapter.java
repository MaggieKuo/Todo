package com.mag.todo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Maggie on 2017/1/16.
 */

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder> {

    private ArrayList<Todo> todoLists = new ArrayList<Todo>();

    private Context context;
    private OnItemClickListener itemClickListener;

    public TodoRecyclerAdapter(ArrayList<Todo> todoLists, OnItemClickListener itemClickListener) {
        this.todoLists = todoLists;
        this.itemClickListener = itemClickListener;
    }

    public void setTodoLists(ArrayList<Todo> todoLists) {
        this.todoLists = todoLists;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row, null);
        return new ViewHolder(view, this.itemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (todoLists == null || todoLists.size() == 0) return;
        Todo todo = todoLists.get(position);
        holder.setData(todo, position);
    }

    @Override
    public int getItemCount() {
        return todoLists==null ? 0 : todoLists.size();
    }

    public interface OnItemClickListener{
        void onItemCheck(Todo todo, boolean isChecked);
        void onItemClick(Todo todo, int position);
        void onItemDelete(Todo todo, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnLongClickListener {
        private final TextView date;
        private final TextView item;
        private final OnItemClickListener itemClickListener;
        private final CheckBox done;
        private Todo todo;
        private int position;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);

            done = (CheckBox) itemView.findViewById(R.id.chk_done);
            date = (TextView) itemView.findViewById(R.id.text_date);
            item = (TextView) itemView.findViewById(R.id.text_item);

            done.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.itemClickListener = itemClickListener;
        }

        public void setData(Todo todo, int position){
            itemView.setTag(todo);
            this.todo = todo;
            this.position = position;

            date.setText(todo.getCdate());
            item.setText(todo.getItem());
            done.setChecked(todo.getStatus()==Todo.STATUS_DONE);

                if (todo.getStatus()==Todo.STATUS_DONE){
                    date.setTextColor(context.getResources().getColor(R.color.colorTodoDone));
                }else{
                    if (todo.getTodoDate().before(new Date())){
                        date.setTextColor(context.getResources().getColor(R.color.colorOverdue));
                    }else {
                        date.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
                    }
                    Log.d(TAG, "Priority = " + todo.getPriority());
                        switch (todo.getPriority()) {
                            case Todo.PRIORITY_HIGH:
                                item.setTextColor(context.getResources().getColor(R.color.colorPriorityHigh));
                                break;
                            case Todo.PRIORITY_MEDIUM:
                                item.setTextColor(context.getResources().getColor(R.color.colorPriorityMedium));
                                break;
                            case Todo.PRIORITY_LOW:
                                item.setTextColor(context.getResources().getColor(R.color.colorPriorityLow));
                                break;
                        }

                }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean realStatus = (todo.getStatus() == Todo.STATUS_DONE);

            if (realStatus!=isChecked)
                itemClickListener.onItemCheck(todo, isChecked);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(todo, position);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onItemDelete(todo, position);
            return false;
        }
    }
}

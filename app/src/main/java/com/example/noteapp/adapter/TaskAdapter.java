package com.example.noteapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;
import com.example.noteapp.model.TaskModel;
import com.example.noteapp.databinding.ItemTaskBinding;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    ArrayList<TaskModel> list = new ArrayList<>();
    private int type;

    public void addText(TaskModel taskModel) {
        list.add(taskModel);
        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.txtTitle.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<TaskModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        public TaskViewHolder(View itemView) {
            super(itemView);
            txtTitle  = itemView.findViewById(R.id.item_title);
        }
    }
}

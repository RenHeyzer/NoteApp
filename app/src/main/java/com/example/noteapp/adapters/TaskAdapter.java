package com.example.noteapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.interfaces.ItemClickListener;
import com.example.noteapp.R;
import com.example.noteapp.models.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    List<TaskModel> list;
    List<TaskModel> filteredData;
    public ItemClickListener itemClickListener;
    public boolean isList;

    public TaskAdapter(boolean isList, ItemClickListener itemClickListener) {
        this.list = new ArrayList<>();
        this.filteredData = new ArrayList<>();
        this.itemClickListener = itemClickListener;
        this.isList = isList;
    }

    public TaskModel getWordAtPosition(int position) {
        return list.get(position);
    }

    public void setList(List<TaskModel> modelList) {
        list.clear();
        list.addAll(modelList);
        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.onBind(list.get(position));
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
            txtTitle = itemView.findViewById(R.id.item_title);
        }

        public void onBind(TaskModel taskModel) {
            txtTitle.setText(taskModel.getTitle());

            itemView.setOnClickListener(v -> {
                itemClickListener.onItemClick(getAdapterPosition(), taskModel);
            });
        }
    }
}
